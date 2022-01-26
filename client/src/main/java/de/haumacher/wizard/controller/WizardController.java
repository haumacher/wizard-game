/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.controller;

import java.io.IOException;
import java.util.function.Consumer;

import de.haumacher.wizard.R;
import de.haumacher.wizard.io.WizardConnection;
import de.haumacher.wizard.msg.Announce;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.Error;
import de.haumacher.wizard.msg.FinishGame;
import de.haumacher.wizard.msg.FinishRound;
import de.haumacher.wizard.msg.FinishTurn;
import de.haumacher.wizard.msg.Game;
import de.haumacher.wizard.msg.GameCmd;
import de.haumacher.wizard.msg.GameCreated;
import de.haumacher.wizard.msg.GameDeleted;
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.JoinAnnounce;
import de.haumacher.wizard.msg.JoinGame;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.LeaveAnnounce;
import de.haumacher.wizard.msg.LeaveGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.ListGamesResult;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.RequestBid;
import de.haumacher.wizard.msg.RequestLead;
import de.haumacher.wizard.msg.RequestTrumpSelection;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartBids;
import de.haumacher.wizard.msg.StartLead;
import de.haumacher.wizard.msg.StartRound;
import de.haumacher.wizard.msg.Welcome;
import de.haumacher.wizard.ui.GameLobby;
import de.haumacher.wizard.ui.GameSelector;
import de.haumacher.wizard.ui.GameView;
import de.haumacher.wizard.ui.RankingView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Central controller of the Wizard UI showing views for specific phases of the game.
 */
public class WizardController implements Consumer<Msg>, Msg.Visitor<Void, Void, IOException>, GameCmd.Visitor<Void, String, IOException> {

	private String _playerId;
	private GameSelector _gameSelector;
	private Game _game;
	private GameLobby _lobby;
	private GameView _gameView;
	private WizardConnection _server;
	private WizardUI _ui;

	/** 
	 * Creates a {@link WizardController}.
	 */
	public WizardController(WizardConnection server, WizardUI ui) {
		connectTo(server, ui);
	}

	public void connectTo(WizardConnection server, WizardUI ui) {
		_server = server;
		_ui = ui;
	}
	
	/**
	 * The assigned player ID.
	 */
	public String getPlayerId() {
		return _playerId;
	}
	
	/**
	 * The game that has been joined.
	 */
	public Game getGame() {
		return _game;
	}

	@Override
	public void accept(Msg t) {
		Platform.runLater(() ->  {
			try {
				t.visit(this, null);
			} catch (IOException ex) {
				new Alert(AlertType.ERROR, R.communicationError_msg.format(ex.getMessage()), ButtonType.OK).show();
			}
		});
	}
	
	@Override
	public Void visit(Welcome self, Void arg) throws IOException {
		_playerId = self.getPlayerId();
		_ui.onLogin();
		return null;
	}

	@Override
	public Void visit(Error self, Void arg) throws IOException {
		new Alert(AlertType.ERROR, self.getMessage(), ButtonType.OK).show();
		return null;
	}

	@Override
	public Void visit(ListGamesResult self, Void arg) throws IOException {
		_gameSelector = _ui.showView(GameSelector.class);
		_gameSelector.init(self, game -> {
			_game = game;
			_server.sendCommand(JoinGame.create().setGameId(game.getGameId()));
		});
		return null;
	}

	@Override
	public Void visit(JoinAnnounce self, Void arg) throws IOException {
		if (_game != null && _game.getGameId().equals(self.getGameId())) {
			_game.addPlayer(self.getPlayer());
		}
		
		if (self.getPlayer().getId().equals(_playerId)) {
			_lobby = _ui.showView(GameLobby.class);
			_lobby.initController(_server, _game.getGameId());
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
			_gameView = _ui.showView(GameView.class);
			_gameView.init(_server, _playerId);
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
		_gameView.requestBid(self.getPlayerId(), self);
		return null;
	}

	@Override
	public Void visit(Bid self, String playerId) throws IOException {
		_gameView.setBid(playerId, self);
		return null;
	}

	@Override
	public Void visit(StartLead self, Void arg) throws IOException {
		_gameView.startLead(self);
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
	public Void visit(ConfirmTrick self, String playerId) throws IOException {
		_gameView.confirmTrick(playerId);
		return null;
	}

	@Override
	public Void visit(FinishRound self, Void arg) throws IOException {
		_gameView.finishRound(self.getPoints());
		return null;
	}

	@Override
	public Void visit(ConfirmRound self, String playerId) throws IOException {
		_gameView.confirmRound(playerId);
		return null;
	}

	@Override
	public Void visit(FinishGame self, Void arg) throws IOException {
		RankingView ranking = _ui.showView(RankingView.class);
		ranking.show(_server, self, e -> {
			_server.sendCommand(LeaveGame.create().setGameId(_game.getGameId()));
			_server.sendCommand(ListGames.create());
		});
		return null;
	}

	@Override
	public Void visit(Announce self, Void arg) throws IOException {
		self.getCmd().visit(this, self.getPlayerId());
		return null;
	}


}
