/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db.h2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import de.haumacher.wizard.msg.CreateAccountResult;
import de.haumacher.wizard.server.db.DBException;
import junit.framework.TestCase;

/**
 * Test case for {@link H2UserDB}.
 */
public class TestH2UserDB2 extends TestCase {
	
	private H2UserDB _db;
	private Connection _connection;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Properties properties = new Properties();
		properties.setProperty("user", "user");
		properties.setProperty("password", "password");

		_connection = org.h2.Driver.load().connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", properties);
		
		StringBuilder builder = new StringBuilder();
		try (Statement statement = _connection.createStatement()) {
			try (InputStream in = getClass().getResourceAsStream("/setup/setup.sql")) {
				try (Reader r = new InputStreamReader(in, "utf-8")) {
					try (BufferedReader reader = new BufferedReader(r)) {
						while (true) {
							String line = reader.readLine();
							if (line == null) {
								break;
							}
							if (line.startsWith("--")) {
								continue;
							}
							if (line.endsWith(";")) {
								builder.append(line.substring(0, line.length() - 1));
								
								statement.execute(builder.toString());
								builder.setLength(0);
							} else {
								builder.append(line);
							}
						}

						if (builder.length() > 0) {
							statement.execute(builder.toString());
						}
					}
				}
			}
		}
		
		_db = new H2UserDB(_connection);
	}
	
	@Override
	protected void tearDown() throws Exception {
		_connection.close();
		_db = null;
		super.tearDown();
	}
	
	public void testCreate() throws DBException {
		CreateAccountResult account1 = _db.createUser("haui");
		String nickName1 = _db.login(account1.getUid(), account1.getSecret());
		assertEquals("haui",nickName1);
		
		String token = _db.addEmail(account1.getUid(), account1.getSecret(), "haui@haumacher.de");
		_db.verifyEmail(account1.getUid(), token);
		
		String token2 = _db.requestSecret("haui@haumacher.de");
		CreateAccountResult account2 = _db.newSecret("haui@haumacher.de", token2);
		
		String nickName2 = _db.login(account1.getUid(), account2.getSecret());
		assertEquals(nickName2, nickName1);
	}

}
