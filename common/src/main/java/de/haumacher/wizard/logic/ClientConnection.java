/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.resources.StaticResources.Resource;

/**
 * Abstraction of the client-facing interface of a player.
 */
public interface ClientConnection {

	/**
	 * Sends the given message to the client UI.
	 */
	void sendMessage(Msg msg);

	/**
	 * Sends an internationalized error message to the client UI.
	 */
	void sendError(Resource message);
	
}
