/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.haumacher.wizard.msg.Announce;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.FinishGame;
import de.haumacher.wizard.msg.FinishRound;
import de.haumacher.wizard.msg.FinishTurn;
import de.haumacher.wizard.msg.Game;
import de.haumacher.wizard.msg.GameCmd;
import de.haumacher.wizard.msg.GameMsg;
import de.haumacher.wizard.msg.JoinAnnounce;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.LeaveAnnounce;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.PlayerInfo;
import de.haumacher.wizard.msg.PlayerScore;
import de.haumacher.wizard.msg.PlayerState;
import de.haumacher.wizard.msg.RequestBid;
import de.haumacher.wizard.msg.RequestLead;
import de.haumacher.wizard.msg.RequestTrumpSelection;
import de.haumacher.wizard.msg.RoundState;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartBids;
import de.haumacher.wizard.msg.StartLead;
import de.haumacher.wizard.msg.StartRound;
import de.haumacher.wizard.msg.Suit;
import de.haumacher.wizard.msg.Value;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class WizardGame implements GameCmd.Visitor<Void, GameClient, IOException> {
	
	public static final int PROTOCOL_VERSION = 3;
	
	static final List<Card> CARDS;

	static {
		List<Card> cards = new ArrayList<>();
		for (Suit suit : Suit.values()) {
			for (Value value : Value.values()) {
				Card card = Card.create().setValue(value);
				if (value != Value.N && value != Value.Z) {
					card.setSuit(suit);
				} else {
					card.setSuit(null);
				}
				cards.add(card);
			}
		}
		
		CARDS = Collections.unmodifiableList(cards);
	}

	private String _id = UUID.randomUUID().toString();
	
	private Map<String, GameClient> _clients = new ConcurrentHashMap<>();

	private boolean _started;

	private int _round;
	private List<PlayerState> _players;
	
	private Card _trumpCard;
	private Suit _trumpSuit;

	/**
	 * Index of the player in {@link #_players} that must make the first bid.
	 */
	private int _bidOffset;
	private int _bidCount;
	
	/**
	 * Index of the player in {@link #_players} that must make the first put.
	 */
	private int _turnOffset;
	private List<Card> _turn;

	private int _maxRound;

	private int _turns;

	private int _totalBids;

	private ConcurrentMap<String, GameClient> _barrier = new ConcurrentHashMap<>();

	private String _trumpSelectorId;

	private Consumer<Game> _onFinish;

	private Consumer<Msg> _broadCastAll;
	
	public WizardGame() {
		this(msg -> {}, g -> {});
	}
	
	/** 
	 * Creates a {@link WizardGame}.
	 */
	public WizardGame(Consumer<Msg> broadCastAll, Consumer<Game> onFinish) {
		_broadCastAll = broadCastAll;
		_onFinish = onFinish;
	}

	public Game getData() {
		return Game.create().setGameId(_id).setPlayers(_clients.values().stream().map(c -> c.getData()).collect(Collectors.toList()));
	}

	public synchronized boolean addPlayer(GameClient player) {
		if (isStarted()) {
			return false;
		}
		
		_clients.put(player.getId(), player);
		broadCast(JoinAnnounce.create().setGameId(_id).setPlayer(player.getData()));
		return true;
	}
	
	public void removePlayer(GameClient player) {
		GameClient removedPlayer = _clients.remove(player.getId());
		if (removedPlayer != null) {
			broadCast(LeaveAnnounce.create().setGameId(getGameId()).setPlayerId(removedPlayer.getId()));
		}
	}

	private void broadCast(Msg msg) {
		_broadCastAll.accept(msg);
	}
	
	private void broadCast(GameMsg msg) {
		for (GameClient player : _clients.values()) {
			player.sendMessage(msg);
		}
	}

	public boolean isStarted() {
		return _started;
	}

	public String getGameId() {
		return _id;
	}

	public synchronized void start()  {
		if (_started) {
			return;
		}
		_started = true;
		
		_round = 1;
		_maxRound = CARDS.size() / _clients.size();
		
		_players = _clients.values().stream().map(c -> PlayerState.create().setPlayer(c.getData())).collect(Collectors.toList());
		Collections.shuffle(_players);
		
		_bidOffset = 0;

		startRound();
	}

	private void startRound()  {
		_turns = _round;
		_turnOffset = _bidOffset;
		
		_bidCount = 0;
		_totalBids = 0;
		_turn = new ArrayList<>();
		
		List<Card> cards = new ArrayList<>(CARDS);
		Collections.shuffle(cards);
		
		int trumpIndex = _players.size() * _round;
		_trumpCard = trumpIndex < cards.size() ? cards.get(trumpIndex) : null;
		_trumpSuit = _trumpCard != null ? _trumpCard.getSuit() : null;
		
		int firstCard = 0;
		for (PlayerState player : _players) {
			RoundState state = RoundState.create();
			List<Card> playerCards = new ArrayList<>(cards.subList(firstCard, firstCard + _round));
			Collections.sort(playerCards, CardComparator.INSTANCE);
			
			state.getCards().addAll(playerCards);
			player.setRoundState(state);
			
			List<Player> players = _players.stream().map(PlayerState::getPlayer).collect(Collectors.toList());
			StartRound message = 
				StartRound.create()
					.setRound(_round)
					.setMaxRound(_maxRound)
					.setPlayers(players)
					.setCards(playerCards)
					.setTrumpCard(_trumpCard);
			getClient(player.getPlayer().getId()).sendMessage(message);
			
			firstCard += _round;
		}
		
		if (isCustomTrumpSelection()) {
			int trumpSelectIndex = _clients.size() - 1 + _bidOffset;
			_trumpSelectorId = getPlayerId(trumpSelectIndex);
			broadCast(RequestTrumpSelection.create().setPlayerId(_trumpSelectorId));
		} else {
			startBids();
		}
	}

	private void requestNextBid()  {
		broadCast(RequestBid.create().setPlayerId(getPlayerId(_bidOffset + _bidCount)).setRound(_round).setExpected(_totalBids));
	}

	private GameClient getClient(int index) {
		return getClient(getPlayerId(index));
	}

	private GameClient getClient(String playerId) {
		return _clients.get(playerId);
	}

	private String getPlayerId(int index) {
		return getPlayerState(index).getPlayer().getId();
	}

	private PlayerState getPlayerState(int index) {
		return _players.get(normalizeIndex(index));
	}

	private int normalizeIndex(int index) {
		return index % _players.size();
	}

	@Override
	public Void visit(SelectTrump self, GameClient arg)  {
		if (_trumpSuit != null) {
			arg.sendError("Es ist bereits eine Trumpffarbe gewählt.");
			return null;
		}
		if (!arg.getId().equals(_trumpSelectorId)) {
			arg.sendError("Du darfst nicht die Trumpffarbe wählen.");
			return null;
		}
		if (self.getTrumpSuit() == null) {
			arg.sendError("Es muss eine Trumpffarbe gewählt werden.");
			return null;
		}
		if (!isCustomTrumpSelection()) {
			arg.sendError("In dieser Runde kann kein Trumpf gewählt werden.");
			return null;
		}

		_trumpSuit = self.getTrumpSuit();
		forward(arg, self);
		
		startBids();
		return null;
	}

	private boolean isCustomTrumpSelection() {
		return _trumpCard != null && _trumpCard.getValue() == Value.Z;
	}

	private void startBids() {
		_trumpSelectorId = null;
		broadCast(StartBids.create());
		requestNextBid();
	}

	private void forward(GameClient sender, GameCmd msg)  {
		broadCast(Announce.create().setPlayerId(sender.getId()).setCmd(msg));
	}

	@Override
	public Void visit(Bid self, GameClient arg)  {
		if (_bidCount >= _players.size()) {
			arg.sendError("Es sind schon alle Gebote abgegeben.");
			return null;
		}
		PlayerState playerState = getPlayerState(_bidOffset + _bidCount);
		if (!playerState.getPlayer().getId().equals(arg.getId())) {
			arg.sendError("Du bist nicht an der Reihe um ein Gebot abzugeben.");
			return null;
		}
		
		int currentBid = self.getCnt();
		_totalBids += currentBid;
		playerState.getRoundState().setBidCnt(currentBid);
		self.setExpected(_totalBids);
		forward(arg, self);
		
		_bidCount++;
		if (_bidCount < _players.size()) {
			requestNextBid();
		} else {
			StartLead startLead = StartLead.create();
			for (PlayerState state : _players) {
				startLead.getState().put(state.getPlayer().getId(), 
					PlayerInfo.create()
						.setPoints(state.getPoints())
						.setBid(state.getRoundState().getBidCnt())
						.setTricks(state.getRoundState().getWinCnt()));
			}
			broadCast(startLead);
			
			requestNextPut();
		}
		return null;
	}

	@Override
	public Void visit(Lead self, GameClient arg)  {
		if (_bidCount < _players.size()) {
			arg.sendError("Es sind noch nicht alle Gebote angegeben.");
			return null;
		}
		PlayerState playerState = getPlayerState(_turnOffset + _turn.size());
		if (!playerState.getPlayer().getId().equals(arg.getId())) {
			arg.sendError("Du bist nicht an der Reihe.");
			return null;
		}
		
		RoundState roundState = playerState.getRoundState();
		List<Card> deck = roundState.getCards();
		Card putCard = self.getCard();
		Optional<Card> searchResult = 
			deck.stream().filter(c -> c.getSuit() == putCard.getSuit() && c.getValue() == putCard.getValue()).findFirst();
		if (!searchResult.isPresent()) {
			arg.sendError("Du hast eine Karte gespielt, die Du gar nicht besitzt.");
			return null;
		}
		Card playedCard = searchResult.get();
		
		Suit leadSuit = leadSuit(_turn);
		if (leadSuit != null && playedCard.getSuit() != null && playedCard.getSuit() != leadSuit) {
			if (deck.stream().filter(c -> c.getSuit() == leadSuit).findFirst().isPresent()) {
				arg.sendError("Du musst Farbe bekennen.");
				return null;
			}
		}
		
		roundState.getCards().remove(playedCard);
		_turn.add(playedCard);
		forward(arg, self);
		
		if (_turn.size() < _players.size()) {
			requestNextPut();
		} else {
			finishTurn();
		}
		return null;
	}

	/** 
	 * The {@link Suit} the next player must follow if it is in his deck.
	 */
	private Suit leadSuit(List<Card> turn) {
		for (Card card : turn) {
			if (card.getValue() == Value.Z) {
				return null;
			}
			if (card.hasSuit()) {
				return card.getSuit();
			}
		}
		return null;
	}

	@Override
	public Void visit(ConfirmTrick self, GameClient arg) throws IOException {
		GameClient client = _barrier.remove(arg.getId());
		if (client == null) {
			arg.sendError("Du hast den Stich schon bestätigt.");
			return null;
		}
		forward(arg, self);
		
		if (_barrier.isEmpty()) {
			nextTurn();
		}
		return null;
	}

	private void nextTurn() {
		_turns--;
		if (_turns > 0) {
			requestNextPut();
		} else {
			// Compute points.
			FinishRound finishRound = FinishRound.create();
			for (PlayerState state : _players) {
				int hitCnt = state.getRoundState().getWinCnt();
				int bidCnt = state.getRoundState().getBidCnt();
				int delta;
				if (hitCnt == bidCnt) {
					delta = 20 + hitCnt * 10;
				} else {
					delta = -Math.abs(hitCnt - bidCnt) * 10;
				}
				int newPoints = state.getPoints() + delta;
				state.setPoints(newPoints);
				
				finishRound.getPoints().put(state.getPlayer().getId(), delta);
			}
			
			initBarrier();
			broadCast(finishRound);
		}
	}
	
	private void finishTurn() {
		int hitIndex = 0;
		Card best = _turn.get(hitIndex);
		for (int n = 1, cnt = _turn.size(); n < cnt; n++) {
			Card current = _turn.get(n);
			if (hits(best, current)) {
				hitIndex = n;
				best = current;
			}
		}
		
		int winnerOffset = normalizeIndex(_turnOffset + hitIndex);
		PlayerState winnerState = _players.get(winnerOffset);
		RoundState winnerRoundState = winnerState.getRoundState();
		winnerRoundState.setWinCnt(winnerRoundState.getWinCnt() + 1);
		
		_turn = new ArrayList<>();
		_turnOffset = winnerOffset;
		initBarrier();
		
		broadCast(FinishTurn.create().setTrick(_turn).setWinner(winnerState.getPlayer()));
	}

	@Override
	public Void visit(ConfirmRound self, GameClient arg) throws IOException {
		GameClient client = _barrier.remove(arg.getId());
		if (client == null) {
			arg.sendError("Du hast die Runde schon bestätigt.");
			return null;
		}
		forward(arg, self);
		
		if (_barrier.isEmpty()) {
			nextRound();
		}
		return null;
	}

	private void nextRound() {
		_round++;
		if (_round <= _maxRound) {
			_bidOffset++;
			startRound();
		} else {
			finishGame();
		}
	}

	private void finishGame() {
		FinishGame finishGame = FinishGame.create();
		for (PlayerState state : _players) {
			int points = state.getPoints();
			finishGame.addScore(PlayerScore.create().setPlayer(state.getPlayer()).setPoints(points));
		}
		Collections.sort(finishGame.getScores(), (s1, s2) -> -Integer.compare(s1.getPoints(), s2.getPoints()));
		
		broadCast(finishGame);
		
		gameFinished();
	}

	private void gameFinished() {
		_onFinish.accept(getData());
	}

	private void initBarrier() {
		_barrier.clear();
		_barrier.putAll(_clients);
	}
	
	private void requestNextPut()  {
		requestPut(_turnOffset + _turn.size());
	}

	private void requestPut(int nextPlayer) {
		broadCast(RequestLead.create().setPlayerId(getPlayerId(nextPlayer)));
	}

	private boolean hits(Card best, Card current) {
		if (best.getValue() == Value.Z) {
			return false;
		}
		if (current.getValue() == Value.Z) {
			return true;
		}
		if (current.getValue() == Value.N) {
			return false;
		}
		if (best.getValue() == Value.N) {
			return true;
		}
		if (best.getSuit() != _trumpSuit && current.getSuit() == _trumpSuit) {
			return true;
		}
		if (current.getSuit() != best.getSuit()) {
			return false;
		}
		return current.getValue().ordinal() > best.getValue().ordinal();
	}

}
