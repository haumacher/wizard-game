/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.haumacher.wizard.R;
import de.haumacher.wizard.controller.Controller;
import de.haumacher.wizard.controller.GenericController;
import de.haumacher.wizard.io.WizardConnection;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.PlayedCard;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.PlayerInfo;
import de.haumacher.wizard.msg.RequestBid;
import de.haumacher.wizard.msg.RoundInfo;
import de.haumacher.wizard.msg.StartLead;
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
public class GameView extends GenericController {
	
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

	private WizardConnection _server;
	
	private Map<String, PlayerStatus> _playerStatus = new HashMap<>();

	private CurrentTrick _currentTrick;

	private Map<String, Player> _players = new LinkedHashMap<>();

	private String _playerId;

	private CardView _cardBeingPut;

	private boolean _trickFinished;

	private TrumpSelection _trumpSelection;

	private Map<String, PlayerStatus> _activePlayers = new HashMap<>();

	public void init(WizardConnection server, String playerId) {
		_server = server;
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
				
				PlayerStatus status = Controller.load(PlayerStatus.class, "PlayerStatus.fxml");
				status.setPlayer(player);
				statusPane.getChildren().add(status.getView());
				
				_playerStatus.put(player.getId(), status);
			}
		}

		for (PlayerStatus status : _playerStatus.values()) {
			status.clearTricks();
			status.markStartPlayer(status.getPlayer().getId().equals(msg.getStartPlayer()));
		}
		
		roundDisplay.setText(R.round_round_maxRound.format(msg.getRound(), msg.getMaxRound()));
	}

	public void requestTrumpSelection(String playerId) {
		_trumpSelection = showView(TrumpSelection.class, "TrumpSelection.fxml");
		_trumpSelection.init(_server, _players.get(playerId), _playerId.equals(playerId));
		setActive(playerId);
	}

	public void selectTrump(Suit trumpSuit) {
		trumpPane.getChildren().setAll(CardView.createCard(Card.create().setSuit(trumpSuit).setValue(Value.Z)));
		_trumpSelection = null;
		clearActive();
	}

	public void startBids() {
		_prophecy = showView(ProphecyPane.class, "ProphecyPane.fxml");
		_prophecy.setPlayers(_server, _players);
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
		prophecySum.setText(R.tricksAnnounced_tricks.format(self));
	}

	public void startLead(StartLead msg) {
		for (Entry<String, PlayerInfo> entry : msg.getState().entrySet()) {
			PlayerStatus playerStatus = _playerStatus.get(entry.getKey());
			PlayerInfo info = entry.getValue();
			
			playerStatus.setBid(info.getBid());
			playerStatus.setScore(info.getPoints());
		}
		
		_currentTrick = showView(CurrentTrick.class, "CurrentTrick.fxml");
		_currentTrick.init(_server);
		
		// Restore the current trick - only relevant when reconnecting to a game.
		for (PlayedCard playedCard : msg.getCurrentTrick()) {
			announceLead(_playerId, playedCard.getCard());
		}
	}

	public void requestLead(String playerId) {
		if (_trickFinished) {
			clearTrick();
			_trickFinished = false;
		}
		
		if (_playerId.equals(playerId)) {
			_currentTrick.setInfo(R.yourTurn);
		} else {
			_currentTrick.setInfo(R.playersTurn_player.format(_players.get(playerId).getName()));
		}
		
		cardsPane.getChildren().forEach(n -> n.setOnMouseClicked(e -> leadCard(n)));
		setActive(playerId);
	}

	private void leadCard(Node n) {
		_cardBeingPut = (CardView) n.getUserData();
		_server.sendCommand(Lead.create().setCard(_cardBeingPut.getCard()));
	}

	public void announceLead(String playerId, Card card) {
		// Note: During replay, this method is invoked without a pending card.
		if (playerId.endsWith(_playerId) && _cardBeingPut != null) {
			cardsPane.getChildren().remove(_cardBeingPut.getView());
			cardsPane.getChildren().forEach(n -> n.setOnMouseClicked(null));
			_cardBeingPut = null;
		}
		_currentTrick.addCard(card, _players.get(playerId).getName());
		removeActive(playerId);
	}

	public void finishTurn(String winnerId) {
		_trickFinished = true;
		_playerStatus.get(winnerId).incTricks();
		setActiveAll();
		
		String message = winnerId.equals(_playerId) ? 
			R.trickWonByYou : R.trickWonBy_player.format(name(winnerId));
		
		_currentTrick.confirm(message, e -> {
			_server.sendCommand(ConfirmTrick.create());
			_currentTrick.setInfo(R.waitingForConfirm);
		});
	}

	public void confirmTrick(String playerId) {
		removeActive(playerId);
	}

	private String name(String playerId) {
		return _players.get(playerId).getName();
	}

	public void finishRound(Map<String, RoundInfo> info) {
		clearTrick();
		trumpPane.getChildren().clear();
		
		for (Entry<String, RoundInfo> entry : info.entrySet()) {
			_playerStatus.get(entry.getKey()).setScore(entry.getValue().getTotal());
		}
		
		setActiveAll();
		
		_currentTrick.confirm(MessageFormat.format(R.youEarn_points, info.get(_playerId).getPoints()), e -> {
			_server.sendCommand(ConfirmRound.create());
			_currentTrick.setInfo(R.waitingForOthers);
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

	private <T extends Controller> T showView(Class<T> controllerClass, String resourceFxml) {
		T controller = Controller.load(controllerClass, resourceFxml);
		
		Node view = controller.getView();
		actionPane.getChildren().setAll(view);
		AnchorPane.setTopAnchor(view, 0.0);
		AnchorPane.setLeftAnchor(view, 0.0);
		AnchorPane.setRightAnchor(view, 0.0);
		AnchorPane.setBottomAnchor(view, 0.0);
		
		return controller;
	}

}
