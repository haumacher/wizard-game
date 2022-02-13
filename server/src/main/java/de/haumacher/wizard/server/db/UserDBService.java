/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db;

import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.haumacher.wizard.server.db.h2.H2UserDB;

/**
 * {@link ServletContextListener} starting the database.
 */
public class UserDBService extends org.h2.server.web.DbStarter {

	private static UserDB INSTANCE;
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		
        ServletContext servletContext = servletContextEvent.getServletContext();
        Connection connection = (Connection) servletContext.getAttribute("connection");
		
        INSTANCE = new H2UserDB(connection);
	}
	
	/**
	 * The {@link UserDB} singleton.
	 */
	public static UserDB getInstance() {
		return INSTANCE;
	}

}
