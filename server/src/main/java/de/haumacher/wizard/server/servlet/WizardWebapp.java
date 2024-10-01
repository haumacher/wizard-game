/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.server.WizardServer;

/**
 * Main entry point for the wizard web application.
 */
@WebListener
public class WizardWebapp implements ServletContextListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(WizardWebapp.class);
	
	private static WizardServer _server;
	
	public static WizardServer getServer() {
		return _server;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("Started wizard server.");
		
		_server = new WizardServer();
		
		sce.getServletContext().setAttribute(WizardServer.class.getName(), _server);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOG.info("Shutting down wizard server.");
		
		if (_server != null) {
			_server.shutdown();
		}
		_server = null;
	}

}
