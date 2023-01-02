/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.logic.ClientConnection;
import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.R;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.AddEmail;
import de.haumacher.wizard.msg.AddEmailSuccess;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.msg.ConfirmRound;
import de.haumacher.wizard.msg.ConfirmTrick;
import de.haumacher.wizard.msg.CreateAccount;
import de.haumacher.wizard.msg.CreateAccountResult;
import de.haumacher.wizard.msg.CreateGame;
import de.haumacher.wizard.msg.Error;
import de.haumacher.wizard.msg.GameCmd;
import de.haumacher.wizard.msg.GameStarted;
import de.haumacher.wizard.msg.Hello;
import de.haumacher.wizard.msg.HelloResult;
import de.haumacher.wizard.msg.JoinGame;
import de.haumacher.wizard.msg.Lead;
import de.haumacher.wizard.msg.LeaveGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.ListGamesResult;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.LoginFailed;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Reconnect;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.StartGame;
import de.haumacher.wizard.msg.VerifyEmail;
import de.haumacher.wizard.msg.VerifyEmailSuccess;
import de.haumacher.wizard.msg.Welcome;
import de.haumacher.wizard.resources.StaticResources.Resource;
import de.haumacher.wizard.server.db.DBException;
import de.haumacher.wizard.server.db.UserDB;
import de.haumacher.wizard.server.db.UserDBService;
import de.haumacher.wizard.server.mail.MailServiceStarter;

/**
 * Server-side logic for a single player.
 */
public class ClientHandler implements Cmd.Visitor<Void, Void, IOException>, ClientConnection {

	private static final Logger LOG = LoggerFactory.getLogger(ClientHandler.class);

	private final WizardServer _server;
	
	private WizardGame _game;
	
	private boolean _loggedIn = false;

	private Consumer<Msg> _msgSink;

	private GameClient _handle;
	
	private Locale _locale = Locale.getDefault();
	
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
			_game.removePlayer(_handle, this);
		}
	}

	/** 
	 * Processes the given {@link Cmd} received from the client-side.
	 */
	public void handleCmd(Cmd cmd) throws IOException {
		if (cmd != null) {
			LOG.info(_handle.getName() + " -> " + cmd);
			cmd.visit(this, null);
		} else {
			LOG.warn("Received unknown command from '" + _handle + "'.");
		}
	}

	@Override
	public Void visit(Hello self, Void arg) throws IOException {
		if (!self.getLanguage().isBlank()) {
			_locale = new Locale(self.getLanguage().trim());
		}
		
		boolean ok;
		String message;
		if (self.getVersion() != WizardGame.PROTOCOL_VERSION) {
			if (self.getVersion() > WizardGame.PROTOCOL_VERSION) {
				message = R.errVersionToNew.format();
			} else {
				message = R.errVersionToOld.format();
			}
			ok = false;
		} else {
			message = R.connectionAccepted.format();
			ok = true;
		}
		
		sendMessage(HelloResult.create().setOk(ok).setMsg(message).setVersion(WizardGame.PROTOCOL_VERSION));
		return null;
	}

	@Override
	public Void visit(CreateAccount self, Void arg) throws IOException {
		UserDB db = UserDBService.getInstance();
		
		CreateAccountResult userCreated;
		try {
			userCreated = db.createUser(self.getNickname());
		} catch (DBException ex) {
			sendError(R.errAccountCreationFailed_msg.fill(ex.getMessage()));
			return null;
		}
		
		sendMessage(userCreated);
		return null;
	}

	@Override
	public Void visit(AddEmail self, Void arg) throws IOException {
		UserDB db = UserDBService.getInstance();
		
		String token;
		try {
			token = db.addEmail(self.getUid(), self.getSecret(), self.getEmail());
		} catch (DBException ex) {
			sendError(R.errCannotAddEmail_msg.fill(ex.getMessage()));
			return null;
		}
		
		try {
			MailServiceStarter.getInstance().sendActivationMail(self.getEmail(), self.getUid(), token);
		} catch (MessagingException ex) {
			sendError(R.sendingVerificationMailFailed_msg.fill(ex.getMessage()));
			return null;
		}

		sendMessage(AddEmailSuccess.create());
		return null;
	}

	@Override
	public Void visit(VerifyEmail self, Void arg) throws IOException {
		UserDB db = UserDBService.getInstance();
		
		try {
			db.verifyEmail(self.getUid(), self.getToken());
		} catch (DBException ex) {
			sendError(R.errVerificationFailed_msg.fill(ex.getMessage()));
			return null;
		}
		
		sendMessage(VerifyEmailSuccess.create());
		return null;
	}
	
	@Override
	public Void visit(Login self, Void arg) throws IOException {
		UserDB db = UserDBService.getInstance();
		String nickName;
		try {
			nickName = db.login(self.getUid(), self.getSecret());
		} catch (DBException ex) {
			sendMessage(LoginFailed.create().setMsg(R.errInvalidCredentials.format()));
			return null;
		}
		
		_loggedIn = true;
		_handle.getData().setName(nickName);
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
			sendError(R.errPlayerNotFound);
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
		sendError(R.errGameAlreadyJoined);
	}

	private void sendErrorNoSuchGame() {
		sendError(R.errGameNoLongerExists);
	}
	
	@Override
	public void sendError(Resource message) {
		sendMessage(Error.create().setMessage(message.format(_locale)));
	}

	@Override
	public void sendMessage(Msg msg) {
		_msgSink.accept(msg);
	}

	@Override
	public Void visit(StartGame self, Void arg) throws IOException {
		if (_game == null) {
			sendError(R.errNoGameJoined);
			return null;
		}
		if (!_game.getGameId().equals(self.getGameId())) {
			sendError(R.errMustNotStartForeignGame);
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
			sendError(R.errGameAlreadyStarted);
			return null;
		}
		return null;
	}

	@Override
	public Void visit(LeaveGame self, Void arg) throws IOException {
		if (_game != null && _game.getGameId().equals(self.getGameId())) {
			_game.removePlayer(_handle, this);
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
			sendError(R.errNotLoggedIn);
			return null;
		}
		if (_game == null) {
			sendError(R.errNoGameJoined);
			return null;
		}

		return self.visit(_game, _handle);
	}

}
