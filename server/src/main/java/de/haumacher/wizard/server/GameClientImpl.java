/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.logic.ClientConnection;
import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.resources.StaticResources.Resource;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class GameClientImpl implements GameClient {

	private static final Logger LOG = LoggerFactory.getLogger(GameClientImpl.class);

	private static final ClientConnection NONE = new ClientConnection() {
		@Override
		public void sendMessage(Msg msg) {
			LOG.warn("Trying to send message to void connection: " + msg);
		}

		@Override
		public void sendError(Resource message) {
			LOG.warn("Trying to send errorto void connection: " + message);
		}
	};

	private Player _data;
	
	private ClientConnection _connection;
	
	/** 
	 * Creates a {@link GameClientImpl}.
	 */
	public GameClientImpl(ClientConnection connection) {
		_connection = connection;
	}
	
	@Override
	public void reconnectTo(ClientConnection connection) {
		_connection = connection;
	}
	
	@Override
	public boolean disconnect(ClientConnection connection) {
		boolean wasConnected = _connection == connection;
		if (wasConnected) {
			_connection = NONE;
		}
		return wasConnected;
	}
	
	@Override
	public boolean isLoggedIn() {
		return _data != null;
	}
	
	@Override
	public Player getData() {
		if (_data == null) {
			throw new IllegalStateException("Not logged in.");
		}
		return _data;
	}
	
	@Override
	public void setData(Player data) {
		if (_data != null) {
			throw new IllegalStateException("Must not override login information.");
		}
		_data = data;
	}

	@Override
	public void sendError(Resource message) {
		_connection.sendError(message);
	}

	@Override
	public void sendMessage(Msg msg) {
		_connection.sendMessage(msg);
	}

	@Override
	public String toString() {
		return getName() + "(" + getId() + ")";
	}

}
