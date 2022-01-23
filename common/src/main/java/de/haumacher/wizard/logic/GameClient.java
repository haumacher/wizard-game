/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import de.haumacher.wizard.msg.Player;

/**
 * Abstraction of a player of a {@link WizardGame}.
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
	
}
