/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import de.haumacher.wizard.msg.Error;
import de.haumacher.wizard.msg.Msg;

/**
 * Abstraction of the client-facing interface of a player.
 */
public interface ClientConnection {

	/**
	 * Sends a message to the UI.
	 */
	void sendMessage(Msg msg);

	/**
	 * Sends an error message to the UI.
	 */
	default void sendError(String message) {
		sendMessage(Error.create().setMessage(message));
	}

}
