/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import junit.framework.TestCase;

/**
 * Test for {@link MailService}
 */
public class TestMailService extends TestCase {
	
	/**
	 * Tests sending plain-text mails.
	 */
	public void testSend() throws FileNotFoundException, IOException, NoSuchProviderException, MessagingException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("./test-mail.properties")));
		String user = popProperty(properties, "test.user");
		String password = popProperty(properties, "test.password");
		String receiver = popProperty(properties, "test.receiver");
		
		MailService service = new MailService(user, password, properties);
		service.startUp();
		
		service.sendActivationMail(receiver, "4711", "12345678");
		
		service.shutdown();
	}

	private String popProperty(Properties properties, String name) {
		String user = properties.getProperty(name);
		properties.remove(name);
		return user;
	}

}
