/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.haumacher.wizard.ClientHandler;
import de.haumacher.wizard.WizardApp;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.PlayerInfo;
import de.haumacher.wizard.msg.RequestBid;
import de.haumacher.wizard.msg.StartRound;
import de.haumacher.wizard.msg.Suit;
import de.haumacher.wizard.msg.Value;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Main view while a trick is played.
 */
public class GameView extends Controller {
	
	/**
	 * Area showing the currently played cards (the current trick).
	 */
	@FXML
	Pane actionPane;
	
	/**
	 * Area showning the players hand.
	 */
	@FXML
	Pane cardsPane;
	
	/**
	 * Area showing the trump card.
	 */
	@FXML
	Pane trumpPane;

	/**
	 * Text field showing the current round information (round x of y).
	 */
	@FXML
	Text roundDisplay;
	
	/**
	 * Text field showing the total number of tricks expected by players.
	 */
	@FXML
	Text prophecySum;
	
	/**
	 * Area, where {@link PlayerStatus} views are shown for each player.
	 */
	@FXML
	Pane statusPane;

	private ProphecyPane _prophecy;

	private ClientHandler _handler;
	
	private Map<String, PlayerStatus> _playerStatus = new HashMap<>();

	private CurrentTrick _currentTrick;

	private Map<String, Player> _players = new LinkedHashMap<>();

	private String _playerId;

	private CardView _cardBeingPut;

	private boolean _trickFinished;

	private TrumpSelection _trumpSelection;

	private Map<String, PlayerStatus> _activePlayers = new HashMap<>();

	public void init(ClientHandler handler, String playerId) {
		_handler = handler;
		_playerId = playerId;
	}

	public void startRound(StartRound msg) {
		setProphecySum(0);
		Card trumpCard = msg.getTrumpCard();
		if (trumpCard == null) {
			trumpPane.getChildren().clear();
		} else {
			trumpPane.getChildren().setAll(CardView.createCard(trumpCard));
		}
		cardsPane.getChildren().setAll(msg.getCards().stream().map(CardView::createCard).collect(Collectors.toList()));
		
		List<Player> players = msg.getPlayers();
		if (_playerStatus.isEmpty()) {
			for (Player player : players) {
				_players.put(player.getId(), player);
				
				Node statusView = WizardApp.load(PlayerStatus.class, "PlayerStatus.fxml");
				PlayerStatus status = (PlayerStatus) statusView.getUserData();
				status.setPlayer(player);
				statusPane.getChildren().add(statusView);
				
				_playerStatus.put(player.getId(), status);
			}
		}

		for (PlayerStatus status : _playerStatus.values()) {
			status.clearTricks();
			status.markStartPlayer(status.getPlayer().getId().equals(msg.getStartPlayer()));
		}
		
		roundDisplay.setText("Runde " + msg.getRound() + " von " + msg.getMaxRound());
	}

	public void requestTrumpSelection(String playerId) {
		_trumpSelection = load(TrumpSelection.class, "TrumpSelection.fxml");
		_trumpSelection.init(_handler, _players.get(playerId), _playerId.equals(playerId));
		setActive(playerId);
	}

	public void selectTrump(Suit trumpSuit) {
		trumpPane.getChildren().setAll(CardView.createCard(Card.create().setSuit(trumpSuit).setValue(Value.Z)));
		_trumpSelection = null;
		clearActive();
	}

	public void startBids() {
		_prophecy = load(ProphecyPane.class, "ProphecyPane.fxml");
		_prophecy.setPlayers(_handler, _players);
	}

	public void requestBid(String playerId, RequestBid self) {
		_prophecy.requestBid(_players.get(playerId), playerId.equals(_playerId), self.getRound(), self.getRound() - self.getExpected());
		setActive(playerId);
	}

	public void setBid(String playerId, Bid self) {
		setProphecySum(self.getExpected());
		_playerStatus.get(playerId).setBid(self.getCnt());
		removeActive(playerId);
	}

	private void setProphecySum(int self) {
		prophecySum.setText(self + " Stiche vorhergesagt");
	}

