/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.io.IOError;
import java.io.IOException;

import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.Announce;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.CreateGame;
import de.haumacher.wizard.msg.Error;
import de.haumacher.wizard.msg.FinishGame;
import de.haumacher.wizard.msg.FinishRound;
import de.haumacher.wizard.msg.FinishTurn;
import de.haumacher.wizard.msg.Game;
import de.haumacher.wizard.msg.GameCmd;
import de.haumacher.wizard.msg.GameCreated;
import de.haumacher.wizard.msg.GameDeleted;
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.JoinGame;
import de.haumacher.wizard.msg.JoinAnnounce;
import de.haumacher.wizard.msg.LeaveAnnounce;
import de.haumacher.wizard.msg.LeaveGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.ListGamesResult;
import de.haumacher.wizard.msg.LoggedIn;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.RequestBid;
import de.haumacher.wizard.msg.RequestLead;
import de.haumacher.wizard.msg.RequestTrumpSelection;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartBids;
import de.haumacher.wizard.msg.StartLead;
import de.haumacher.wizard.msg.StartRound;
import de.haumacher.wizard.ui.ConnectDialog;
import de.haumacher.wizard.ui.GameLobby;
import de.haumacher.wizard.ui.GameSelector;
import de.haumacher.wizard.ui.GameView;
import de.haumacher.wizard.ui.RankingView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Client {@link Application} providing the user interface for the Wizard game.
 */
public class WizardApp extends Application implements Msg.Visitor<Void, Void, IOException>, GameCmd.Visitor<Void, String, IOException> {
	
	private Scene _scene;
	ClientHandler _handler;
	
	@FXML
	AnchorPane main;
	private GameSelector _gameSelector;
	private String _playerId;
	private Game _game;
	private GameLobby _lobby;
	private GameView _gameView;

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WizardApp.fxml"));
		loader.setController(this);
		_scene = loader.load();
		
