/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.stream.Collectors;

import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.msgbuf.json.JsonToken;
import de.haumacher.msgbuf.json.JsonWriter;
import de.haumacher.msgbuf.server.io.ReaderAdapter;
import de.haumacher.msgbuf.server.io.WriterAdapter;
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
import de.haumacher.wizard.msg.LoggedIn;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartGame;

/**
 * Stand-alone server for the wizard game that uses plain sockets for communication.
 */
public class StandaloneServer {

	public static void main(String[] args) throws IOException {
		new StandaloneServer().run();
	}
	
	final WizardServer _server = new WizardServer();
	
	private void run() throws IOException {
		try (ServerSocket server = new ServerSocket(8090)) {
			while (true) {
				Socket socket = server.accept();
				Client client = createClient(socket.getInputStream(), socket.getOutputStream());
				new Thread(client).start();
			}
		}
	}

	final Client createClient(InputStream inStream, OutputStream outStream) throws UnsupportedEncodingException {
		ReaderAdapter in = new ReaderAdapter(new InputStreamReader(inStream, "utf-8"));
		WriterAdapter out = new WriterAdapter(new OutputStreamWriter(outStream, "utf-8"));
		return new Client(in, out);
	}
	
	public void broadCast(Msg msg) {
		_server.broadCast(msg);
	}

	class Client implements Runnable, Cmd.Visitor<Void, Void, IOException>, GameClient {

		private JsonReader _in;
		private JsonWriter _out;
		
		private WizardGame _game;
		
		private Player _data = Player.create().setId(UUID.randomUUID().toString()).setName("Anonymous");
		private boolean _loggedIn = false;

		public Client(ReaderAdapter in, WriterAdapter out) {
			_in = new JsonReader(in);
			_out = new JsonWriter(out);
		}

		@Override
		public void run() {
			_server.addClient(this);
			try {
				_out.beginArray();
				_in.beginArray();
				while (true) {
					try {
						if (_in.peek() == JsonToken.END_ARRAY) {
							break;
						}
						
						Cmd cmd = Cmd.readCmd(_in);
						if (cmd != null) {
							System.out.println(_data.getName() + " -> " + cmd);
							cmd.visit(this, null);
						} else {
							System.err.println("Received unknown command from '" + _data + "'.");
						}
					} catch (RuntimeException ex) {
						ex.printStackTrace();
						
						String message = ex.getMessage();
						if (message == null) {
							message = ex.getClass().getName();
						} else {
							message = ex.getClass().getName() + ": " + message;
						}
						sendError("Es ist ein Fehler aufgetreten: " + message);
					}
				}
				_in.endArray();
				_out.endArray();
			} catch (IOException ex) {
				System.out.println("Client stopped.");
			} finally {
				_server.removeClient(this);
				if (_game != null) {
					_server.removePlayer(_game, this);
				}
			}
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
			try {
				System.out.println(_data.getName() + " <- " + msg);
				msg.writeTo(_out);
				_out.flush();
			} catch (IOException ex) {
				System.err.println("Cannot send to " + _data + ": " + msg + " (" + ex.getMessage() + ")");
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
			_data.setName(self.getName());
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
			
			broadCast(GameStarted.create().setGame(_game.getData()));
			
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

	}
}
