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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.msg.Announce;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.ConfirmGame;
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
import de.haumacher.wizard.msg.RoundInfo;
import de.haumacher.wizard.msg.RoundState;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartBids;
import de.haumacher.wizard.msg.StartLead;
import de.haumacher.wizard.msg.StartRound;
import de.haumacher.wizard.msg.Suit;
import de.haumacher.wizard.msg.Value;

/**
 * Central server-side game logic.
 * 
 * <p>
 * A {@link WizardGame} receives {@link GameCmd} messages from clients playing the game and sends
 * back {@link GameMsg} messages.
 * </p>
 */
public class WizardGame implements GameCmd.Visitor<Void, GameClient, IOException> {
	
	private static final Logger LOG = LoggerFactory.getLogger(WizardGame.class);
	
	/**
	 * The version of the wire-protocol a client must understand to participate in a game.
	 */
	public static final int PROTOCOL_VERSION = 6;
	
	/**
	 * All {@link Card}s available in the Wizard game.
	 */
	static final List<Card> ALL_CARDS;

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
		
		ALL_CARDS = Collections.unmodifiableList(cards);
	}
	
	/**
	 * The life-cycle state of a {@link WizardGame}
	 */
	enum LifeCycleState {
		/**
		 * The game has been created and is waiting for players to join.
		 */
		CREATED, 
		
		/**
		 * The game is active, no more players can join the game.
		 */
		ACTIVE, 
		
		/**
		 * The last move of the game has been played. The winner of the game is certain.
		 */
		FINISHED;
	}

	/**
	 * The state of an active {@link WizardGame}.
	 */
	enum PlayingState {
		/**
		 * A wizard was chosen as trump card. The player that dealt out the cards must select the
		 * trump color.
		 */
		TRUMP_SELECTION, 
		
		/**
		 * Players are announcing the number of tricks they will make.
		 */
		BIDDING, 
		
		/**
		 * An active round where each player puts a card on the table.
		 */
		LEADING, 
		
		/**
		 * All players have put a card on the table. The winner of the trick has been selected.
		 */
		FINISHING_TURN, 
		
		/**
		 * All players have played all their cards. The number points each player has won or lost
		 * during this round is certain.
		 */
		FINISHING_ROUND,
		
		/**
		 * The last turn was played, the scores are shown to all players.
		 */
		FINISHING_GAME;
	}
	
	/**
	 * @see #getGameId()
	 */
	private String _id = UUID.randomUUID().toString();
	
	private LifeCycleState _lifeCycle = LifeCycleState.CREATED;
	
	/**
	 * The state of an {@link LifeCycleState#ACTIVE} game.
	 */
	private PlayingState _gameState;
	
	private Map<String, GameClient> _clientByPlayerId = new LinkedHashMap<>();

	/**
	 * The ID of the current round starting with 1.
	 * 
	 * <p>
	 * The round ID is equal to the number of cards each player gets dealt at the start of the round.
	 * </p>
	 * 
	 * @see #_turnsLeft
	 */
	private int _round;
	
	/**
	 * The list of players participating in this game.
	 * 
	 * <p>
	 * The order of this list decides about the order of moves played. 
	 * </p>
	 */
	private List<PlayerState> _players;
	
	/**
	 * The {@link Card} that chooses the {@link #_trumpSuit}, if it is not a {@link Value#Z} card.
	 */
	private Card _trumpCard;
	
	/**
	 * The {@link Suit} that is trump during the current round.
	 * 
	 * <p>
	 * It is either chosen by the drawn {@link #_trumpCard}, or by the player that has dealt the
	 * cards, if the {@link #_trumpCard} is a {@link Value#Z} card.
	 * </p>
	 * 
	 * <p>
	 * If the round has no trump, the value is <code>null</code>.
	 * </p>
	 */
	private Suit _trumpSuit;

	/**
	 * Index of the player in {@link #_players} that must make the first bid.
	 * 
	 * <p>
	 * The index is chosen randomly when the game starts and is incremented after each round.
	 * </p>
	 */
	private int _bidOffset;
	
	/**
	 * The number of players that have already placed their bids.
	 * 
	 * @see PlayingState#BIDDING
	 * @see #_tricksAnnounced
	 */
	private int _bidCount;

	/**
	 * The number of tricks announced so far.
	 * 
	 * @see PlayingState#BIDDING
	 */
	private int _tricksAnnounced;
	
	/**
	 * Index of the player in {@link #_players} that plays the first card.
	 * 
	 * <p>
	 * The player that is expected to play the next card is {@link #_startPlayerOffset} plus the size of
	 * {@link #_cardsPlayed}.
	 * </p>
	 */
	private int _startPlayerOffset;
	
	/**
	 * All {@link Card} currently on the table.
	 */
	private List<Card> _cardsPlayed;

	/**
	 * Index of the player in {@link #_players} that won the last trick.
	 * 
	 * <p>
	 * The player that won the last trick plays the first card during the next turn.
	 * </p>
	 * 
	 * @see #_startPlayerOffset
	 */
	private int _trickWinnerOffset;

	/**
	 * The number of rounds to play in this game. 
	 * 
	 * <p>
	 * Depends on the number of players participating in this game.
	 * </p>
	 */
	private int _maxRound;

	/**
	 * The number of cards a player currently holds in his hand.
	 */
	private int _turnsLeft;

	/**
	 * {@link GameClient} by {@link Player#getId()} that are required to confirm the last
	 * announcement before the game might continue.
	 * 
	 * @see ConfirmTrick
	 * @see ConfirmRound
	 */
	private Map<String, GameClient> _barrier = new HashMap<>();

	/**
	 * The {@link Player#getId()} of the player that is allowed to select the trump color during
	 * {@link PlayingState#TRUMP_SELECTION} phase.
	 */
	private String _trumpSelectorId;

	/**
	 * Function transmitting the given {@link Msg} to all players of the game.
	 */
	private Consumer<Msg> _broadCastAll;
	
	/**
	 * Callback invoked after the game has finished.
	 */
	private Consumer<String> _onFinish;
	
	private Map<String, GameClient> _lostClients = new HashMap<>();

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
		// Note at the time, this method is called, the game is not yet started and _players is not yet initialized.
		return Game.create()
			.setGameId(_id)
			.setPlayers(_clientByPlayerId.values().stream().map(GameClient::getData).collect(Collectors.toList()));
	}
	
	/**
	 * Whether new players can join this game.
	 */
	public synchronized boolean isAcceptingPlayers() {
		return _lifeCycle == LifeCycleState.CREATED;
	}

	/**
	 * Adds a new player to this game.
	 */
	public synchronized boolean addPlayer(GameClient player) {
		if (_lifeCycle != LifeCycleState.CREATED) {
			return false;
		}
		
		_clientByPlayerId.put(player.getId(), player);
		broadCastAll(JoinAnnounce.create().setGameId(_id).setPlayer(player.getData()));
		
		LOG.info("Added '" + player + "' to game '" + getGameId() + "'.");
		return true;
	}
	
	/**
	 * Removes a player from this game.
	 */
	public synchronized void removePlayer(GameClient handle) {
		if (_lifeCycle == LifeCycleState.ACTIVE) {
			// A player lost its connection, prepare for reconnect.
			_lostClients.put(handle.getId(), handle);
		} else {
			GameClient removedPlayer = _clientByPlayerId.remove(handle.getId());
			if (removedPlayer != null) {
				broadCastAll(LeaveAnnounce.create().setGameId(getGameId()).setPlayerId(removedPlayer.getId()));
			}
		}
		
		if (_clientByPlayerId.size() - _lostClients.size() == 0) {
			gameFinished();
		}
	}

	private void broadCast(GameMsg msg) {
		for (GameClient player : _clientByPlayerId.values()) {
			player.sendMessage(msg);
		}
	}

	/**
	 * Starts the game.
	 */
	public synchronized void start()  {
		if (_lifeCycle != LifeCycleState.CREATED) {
			LOG.error("Tried to start a game that is not in created state: " + _lifeCycle);
			return;
		}
		_lifeCycle = LifeCycleState.ACTIVE;
		
		_round = 1;
		_players = _clientByPlayerId.values()
			.stream()
			.map(c -> PlayerState.create().setPlayer(c.getData()))
			.collect(Collectors.toList());
		_maxRound = ALL_CARDS.size() / _players.size();
		
		// Test only:
		// _maxRound = 1;
		
		// Choose starting player.
		_bidOffset = (int) (Math.random() * _players.size());
		
		LOG.info("Started game '" + getGameId() + "' with " + _maxRound + " rounds, players: " + _players);

		startRound();
	}

	/** 
	 * Reconnects to this game.
	 */
	public synchronized GameClient reconnect(String clientId, ClientConnection connection) {
		GameClient result = _lostClients.remove(clientId);
		if (result == null) {
			// Server connection might not yet have crashed, but the client may try to reconnect early.
			result = client(clientId);
		}
		if (result != null) {
			result.reconnectTo(connection);
			
			result.sendMessage(GameStarted.create().setGame(getData()));
			sendStartRound(_players.stream().filter(s -> s.getPlayer().getId().equals(clientId)).findFirst().get());
			
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
				result.sendMessage(createRequestLeadMessage());
				break;
				
			case FINISHING_TURN:
				result.sendMessage(createStartLeadMessage());
				result.sendMessage(createFinishTurnMessage());
				break;
				
			case FINISHING_ROUND:
				result.sendMessage(createStartLeadMessage());
				result.sendMessage(createFinishRoundMessage());
				break;
				
			case FINISHING_GAME: 
				result.sendMessage(createStartLeadMessage());
				result.sendMessage(createFinishRoundMessage());
				result.sendMessage(createFinishGameMessage());
				break;
			}
		}
		return result;
	}

	private void startRound()  {
		_turnsLeft = _round;
		_startPlayerOffset = _bidOffset;
		
		_bidCount = 0;
		_tricksAnnounced = 0;
		_cardsPlayed = new ArrayList<>();
		
		List<Card> cards = new ArrayList<>(ALL_CARDS);
		Collections.shuffle(cards);
		
		int trumpIndex = _players.size() * _round;
		_trumpCard = trumpIndex < cards.size() ? cards.get(trumpIndex) : null;
		_trumpSuit = _trumpCard != null ? _trumpCard.getSuit() : null;
		
		LOG.info("Starting round with trump card: " + _trumpCard);
		
		int firstCard = 0;
		for (PlayerState player : _players) {
			RoundState state = RoundState.create();
			List<Card> playerCards = new ArrayList<>(cards.subList(firstCard, firstCard + _round));
			Collections.sort(playerCards, CardComparator.INSTANCE);
			state.getCards().addAll(playerCards);
			player.setRoundState(state);

			LOG.info("Giving cards to '" + player.getPlayer() + "': " + playerCards);

			sendStartRound(player);
			
			firstCard += _round;
		}
		
		if (isCustomTrumpSelection()) {
			_gameState = PlayingState.TRUMP_SELECTION;
			
			int trumpSelectIndex = _bidOffset + (_players.size() - 1);
			_trumpSelectorId = getPlayerId(trumpSelectIndex);
			
			LOG.info("Requesting trump selection from '" + playerState(trumpSelectIndex).getPlayer() + "'.");
			
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
		client(player.getPlayer().getId()).sendMessage(message);
	}

	private void requestNextBid()  {
		broadCast(createBidRequestMessage());
	}

	private RequestBid createBidRequestMessage() {
		return RequestBid.create().setPlayerId(getPlayerId(_bidOffset + _bidCount)).setRound(_round).setExpected(_tricksAnnounced);
	}

	private GameClient client(String playerId) {
		return _clientByPlayerId.get(playerId);
	}

	private String getPlayerId(int index) {
		return playerState(index).getPlayer().getId();
	}

	private PlayerState playerState(int index) {
		return _players.get(normalizeIndex(index));
	}

	private int normalizeIndex(int index) {
		return index % _players.size();
	}

	@Override
	public Void visit(SelectTrump self, GameClient sender)  {
		synchronized (this) {
			if (_trumpSuit != null) {
				sender.sendError(R.errTrumpAlreadySelected);
				return null;
			}
			if (!sender.getId().equals(_trumpSelectorId)) {
				sender.sendError(R.errYouCannotSelectTrump);
				return null;
			}
			if (self.getTrumpSuit() == null) {
				sender.sendError(R.errMustSelectTrump);
				return null;
			}
			if (!isCustomTrumpSelection()) {
				sender.sendError(R.errNoTrumpSelectionAllowed);
				return null;
			}
			
			_trumpSuit = self.getTrumpSuit();
		}
		
		forward(sender, self);
		startBids();
		return null;
	}

	private boolean isCustomTrumpSelection() {
		return _trumpCard != null && _trumpCard.getValue() == Value.Z;
	}

	private void startBids() {
		LOG.info("Requesting bids.");
		_gameState = PlayingState.BIDDING;
		
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
	public Void visit(Bid self, GameClient sender)  {
		boolean lastBid;
		synchronized (this) {
			boolean bidOpen = _bidCount >= _players.size();
			if (bidOpen) {
				sender.sendError(R.errAllBidsPlaced);
				return null;
			}
			PlayerState playerState = playerState(_bidOffset + _bidCount);
			if (!playerState.getPlayer().getId().equals(sender.getId())) {
				sender.sendError(R.errNotYourTurnToBid);
				return null;
			}
			int currentBid = self.getCnt();
			_tricksAnnounced += currentBid;
			playerState.getRoundState().setBidCnt(currentBid);
			self.setExpected(_tricksAnnounced);
			_bidCount++;
			lastBid = _bidCount >= _players.size();
		}
		
		forward(sender, self);
		
		if (lastBid) {
			broadCast(createStartLeadMessage());
			requestNextLead();
		} else {
			requestNextBid();
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
		int playerIndex = _startPlayerOffset;
		for (Card card : _cardsPlayed) {
			startLead.getCurrentTrick().add(PlayedCard.create().setCard(card).setPlayerId(getPlayerId(playerIndex)));
			playerIndex++;
		}
		
		return startLead;
	}

	@Override
	public Void visit(Lead self, GameClient sender)  {
		PlayerState activePlayer;
		synchronized (this) {
			if (_bidCount < _players.size()) {
				sender.sendError(R.errNotAllBidsPlaced);
				return null;
			}
			activePlayer = playerState(_startPlayerOffset + _cardsPlayed.size());
			if (!activePlayer.getPlayer().getId().equals(sender.getId())) {
				sender.sendError(R.errNotYourTurn);
				return null;
			}
			if (!_barrier.isEmpty()) {
				sender.sendError(R.errAllPlayersMustConfirm);
				return null;
			}
		}
		
		RoundState roundState = activePlayer.getRoundState();
		List<Card> deck = roundState.getCards();
		Card putCard = self.getCard();
		Optional<Card> searchResult = 
			deck.stream().filter(c -> c.getSuit() == putCard.getSuit() && c.getValue() == putCard.getValue()).findFirst();
		if (!searchResult.isPresent()) {
			sender.sendError(R.errWrongCard);
			return null;
		}
		Card playedCard = searchResult.get();
		
		Suit leadSuit = leadSuit(_cardsPlayed);
		if (leadSuit != null && playedCard.getSuit() != null && playedCard.getSuit() != leadSuit) {
			if (deck.stream().filter(c -> c.getSuit() == leadSuit).findFirst().isPresent()) {
				sender.sendError(R.errMustFollowSuit);
				return null;
			}
		}
		
		roundState.getCards().remove(playedCard);
		_cardsPlayed.add(playedCard);
		forward(sender, self);
		
		if (_cardsPlayed.size() < _players.size()) {
			requestNextLead();
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
	public Void visit(ConfirmTrick self, GameClient sender) throws IOException {
		boolean lastConfirmer;
		synchronized (this) {
			GameClient client = _barrier.remove(sender.getId());
			if (client == null) {
				sender.sendError(R.errAlreadyConfirmed);
				return null;
			}
			lastConfirmer = _barrier.isEmpty();
		}
		
		forward(sender, self);
		
		if (lastConfirmer) {
			_startPlayerOffset = _trickWinnerOffset;
			nextTurn();
		}
		return null;
	}

	private void nextTurn() {
		_cardsPlayed = new ArrayList<>();
		
		_turnsLeft--;
		if (_turnsLeft > 0) {
			requestNextLead();
		} else {
			// Compute points.
			
			FinishRound finishRound = createFinishRoundMessage();
			initBarrier(PlayingState.FINISHING_ROUND);
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
			
			// Apply delta to player state.
			state.setPoints(state.getPoints() + delta);
			
			finishRound.getInfo().put(state.getPlayer().getId(), 
				RoundInfo.create().setPoints(delta).setTotal(state.getPoints()));
		}
		return finishRound;
	}
	
	private void finishTurn() {
		int winningCardIndex = computeWinningCardIndex();
		_trickWinnerOffset = normalizeIndex(_startPlayerOffset + winningCardIndex);
		RoundState winnerRoundState = _players.get(_trickWinnerOffset).getRoundState();
		winnerRoundState.setWinCnt(winnerRoundState.getWinCnt() + 1);
		
		initBarrier(PlayingState.FINISHING_TURN);
		broadCast(createFinishTurnMessage());
	}

	private FinishTurn createFinishTurnMessage() {
		return FinishTurn.create().setTrick(_cardsPlayed).setWinner(_players.get(_trickWinnerOffset).getPlayer());
	}

	/** 
	 * Compute the index of the winning card in {@link #_cardsPlayed}.
	 */
	private int computeWinningCardIndex() {
		int winnerIndex = 0;
		Card best = _cardsPlayed.get(winnerIndex);
		for (int n = 1, cnt = _cardsPlayed.size(); n < cnt; n++) {
			Card current = _cardsPlayed.get(n);
			if (hits(best, current)) {
				winnerIndex = n;
				best = current;
			}
		}
		return winnerIndex;
	}

	@Override
	public Void visit(ConfirmRound self, GameClient sender) throws IOException {
		boolean lastConfirmer;
		synchronized (this) {
			GameClient client = _barrier.remove(sender.getId());
			if (client == null) {
				sender.sendError(R.errAlreadyConfirmed);
				return null;
			}
			lastConfirmer = _barrier.isEmpty();
		}
		
		forward(sender, self);
		
		if (lastConfirmer) {
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
		FinishGame finishGame = createFinishGameMessage();
		
		broadCast(finishGame);
		
		initBarrier(PlayingState.FINISHING_GAME);
	}

	private FinishGame createFinishGameMessage() {
		FinishGame finishGame = FinishGame.create();
		for (PlayerState state : _players) {
			int points = state.getPoints();
			finishGame.addScore(PlayerScore.create().setPlayer(state.getPlayer()).setPoints(points));
		}
		Collections.sort(finishGame.getScores(), (s1, s2) -> -Integer.compare(s1.getPoints(), s2.getPoints()));
		return finishGame;
	}
	
	@Override
	public Void visit(ConfirmGame self, GameClient sender) throws IOException {
		boolean lastConfirmer;
		synchronized (this) {
			GameClient client = _barrier.remove(sender.getId());
			if (client == null) {
				sender.sendError(R.errAlreadyConfirmed);
				return null;
			}
			lastConfirmer = _barrier.isEmpty();
		}
		
		forward(sender, self);
		
		if (lastConfirmer) {
			gameFinished();
		}
		return null;
	}

	/**
	 * Sends the given {@link Msg} to all idle players (not currently participating in an active game).
	 */
	private void broadCastAll(Msg msg) {
		_broadCastAll.accept(msg);
	}

	private void gameFinished() {
		if (_lifeCycle != LifeCycleState.FINISHED) {
			_lifeCycle = LifeCycleState.FINISHED;
			
			_onFinish.accept(getGameId());
		}
	}

	private void initBarrier(PlayingState nextState) {
		_gameState = nextState;
		_barrier.clear();
		_barrier.putAll(_clientByPlayerId);
	}
	
	private void requestNextLead()  {
		_gameState = PlayingState.LEADING;
		broadCast(createRequestLeadMessage());
	}

	private RequestLead createRequestLeadMessage() {
		return RequestLead.create().setPlayerId(getPlayerId(_startPlayerOffset + _cardsPlayed.size()));
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
