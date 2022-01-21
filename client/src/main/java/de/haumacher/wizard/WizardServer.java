/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import de.haumacher.wizard.msg.Cmd;

/**
 * Abstraction of the server-part of the game.
 */
public interface WizardServer {
	
	/**
	 * Sends the given command to the server.
	 */
	void sendCommand(Cmd cmd);

}
