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
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.Suit;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.PlayerInfo;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.StartRound;
import de.haumacher.wizard.msg.Value;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class GameView extends Controller {
	
	@FXML
	AnchorPane actionPane;
	
	@FXML
	TilePane cardsPane;
	
	@FXML
	TilePane trumpPane;

	@FXML
	Text roundDisplay;
	
	@FXML
	VBox statusPane;

	private ProphecyPane _prophecy;

	private ClientHandler _handler;
	
	private Map<String, PlayerStatus> _playerStatus = new HashMap<>();

	private CurrentTrick _currentTrick;

	private Map<String, Player> _players = new LinkedHashMap<>();

	private String _playerId;

	private CardView _cardBeingPut;

	private boolean _trickFinished;

	private TrumpSelection _trumpSelection;

	public void init(ClientHandler handler, String playerId) {
		_handler = handler;
		_playerId = playerId;
	}

	public void startRound(StartRound msg) {
		trumpPane.getChildren().setAll(CardView.createCard(msg.getTrumpCard()));
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
		} else {
			// Reorder players for current turn.
			statusPane.getChildren().clear();
			for (Player player : players) {
				PlayerStatus playerStatus = _playerStatus.get(player.getId());
				statusPane.getChildren().add(playerStatus.getView());
				playerStatus.clearTricks();
			}
		}
		
		roundDisplay.setText("Runde " + msg.getRound() + " von " + msg.getMaxRound());
	}

	public void requestTrumpSelection(String playerId) {
		_trumpSelection = load(TrumpSelection.class, "TrumpSelection.fxml");
		_trumpSelection.init(_handler, _players.get(playerId), _playerId.equals(playerId));
	}

	public void selectTrump(Suit trumpSuit) {
		trumpPane.getChildren().setAll(CardView.createCard(Card.create().setSuit(trumpSuit).setValue(Value.Z)));
		_trumpSelection = null;
	}

	public void startBids() {
		_prophecy = load(ProphecyPane.class, "ProphecyPane.fxml");
		_prophecy.setPlayers(_handler, _players);
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
	}

	public void finishTurn(String winnerId) {
		_playerStatus.get(winnerId).incTricks();
		
		_currentTrick.confirm("Dieser Stich geht an " + name(winnerId) + ".", e -> {
			_handler.sendCommand(ConfirmTrick.create());
			_currentTrick.setInfo("Warte auf die Bestätigung der anderen Spieler.");
			_trickFinished = true;
		});
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
		
		_currentTrick.confirm("In dieser Runde erhälst Du " + points.get(_playerId) + " Punkte.", e -> {
			_handler.sendCommand(ConfirmRound.create());
			_currentTrick.setInfo("Warte auf die anderen Spieler...");
		});
	}

	private void clearTrick() {
		if (_currentTrick != null) {
			_currentTrick.clear();
		}
	}

	public ProphecyPane getProphecy() {
		return _prophecy;
	}

}
