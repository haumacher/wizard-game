/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.util.function.Consumer;

import de.haumacher.wizard.msg.Msg;

/**
 * Common service provider interface of {@link WizardConnection} implementations.
 */
public interface WizardConnectionSPI extends WizardConnection, AutoCloseable {

	/** 
	 * Opens the connection.
	 */
	void start(Consumer<Msg> client) throws Exception;

}
