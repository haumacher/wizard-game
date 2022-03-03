/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.util.UUID;

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

	private static final ClientConnection NONE = new ClientConnection() {
		@Override
		public void sendMessage(Msg msg) {
			// Ignore.
		}

		@Override
		public void sendError(Resource message) {
			// Ignore.
		}
	};

	private final Player _data = Player.create().setId(UUID.randomUUID().toString()).setName("Anonymous");
	
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
	public Player getData() {
		return _data;
	}

	@Override
	public String getId() {
		return _data.getId();
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
