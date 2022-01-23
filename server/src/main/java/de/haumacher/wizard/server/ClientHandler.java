/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.CreateGame;
import de.haumacher.wizard.msg.Error;
import de.haumacher.wizard.msg.GameCmd;
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.JoinGame;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.LeaveGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.ListGamesResult;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.Reconnect;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartGame;
import de.haumacher.wizard.msg.Welcome;

/**
 * Server-side logic for a single player.
 */
public class ClientHandler implements Cmd.Visitor<Void, Void, IOException>, GameClient {

	private final WizardServer _server;
	
	private WizardGame _game;
	
	private Player _data = Player.create().setId(UUID.randomUUID().toString()).setName("Anonymous");
	private boolean _loggedIn = false;

	private Consumer<Msg> _msgSink;

	/** 
	 * Creates a {@link ClientHandler}.
	 */
	public ClientHandler(WizardServer server, Consumer<Msg> msgSink) {
		_server = server;
		_msgSink = msgSink;
	}

	/** 
	 * Processes the given {@link Cmd} received from the client-side.
	 */
	public void handleCmd(Cmd cmd) throws IOException {
		if (cmd != null) {
			System.out.println(_data.getName() + " -> " + cmd);
			cmd.visit(this, null);
		} else {
			System.err.println("Received unknown command from '" + _data + "'.");
		}
	}

	@Override
	public Player getData() {
		return _data;
	}
	
	@Override
	public String toString() {
		return getName() + "(" + getId() + ")";
	}

	@Override
	public void sendError(String message) {
		sendMessage(Error.create().setMessage(message));
	}
	
	@Override
	public synchronized void sendMessage(Msg msg) {
		_msgSink.accept(msg);
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
		_data.setName(self.getName());
		sendMessage(Welcome.create().setPlayerId(getId()));
		return null;
	}
	
	@Override
	public Void visit(Reconnect self, Void arg) throws IOException {
		//  TODO: Automatically created
		return null;
	}

	@Override
	public Void visit(CreateGame self, Void arg) throws IOException {
		if (_game != null) {
			sendError("Du bist schon einem Spiel beigetreten.");
			return null;
		}
		
		_game = _server.createGame(this);
		_game.addPlayer(this);
		
		return null;
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
			sendError("Du bist schon einem Spiel beigetreten.");
			return null;
		}
		_game = _server.getGame(self.getGameId());
		if (_game == null) {
			sendError("Das von Dir gewählte Spiel gibt es nicht mehr.");
			return null;
		}
		if (!_game.addPlayer(this)) {
			sendError("Das von Dir gewählte Spiel hat schon begonnen.");
			return null;
		}
		return null;
	}
	
	@Override
	public Void visit(LeaveGame self, Void arg) throws IOException {
		if (_game != null && _game.getGameId().equals(self.getGameId())) {
			_game.removePlayer(this);
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

		return self.visit(_game, this);
	}

	/** 
	 * TODO
	 *
	 */
	public void start() {
		_server.addClient(this);
	}

	/** 
	 * TODO
	 *
	 */
	public void stop() {
		_server.removeClient(this);
		if (_game != null) {
			_server.removePlayer(_game, this);
		}
	}

}
