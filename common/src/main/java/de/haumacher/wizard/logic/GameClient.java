/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import de.haumacher.wizard.msg.Player;

/**
 * Abstraction of a single player.
 */
public interface GameClient extends ClientConnection {

	/**
	 * The player data.
	 */
	Player getData();

	/**
	 * The player ID.
	 */
	default String getId() {
		return getData().getId();
	}

	/**
	 * The player name.
	 */
	default String getName() {
		return getData().getName();
	}

	/**
	 * Takes over this player with a new {@link ClientConnection}.
	 */
	void reconnectTo(ClientConnection connection);

	/** 
	 * Removes the broken connection from this client. 
	 * 
	 * @return Whether the given connection was formerly the active connection of this client.
	 */
	boolean disconnect(ClientConnection connection);
	
}
