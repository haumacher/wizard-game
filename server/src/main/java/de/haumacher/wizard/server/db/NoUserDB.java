/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db;

import de.haumacher.wizard.msg.CreateAccountResult;

/**
 * Stub for {@link UserDB} that does not fulfill any request.
 */
public class NoUserDB implements UserDB {
	
	/**
	 * Singleton {@link NoUserDB} instance.
	 */
	public static final NoUserDB INSTANCE = new NoUserDB();

	private NoUserDB() {
		// Singleton constructor.
	}
	
	@Override
	public void startup() {
		System.out.println("No user DB found.");
	}

	@Override
	public CreateAccountResult createUser(String nickname) throws DBException {
		return CreateAccountResult.create().setUid(nickname).setSecret("123");
	}

	@Override
	public String login(String uid, String secret) throws DBException {
		return uid;
	}

	@Override
	public String requestSecret(String email) throws DBException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CreateAccountResult newSecret(String email, String token)
			throws DBException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String addEmail(String uid, String secret, String email)
			throws DBException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void verifyEmail(String uid, String token) throws DBException {
		throw new UnsupportedOperationException();
	}

}
