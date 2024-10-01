/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.mail;

import java.util.Properties;

import jakarta.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MailService} singleton.
 */
@WebListener
public class MailServiceStarter implements ServletContextListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(MailServiceStarter.class);
	
	private static MailService INSTANCE;
	
	/**
	 * The {@link MailService} instance.
	 */
	public static MailService getInstance() {
		return INSTANCE;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			InitialContext initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			String user;
			String password;
			try {
				user = (String) envCtx.lookup("smtp/user");        
				password = (String) envCtx.lookup("smtp/password");
				
				Properties properties = new Properties();
				Context propertyContext = (Context) envCtx.lookup("smtp/properties");
				if (propertyContext != null) {
					NamingEnumeration<NameClassPair> list = envCtx.list("smtp/properties");
					while (list.hasMore()) {
						NameClassPair pair = list.next();
						
						if ("java.lang.String".equals(pair.getClassName())) {
							String name = pair.getName();
							String value = (String) propertyContext.lookup(name);
							properties.setProperty(name, value);
						}
					}
				}
				
				MailService mailService = new SmtpMailService(user, password, properties);
				INSTANCE = mailService;
			} catch (NameNotFoundException ex) {
				INSTANCE = new MailService() {
					@Override
					public void startUp() {
						LOG.warn("No mail configuration, sending mails is deactivated.");
					}

					@Override
					public void shutdown() {
						LOG.warn("Shutting down mail service.");
					}

					@Override
					public void sendActivationMail(String email, String uid, String token) throws MessagingException {
						LOG.warn("Skipped sending mail to '" + uid +"', no mail service active.");
					}
				};
			}

			INSTANCE.startUp();
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		INSTANCE.shutdown();
		INSTANCE = null;
	}
	
}
