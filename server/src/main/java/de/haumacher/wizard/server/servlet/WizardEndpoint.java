/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.servlet;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import de.haumacher.msgbuf.io.StringR;
import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.CreateGame;
import de.haumacher.wizard.msg.Error;
import de.haumacher.wizard.msg.Game;
import de.haumacher.wizard.msg.GameCmd;
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.JoinGame;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.LeaveGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.ListGamesResult;
import de.haumacher.wizard.msg.LoggedIn;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartGame;
import de.haumacher.wizard.server.WizardServer;

/**
 * Websocket endpoint for the wizard game.
 */
@ServerEndpoint(value = "/ws")
//@ServerEndpoint(value = "/ws", configurator = WizardEndpoint.Configurator.class)
public class WizardEndpoint implements Cmd.Visitor<Void, Void, IOException>, GameClient {

	// Makes only sense if there is a session.
	
//	public static class Configurator extends ServerEndpointConfig.Configurator {
//
//	    private static final String HTTP_SESSION = "httpSession";
//
//		@Override
//	    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
//	        HttpSession httpSession = (HttpSession) request.getHttpSession();
//	        config.getUserProperties().put(HTTP_SESSION, httpSession);
//	    }
//
//	}
	
	private WizardGame _game;
	
	private Player _data = Player.create().setId(UUID.randomUUID().toString()).setName("Anonymous");
	
	private boolean _loggedIn = false;

	private Session _session;

	private WizardServer _server;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
		_session = session;
		
//        HttpSession httpSession = (HttpSession) config.getUserProperties().get(Configurator.HTTP_SESSION);
//        ServletContext servletContext = httpSession.getServletContext();    
//        
//        _server = (WizardServer) servletContext.getAttribute(WizardServer.class.getName());
        _server = WizardWebapp.getServer();
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
		try {
	    	Cmd cmd = Cmd.readCmd(new JsonReader(new StringR(message)));
			cmd.visit(this, null);
			
			System.out.println(_data.getName() + " -> " + cmd);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			
			String report = ex.getMessage();
			if (report == null) {
				report = ex.getClass().getName();
			} else {
				report = ex.getClass().getName() + ": " + report;
			}
			sendError("Es ist ein Fehler aufgetreten: " + report);
		}
    }
    
    @OnClose
    public void onClose(Session session) throws IOException {
		_server.removeClient(this);
		if (_game != null) {
			_server.removePlayer(_game, this);
			_game = null;
		}
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    	System.err.println("Error in endpoint for: " + _data);
    	throwable.printStackTrace();
    }
    
    @Override
	public Player getData() {
		return _data;
	}

	@Override
	public String getId() {
		return _data.getId();
	}
	
	@Override
	public void sendError(String message) {
		sendMessage(Error.create().setMessage(message));
	}
	
	@Override
	public synchronized void sendMessage(Msg msg) {
		System.out.println(_data.getName() + " <- " + msg);
		_session.getAsyncRemote().sendText(msg.toString());
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
		
		_server.addClient(this);

		sendMessage(LoggedIn.create().setPlayerId(getId()));
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
		List<Game> gameData = _server.getWaitingGames().stream().map(WizardGame::getData).collect(Collectors.toList());
		sendMessage(ListGamesResult.create().setGames(gameData));
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
}
