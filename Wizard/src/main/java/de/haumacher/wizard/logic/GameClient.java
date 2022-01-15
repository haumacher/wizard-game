/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public interface GameClient {

	String getId();

	void sendMessage(Msg msg);

	Player getData();

	void sendError(String message);

}
