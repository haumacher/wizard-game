/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.JoinAnnounce;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.LeaveAnnounce;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.PlayedCard;
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
 * Game logic controlled by {@link GameCmd} messages and sending {@link GameMsg} messages to corresponding clients.
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
	
	enum State {
		CREATED, STARTED, FINISHED;
	}

	enum GameState {
		TRUMP_SELECTION, BIDDING, LEADING, FINISHING_TURN, FINISHING_ROUND;
	}
	
	private String _id = UUID.randomUUID().toString();
	
	private Map<String, GameClient> _clients = new LinkedHashMap<>();

	private State _state = State.CREATED;
	
	private GameState _gameState;

	private int _round;
	private List<PlayerState> _players;
	
	private Card _trumpCard;
	private Suit _trumpSuit;

	/**
	 * Index of the player in {@link #_players} that must make the first bid.
	 */
	private int _bidOffset;
	
	/**
	 * The number of bids already placed in this round.
	 */
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

	private Consumer<String> _onFinish;

	private Consumer<Msg> _broadCastAll;
	
	private Map<String, GameClient> _lostClients = new HashMap<>();

	private int _winnerOffset;


	public WizardGame() {
		this(msg -> {}, g -> {});
	}
	
	/** 
	 * Creates a {@link WizardGame}.
	 * 
	 * @param broadCastAll Sink for messages that must be sent to all idle clients.
	 * @param onFinish Callback that accepts the ID of the game that has finished.
	 */
	public WizardGame(Consumer<Msg> broadCastAll, Consumer<String> onFinish) {
		_broadCastAll = broadCastAll;
		_onFinish = onFinish;
	}

	/**
	 * The ID of this game.
	 */
	public String getGameId() {
		return _id;
	}

	/**
	 * Description of this game.
	 */
	public Game getData() {
		return Game.create().setGameId(_id).setPlayers(_clients.values().stream().map(c -> c.getData()).collect(Collectors.toList()));
	}
	
	/**
	 * Whether new players can join this game.
	 */
	public synchronized boolean isAcceptingPlayers() {
		return _state == State.CREATED;
	}

	/**
	 * Adds a new player to this game.
	 */
	public synchronized boolean addPlayer(GameClient player) {
		if (_state != State.CREATED) {
			return false;
		}
		
		_clients.put(player.getId(), player);
		broadCast(JoinAnnounce.create().setGameId(_id).setPlayer(player.getData()));
		return true;
	}
	
	/**
	 * Removes a player from this game.
	 */
	public synchronized void removePlayer(GameClient player) {
		if (_state == State.STARTED) {
			// A player lost its connection, prepare for reconnect.
			_lostClients.put(player.getId(), player);
		} else {
			GameClient removedPlayer = _clients.remove(player.getId());
			if (removedPlayer != null) {
				broadCast(LeaveAnnounce.create().setGameId(getGameId()).setPlayerId(removedPlayer.getId()));
			}
		}
		
		if (_clients.size() - _lostClients.size() == 0) {
			gameFinished();
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

	/**
	 * Starts the game.
	 */
	public synchronized void start()  {
		if (_state != State.CREATED) {
			return;
		}
		_state = State.STARTED;
		
		_round = 1;
		_maxRound = CARDS.size() / _clients.size();
		
		_players = _clients.values().stream().map(c -> PlayerState.create().setPlayer(c.getData())).collect(Collectors.toList());
		
		// Choose starting player.
		_bidOffset = (int) (Math.random() * _players.size());

		startRound();
	}

	/** 
	 * Reconnects to this game.
	 */
	public synchronized GameClient reconnect(String clientId, ClientConnection connection) {
		GameClient result = _lostClients.remove(clientId);
		if (result != null) {
			result.reconnectTo(connection);
			
			result.sendMessage(GameStarted.create().setGame(getData()));
			sendStartRound(_players.stream().filter(s -> s.getPlayer().getId().equals(result.getId())).findFirst().get());
			
			switch (_gameState) {
			case TRUMP_SELECTION:
				result.sendMessage(createSelectTrumpMessage());
				break;
				
			case BIDDING:
				result.sendMessage(createStartBiddingMessage());
				result.sendMessage(createBidRequestMessage());
				break;
				
			case LEADING:
				result.sendMessage(createStartLeadMessage());
				result.sendMessage(createRequestPutMessage());
				break;
				
			case FINISHING_TURN:
				result.sendMessage(createStartLeadMessage());
				result.sendMessage(createFinishTurnMessage());
				break;
				
			case FINISHING_ROUND:
				result.sendMessage(createStartLeadMessage());
				result.sendMessage(createFinishRoundMessage());
			}
		}
		return result;
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
			
			sendStartRound(player);
			
			firstCard += _round;
		}
		
		if (isCustomTrumpSelection()) {
			_gameState = GameState.TRUMP_SELECTION;
			
			int trumpSelectIndex = _clients.size() - 1 + _bidOffset;
			_trumpSelectorId = getPlayerId(trumpSelectIndex);
			broadCast(createSelectTrumpMessage());
		} else {
			startBids();
		}
	}

	private RequestTrumpSelection createSelectTrumpMessage() {
		return RequestTrumpSelection.create().setPlayerId(_trumpSelectorId);
	}

	private void sendStartRound(PlayerState player) {
		List<Player> players = _players.stream().map(PlayerState::getPlayer).collect(Collectors.toList());
		StartRound message = 
			StartRound.create()
				.setRound(_round)
				.setMaxRound(_maxRound)
				.setPlayers(players)
				.setStartPlayer(players.get(_bidOffset).getId())
				.setCards(player.getRoundState().getCards())
				.setTrumpCard(_trumpCard);
		getClient(player.getPlayer().getId()).sendMessage(message);
	}

	private void requestNextBid()  {
		broadCast(createBidRequestMessage());
	}

	private RequestBid createBidRequestMessage() {
		return RequestBid.create().setPlayerId(getPlayerId(_bidOffset + _bidCount)).setRound(_round).setExpected(_totalBids);
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
		_gameState = GameState.BIDDING;
		
		_trumpSelectorId = null;
		broadCast(createStartBiddingMessage());
		requestNextBid();
	}

	private StartBids createStartBiddingMessage() {
		return StartBids.create();
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
			broadCast(createStartLeadMessage());
			requestNextPut();
		}
		return null;
	}

	private StartLead createStartLeadMessage() {
		StartLead startLead = StartLead.create();
		for (PlayerState state : _players) {
			startLead.getState().put(state.getPlayer().getId(), 
				PlayerInfo.create()
					.setPoints(state.getPoints())
					.setBid(state.getRoundState().getBidCnt())
					.setTricks(state.getRoundState().getWinCnt()));
		}
		
		// Only relevant when reconnecting to a running game.
		int playerIndex = _turnOffset;
		for (Card card : _turn) {
			startLead.getCurrentTrick().add(PlayedCard.create().setCard(card).setPlayerId(getPlayerId(playerIndex)));
			playerIndex++;
		}
		
		return startLead;
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
		if (!_barrier.isEmpty()) {
			arg.sendError("Es müssen zuerst alle Spieler bestätigen.");
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
			_turnOffset = _winnerOffset;
			nextTurn();
		}
		return null;
	}

	private void nextTurn() {
		_turn = new ArrayList<>();
		
		_turns--;
		if (_turns > 0) {
			requestNextPut();
		} else {
			// Compute points.
			
			FinishRound finishRound = createFinishRoundMessage();
			
			// Apply delta to player state.
			for (PlayerState state : _players) {
				int delta = finishRound.getPoints().get(state.getPlayer().getId());
				int newPoints = state.getPoints() + delta;
				state.setPoints(newPoints);
			}
			
			initBarrier(GameState.FINISHING_ROUND);
			broadCast(finishRound);
		}
	}

	private FinishRound createFinishRoundMessage() {
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
			finishRound.getPoints().put(state.getPlayer().getId(), delta);
		}
		return finishRound;
	}
	
	private void finishTurn() {
		int winningCardIndex = computeWinningCardIndex();
		_winnerOffset = normalizeIndex(_turnOffset + winningCardIndex);
		RoundState winnerRoundState = _players.get(_winnerOffset).getRoundState();
		winnerRoundState.setWinCnt(winnerRoundState.getWinCnt() + 1);
		
		initBarrier(GameState.FINISHING_TURN);
		broadCast(createFinishTurnMessage());
	}

	private FinishTurn createFinishTurnMessage() {
		return FinishTurn.create().setTrick(_turn).setWinner(_players.get(_winnerOffset).getPlayer());
	}

	/** 
	 * Compute the index of the winning card in {@link #_turn}.
	 */
	private int computeWinningCardIndex() {
		int winnerIndex = 0;
		Card best = _turn.get(winnerIndex);
		for (int n = 1, cnt = _turn.size(); n < cnt; n++) {
			Card current = _turn.get(n);
			if (hits(best, current)) {
				winnerIndex = n;
				best = current;
			}
		}
		return winnerIndex;
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
			_bidOffset = normalizeIndex(_bidOffset + 1);
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
		if (_state != State.FINISHED) {
			_state = State.FINISHED;
			
			_onFinish.accept(getGameId());
		}
	}

	private void initBarrier(GameState nextState) {
		_gameState = nextState;
		_barrier.clear();
		_barrier.putAll(_clients);
	}
	
	private void requestNextPut()  {
		_gameState = GameState.LEADING;
		broadCast(createRequestPutMessage());
	}

	private RequestLead createRequestPutMessage() {
		return RequestLead.create().setPlayerId(getPlayerId(_turnOffset + _turn.size()));
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
