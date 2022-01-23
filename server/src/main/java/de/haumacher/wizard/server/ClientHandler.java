/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.haumacher.wizard.logic.ClientConnection;
import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.CreateGame;
import de.haumacher.wizard.msg.GameCmd;
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.JoinGame;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.LeaveGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.ListGamesResult;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Reconnect;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartGame;
import de.haumacher.wizard.msg.Welcome;

/**
 * Server-side logic for a single player.
 */
public class ClientHandler implements Cmd.Visitor<Void, Void, IOException>, ClientConnection {

	private final WizardServer _server;
	
	private WizardGame _game;
	
	private boolean _loggedIn = false;

	private Consumer<Msg> _msgSink;

	private GameClient _handle;
	
	/** 
	 * Creates a {@link ClientHandler}.
	 */
	public ClientHandler(WizardServer server, Consumer<Msg> msgSink) {
		_server = server;
		_msgSink = msgSink;
		
		_handle = new GameClientImpl(this);
	}

	public String getId() {
		return _handle.getId();
	}
	
	public String getName() {
		return _handle.getName();
	}
	
	@Override
	public String toString() {
		return _handle.toString();
	}

	/** 
	 * Starts this handler.
	 * 
	 * <p>
	 * Method is called directly after a client opened a connection to the game server.
	 * </p>
	 */
	public void start() {
		_server.addClient(this);
	}

	/** 
	 * Called whenever the client connection to the game server has terminated.
	 */
	public void stop() {
		_server.removeClient(this);
		if (_game != null) {
			_game.removePlayer(_handle);
		}
	}

	/** 
	 * Processes the given {@link Cmd} received from the client-side.
	 */
	public void handleCmd(Cmd cmd) throws IOException {
		if (cmd != null) {
			System.out.println(_handle.getName() + " -> " + cmd);
			cmd.visit(this, null);
		} else {
			System.err.println("Received unknown command from '" + _handle + "'.");
		}
	}

	@Override
	public Void visit(Login self, Void arg) throws IOException {
		if (self.getVersion() != WizardGame.PROTOCOL_VERSION) {
			if (self.getVersion() > WizardGame.PROTOCOL_VERSION) {
				sendError("Die Version deines Programms ist zu neu. Sie ist nicht mit diesem Server kompatibel.");
			} else {
				sendError("Die Version deines Programms ist zu alt. Sie ist nicht mit diesem Server kompatibel.");
			}
			return null;
		}
		_loggedIn = true;
		_handle.getData().setName(self.getName());
		sendMessage(Welcome.create().setPlayerId(getId()));
		return null;
	}
	
	@Override
	public Void visit(Reconnect self, Void arg) throws IOException {
		if (_game != null) {
			sendErrorHasGame();
			return null;
		}
		
		WizardGame game = _server.getGame(self.getGameId());
		if (game == null) {
			sendErrorNoSuchGame();
			return null;
		}
		
		GameClient handle = game.reconnect(self.getPlayerId(), this);
		if (handle == null) {
			sendError("Spieler zur Wiederaufnahme nicht gefunden.");
			return null;
		}
		
		_loggedIn = true;
		_game = game;
		_handle = handle;
		
		return null;
	}

	@Override
	public Void visit(CreateGame self, Void arg) throws IOException {
		if (_game != null) {
			sendErrorHasGame();
			return null;
		}
		
		_game = _server.createGame(_handle);
		_game.addPlayer(_handle);
		
		return null;
	}

	private void sendErrorHasGame() {
		sendError("Du bist schon einem Spiel beigetreten.");
	}

	private void sendErrorNoSuchGame() {
		sendError("Das von Dir gewählte Spiel gibt es nicht mehr.");
	}

	@Override
	public void sendMessage(Msg msg) {
		_msgSink.accept(msg);
	}

	@Override
	public Void visit(StartGame self, Void arg) throws IOException {
		if (_game == null) {
			sendError("Du bist keinem Spiel beigetreten.");
			return null;
		}
		if (!_game.getGameId().equals(self.getGameId())) {
			sendError("Du kannst nur das Spiel starten, dem Du beigetreten bist.");
			return null;
		}
		
		_server.broadCast(GameStarted.create().setGame(_game.getData()));
		
		// Note: This starts the first round and must therefore happen after the start announce.
		_game.start();
		
		return null;
	}

	@Override
	public Void visit(JoinGame self, Void arg) throws IOException {
		if (_game != null) {
			sendErrorHasGame();
			return null;
		}
		_game = _server.getGame(self.getGameId());
		if (_game == null) {
			sendErrorNoSuchGame();
			return null;
		}
		if (!_game.addPlayer(_handle)) {
			sendError("Das von Dir gewählte Spiel hat schon begonnen.");
			return null;
		}
		return null;
	}

	@Override
	public Void visit(LeaveGame self, Void arg) throws IOException {
		if (_game != null && _game.getGameId().equals(self.getGameId())) {
			_game.removePlayer(_handle);
			_game = null;
		}
		return null;
	}

	@Override
	public Void visit(ListGames self, Void arg) throws IOException {
		sendMessage(
			ListGamesResult.create()
				.setGames(_server.getWaitingGames().stream().map(WizardGame::getData).collect(Collectors.toList())));
		return null;
	}

	@Override
	public Void visit(SelectTrump self, Void arg) throws IOException {
		return forwardToGame(self);
	}

	@Override
	public Void visit(Bid self, Void arg) throws IOException {
		return forwardToGame(self);
	}

	@Override
	public Void visit(Lead self, Void arg) throws IOException {
		return forwardToGame(self);
	}
	
	@Override
	public Void visit(ConfirmTrick self, Void arg) throws IOException {
		return forwardToGame(self);
	}
	
	@Override
	public Void visit(ConfirmRound self, Void arg) throws IOException {
		return forwardToGame(self);
	}

	private Void forwardToGame(GameCmd self) throws IOException {
		if (!_loggedIn) {
			sendError("Du bist nicht angemeldet.");
			return null;
		}
		if (_game == null) {
			sendError("Du bist keinem Spiel beigetreten.");
			return null;
		}

		return self.visit(_game, _handle);
	}

}
