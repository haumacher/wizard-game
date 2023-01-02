/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.h2.tools.Server;
import org.h2.util.StringUtils;

import de.haumacher.wizard.server.db.h2.H2UserDB;

/**
 * {@link ServletContextListener} starting the database.
 */
@WebListener
public class UserDBService implements ServletContextListener {

	private static final String DB_TCP_SERVER_NAME = "db/tcpServer";

	private static final String DB_PASSWORD_NAME = "db/password";

	private static final String DB_USER_NAME = "db/user";

	private static final String DB_URL_NAME = "db/url";

	private static UserDB INSTANCE;
	
	private Server _server;
	private Connection _connection;
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            org.h2.Driver.load();
        	
			InitialContext initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			String tcpServer;
			try {
				tcpServer = (String) envCtx.lookup(DB_TCP_SERVER_NAME);        
				String[] params = StringUtils.arraySplit(tcpServer, ' ', true);
				_server = Server.createTcpServer(params);
				_server.start();
			} catch (NameNotFoundException ex) {
				System.out.println("No JDNI environment setting '" + DB_TCP_SERVER_NAME + "', no DB access through TCP.");
			}
			
			boolean error = false;
			String url = null;
			try {
				url = (String) envCtx.lookup(DB_URL_NAME);
			} catch (NameNotFoundException ex) {
				System.err.println("Missing JNDI environment setting: " + DB_URL_NAME);
				error = true;
			}
			
			String user = null;
			try {
				user = (String) envCtx.lookup(DB_USER_NAME);
			} catch (NameNotFoundException ex) {
				System.err.println("Missing JNDI environment setting: " + DB_USER_NAME);
				error = true;
			}
			
			String password = null;
			try {
				password = (String) envCtx.lookup(DB_PASSWORD_NAME);
			} catch (NameNotFoundException ex) {
				System.err.println("Missing JNDI environment setting: " + DB_PASSWORD_NAME);
				error = true;
			}
			
			if (error) {
				INSTANCE = new NoUserDB();
			} else {
				System.out.println("Opening user DB: " + url);
				_connection = DriverManager.getConnection(url, user, password);
				INSTANCE = new H2UserDB(_connection);
			}
			INSTANCE.startup();
		} catch (NamingException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * The {@link UserDB} singleton.
	 */
	public static UserDB getInstance() {
		return INSTANCE;
	}

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	if (_connection != null) {
    		try (Statement statement = _connection.createStatement()) {
    			statement.execute("SHUTDOWN");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		try {
    			_connection.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
        
        if (_server != null) {
            _server.stop();
            _server = null;
        }
        
        org.h2.Driver.unload();
        
        INSTANCE = null;
    }
	
}
