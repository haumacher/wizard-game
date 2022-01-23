/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import de.haumacher.wizard.server.WizardServer;

/**
 * Main entry point for the wizard web application.
 */
@WebListener
public class WizardWebapp implements ServletContextListener {
	
	private static WizardServer _server;
	
	public static WizardServer getServer() {
		return _server;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		_server = new WizardServer();
		
		sce.getServletContext().setAttribute(WizardServer.class.getName(), _server);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (_server != null) {
			_server.shutdown();
		}
		_server = null;
	}

}