	public void startLead(Map<String, PlayerInfo> state) {
		for (Entry<String, PlayerInfo> entry : state.entrySet()) {
			PlayerStatus playerStatus = _playerStatus.get(entry.getKey());
			PlayerInfo info = entry.getValue();
			
			playerStatus.setBid(info.getBid());
			playerStatus.setScore(info.getPoints());
		}
		
		_currentTrick = load(CurrentTrick.class, "CurrentTrick.fxml");
		_currentTrick.init(_handler);
	}

	public void requestLead(String playerId) {
		if (_trickFinished) {
			clearTrick();
			_trickFinished = false;
		}
		
		if (_playerId.equals(playerId)) {
			_currentTrick.setInfo("Du bist am Zug!");
		} else {
			_currentTrick.setInfo("Spieler " + _players.get(playerId).getName() + " ist am Zug!");
		}
		
		cardsPane.getChildren().forEach(n -> n.setOnMouseClicked(e -> leadCard(n)));
		setActive(playerId);
	}

	private void leadCard(Node n) {
		_cardBeingPut = (CardView) n.getUserData();
		_handler.sendCommand(Lead.create().setCard(_cardBeingPut.getCard()));
	}

	public void announceLead(String playerId, Card card) {
		if (playerId.endsWith(_playerId)) {
			cardsPane.getChildren().remove(_cardBeingPut.getView());
			cardsPane.getChildren().forEach(n -> n.setOnMouseClicked(null));
			_cardBeingPut = null;
		}
		_currentTrick.addCard(card);
		removeActive(playerId);
	}

	public void finishTurn(String winnerId) {
		_playerStatus.get(winnerId).incTricks();
		setActiveAll();
		
		_currentTrick.confirm("Dieser Stich geht an " + name(winnerId) + ".", e -> {
			_handler.sendCommand(ConfirmTrick.create());
			_currentTrick.setInfo("Warte auf die Bestätigung der anderen Spieler.");
			_trickFinished = true;
		});
	}

	public void confirmTrick(String playerId) {
		removeActive(playerId);
	}

	private String name(String playerId) {
		return playerId.equals(_playerId) ? "Dich" : _players.get(playerId).getName();
	}

	public void finishRound(Map<String, Integer> points) {
		clearTrick();
		trumpPane.getChildren().clear();
		
		for (Entry<String, Integer> entry : points.entrySet()) {
			_playerStatus.get(entry.getKey()).addScore(entry.getValue().intValue());
		}
		
		setActiveAll();
		
		_currentTrick.confirm("In dieser Runde erhälst Du " + points.get(_playerId) + " Punkte.", e -> {
			_handler.sendCommand(ConfirmRound.create());
			_currentTrick.setInfo("Warte auf die anderen Spieler...");
		});
	}

	public void confirmRound(String playerId) {
		removeActive(playerId);
	}

	private void clearTrick() {
		if (_currentTrick != null) {
			_currentTrick.clear();
		}
	}

	public ProphecyPane getProphecy() {
		return _prophecy;
	}

	private void setActiveAll() {
		for (Entry<String, PlayerStatus> entry : _playerStatus.entrySet()) {
			PlayerStatus status = entry.getValue();
			status.setActive(true);
			_activePlayers.put(entry.getKey(), status);
		}
	}

	private void setActive(String playerId) {
		clearActive();
		addActive(playerId);
	}

	private void clearActive() {
		for (PlayerStatus status : _activePlayers.values()) {
			status.setActive(false);
		}
		_activePlayers.clear();
	}

	private void addActive(String playerId) {
		PlayerStatus state = _playerStatus.get(playerId);
		state.setActive(true);
		_activePlayers.put(playerId, state);
	}

	private void removeActive(String playerId) {
		PlayerStatus state = _activePlayers.remove(playerId);
		if (state != null) {
			state.setActive(false);
		}
	}

	private <T extends Controller> T load(Class<T> controllerClass, String resourceFxml) {
		Node view = WizardApp.load(controllerClass, resourceFxml);
		actionPane.getChildren().setAll(view);
		AnchorPane.setTopAnchor(view, 0.0);
		AnchorPane.setLeftAnchor(view, 0.0);
		AnchorPane.setRightAnchor(view, 0.0);
		AnchorPane.setBottomAnchor(view, 0.0);
		@SuppressWarnings("unchecked")
		T controller = (T) view.getUserData();
		return controller;
	}

}
