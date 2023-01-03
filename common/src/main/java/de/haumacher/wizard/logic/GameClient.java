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
	 * Whether this client is ready to play.
	 */
	boolean isLoggedIn();

	/**
	 * The player data.
	 */
	Player getData();
	
	/**
	 * @see #getData()
	 */
	void setData(Player data);

	/**
	 * The player ID.
	 */
	default String getId() {
		return isLoggedIn() ? getData().getId() : null;
	}

	/**
	 * The player name.
	 */
	default String getName() {
		return isLoggedIn() ? getData().getName() : "Anonymous";
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