		stage.setScene(_scene);
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		if (_handler != null) {
			try {
				_handler.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		super.stop();
	}
	
	public void menuConnect(Event evt) {
		ConnectDialog dialog = (ConnectDialog) ((Stage) load(ConnectDialog.class, "ConnectDialog.fxml")).getUserData();
		dialog.show(data -> {
			try {
				if (_handler != null) {
					_handler.close();
					_handler = null;
				}
				
				_handler = new ClientHandler() {
					@Override
					protected void onMessage(Msg msg) throws IOException {
						Platform.runLater(() ->  {
							try {
								msg.visit(WizardApp.this, null);
							} catch (IOException ex) {
								new Alert(AlertType.ERROR, "Fehler bei der Kommunikation: " + ex.getMessage(), ButtonType.OK).show();
							}
						});
					}
				};
				_handler.start(data.getServerAddr(), 8090);
				_handler.sendCommand(Login.create().setName(data.getNickName()).setVersion(WizardGame.PROTOCOL_VERSION));
				_handler.sendCommand(ListGames.create());
			} catch (IOException ex) {
				_handler = null;
				showError(ex);
			}
		});
	}
	
	@Override
	public Void visit(LoggedIn self, Void arg) throws IOException {
		_playerId = self.getPlayerId();
		return null;
	}

	private void showError(IOException ex) {
		new Alert(AlertType.ERROR, "Kommunikation nicht möglich: " + ex.getMessage(), ButtonType.CLOSE).show();
	}

	public void newGame(Event evt) {
		_handler.sendCommand(CreateGame.create());
	}

	private <T> T showView(Class<T> controllerType, String resourceName) {
		Node view = load(controllerType, resourceName);
		main.getChildren().setAll(view);
		
		AnchorPane.setTopAnchor(view, 0.0);
		AnchorPane.setLeftAnchor(view, 0.0);
		AnchorPane.setRightAnchor(view, 0.0);
		AnchorPane.setBottomAnchor(view, 0.0);
		@SuppressWarnings("unchecked")
		T controller = (T) view.getUserData();
		return controller;
	}

	public static <T> T load(Class<?> controllerClass, String resource)  {
		try {
			return FXMLLoader.load(controllerClass.getResource(resource));
		} catch (IOException ex) {
			throw new IOError(ex);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public Void visit(Error self, Void arg) throws IOException {
		new Alert(AlertType.ERROR, self.getMessage(), ButtonType.OK).show();
		return null;
	}

	@Override
	public Void visit(ListGamesResult self, Void arg) throws IOException {
		_gameSelector = showView(GameSelector.class, "GameSelector.fxml");
		_gameSelector.init(self, game -> {
			_game = game;
			_handler.sendCommand(JoinGame.create().setGameId(game.getGameId()));
		});
		return null;
	}

	@Override
	public Void visit(JoinAnnounce self, Void arg) throws IOException {
		if (_game != null && _game.getGameId().equals(self.getGameId())) {
			_game.addPlayer(self.getPlayer());
		}
		
		if (self.getPlayer().getId().equals(_playerId)) {
			_lobby = showView(GameLobby.class, "GameLobby.fxml");
			_lobby.initController(_handler, _game.getGameId());
			_lobby.setPlayers(_game.getPlayers());
		} else {
			if (_gameSelector != null) {
				_gameSelector.processJoin(self);
			}
			if (_lobby != null && _game.getGameId().equals(self.getGameId())) {
				_lobby.addPlayer(self.getPlayer());
			}
		}
		return null;
	}
	
	@Override
	public Void visit(LeaveAnnounce self, Void arg) throws IOException {
		if (_gameSelector != null) {
			_gameSelector.processLeave(self);
		}
		return null;
	}
	
	@Override
	public Void visit(GameCreated self, Void arg) throws IOException {
		if (_playerId != null && _playerId.equals(self.getOwnerId())) {
			// This player has started the game an is therefore implicitly player of the game.
			_game = self.getGame();
		}
		if (_gameSelector != null) {
			_gameSelector.processCreate(self);
		}
		return null;
	}
	
	@Override
	public Void visit(GameDeleted self, Void arg) throws IOException {
		if (_gameSelector != null) {
			_gameSelector.removeGame(self.getGameId());
		}
		return null;
	}

	@Override
	public Void visit(GameStarted self, Void arg) throws IOException {
		if (_gameSelector != null) {
			_gameSelector.removeGame(self.getGame().getGameId());
		}
		if (_lobby != null && _game.getGameId().equals(self.getGame().getGameId())) {
			_gameView = showView(GameView.class, "GameView.fxml");
			_gameView.init(_handler, _playerId);
		}
		return null;
	}
	
	@Override
	public Void visit(StartRound self, Void arg) throws IOException {
		_gameView.startRound(self);
		return null;
	}

	@Override
	public Void visit(RequestTrumpSelection self, Void arg) throws IOException {
		_gameView.requestTrumpSelection(self.getPlayerId());
		return null;
	}

	@Override
	public Void visit(SelectTrump self, String playerId) throws IOException {
		_gameView.selectTrump(self.getTrumpSuit());
		return null;
	}
	
	@Override
	public Void visit(StartBids self, Void arg) throws IOException {
		_gameView.startBids();
		return null;
	}

	@Override
	public Void visit(RequestBid self, Void arg) throws IOException {
		_gameView.getProphecy().requestBid(_playerId, self.getRound(), self.getExpected());
		return null;
	}

	@Override
	public Void visit(Bid self, String playerId) throws IOException {
		_gameView.getProphecy().setBid(playerId, self.getCnt(), self.getExpected());
		return null;
	}

	@Override
	public Void visit(StartLead self, Void arg) throws IOException {
		_gameView.startLead(self.getState());
		return null;
	}

	@Override
	public Void visit(RequestLead self, Void arg) throws IOException {
		_gameView.requestLead(self.getPlayerId());
		return null;
	}

	@Override
	public Void visit(Lead self, String playerId) throws IOException {
		_gameView.announceLead(playerId, self.getCard());
		return null;
	}

	@Override
	public Void visit(FinishTurn self, Void arg) throws IOException {
		_gameView.finishTurn(self.getWinner().getId());
		return null;
	}

	@Override
	public Void visit(ConfirmTrick self, String arg) throws IOException {
		// Not forwarded to other players.
		return null;
	}

	@Override
	public Void visit(FinishRound self, Void arg) throws IOException {
		_gameView.finishRound(self.getPoints());
		return null;
	}

	@Override
	public Void visit(ConfirmRound self, String arg) throws IOException {
		// Not forwarded to other players.
		return null;
	}

	@Override
	public Void visit(FinishGame self, Void arg) throws IOException {
		RankingView ranking = showView(RankingView.class, "RankingView.fxml");
		ranking.show(_handler, self, e -> {
			_handler.sendCommand(LeaveGame.create().setGameId(_game.getGameId()));
			_handler.sendCommand(ListGames.create());
		});
		return null;
	}

	@Override
	public Void visit(Announce self, Void arg) throws IOException {
		self.getCmd().visit(this, self.getPlayerId());
		return null;
	}
	
}
