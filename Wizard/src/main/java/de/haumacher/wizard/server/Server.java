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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
import de.haumacher.wizard.msg.GameCreated;
import de.haumacher.wizard.msg.GameDeleted;
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.Join;
import de.haumacher.wizard.msg.LeaveGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.ListGamesResult;
import de.haumacher.wizard.msg.LoggedIn;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.Put;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartGame;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class Server {

	public static void main(String[] args) throws IOException {
		new Server().run();
	}
	
	final ConcurrentMap<String, Client> _clients = new ConcurrentHashMap<>();
	
	final ConcurrentMap<String, WizardGame> _games = new ConcurrentHashMap<>();

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
		for (Client other : _clients.values()) {
			other.sendMessage(msg);
		}
	}

	class Client implements Runnable, Cmd.Visitor<Void, Void, IOException>, GameClient {

		private JsonReader _in;
		private JsonWriter _out;
		
		private WizardGame _game;
		
		private Player _data = Player.create().setId(UUID.randomUUID().toString());

		public Client(ReaderAdapter in, WriterAdapter out) {
			_in = new JsonReader(in);
			_out = new JsonWriter(out);
		}

		@Override
		public void run() {
			_clients.put(getId(), this);
			try {
				_out.beginArray();
				_in.beginArray();
				while (true) {
					try {
						if (_in.peek() == JsonToken.END_ARRAY) {
							break;
						}
						Cmd cmd = Cmd.readCmd(_in);
						System.out.println(_data.getName() + " -> " + cmd);
						cmd.visit(this, null);
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
				_clients.remove(getId());
				
				if (_game != null) {
					_game.removePlayer(this);
					
					if (_game.getData().getPlayers().isEmpty()) {
						String gameId = _game.getGameId();
						_games.remove(gameId);
						broadCast(GameDeleted.create().setGameId(gameId));
					}
				}
			}
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
		public Void visit(Put self, Void arg) throws IOException {
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
			if (_game == null) {
				sendError("Du bist keinem Spiel beigetreten.");
				return null;
			}

			return self.visit(_game, this);
		}

		@Override
		public Void visit(Login self, Void arg) throws IOException {
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
			
			_game = new WizardGame(g -> _games.remove(g.getGameId()));
			_games.put(_game.getGameId(), _game);
			broadCast(GameCreated.create().setOwnerId(getId()).setGame(_game.getData()));
			_game.addPlayer(this);
			
			return null;
		}

		@Override
		public void sendError(String message) {
			sendMessage(Error.create().setMessage(message));
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
		public Void visit(Join self, Void arg) throws IOException {
			if (_game != null) {
				sendError("Du bist schon einem Spiel beigetreten.");
				return null;
			}
			_game = _games.get(self.getGameId());
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
					.setGames(_games.values().stream().map(WizardGame::getData).collect(Collectors.toList())));
			return null;
		}

		@Override
		public Player getData() {
			return _data;
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
		public String getId() {
			return _data.getId();
		}
		
	}
}
