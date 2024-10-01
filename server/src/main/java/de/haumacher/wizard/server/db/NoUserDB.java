/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.msg.CreateAccountResult;
import de.haumacher.wizard.server.data.Account;

/**
 * Stub for {@link UserDB} that does not fulfill any request.
 */
public class NoUserDB implements UserDB {
	
	private static final Logger LOG = LoggerFactory.getLogger(NoUserDB.class);
	
	private final Map<String, Account> _accoundByUID = new HashMap<>();
	
	/**
	 * Creates a {@link NoUserDB}.
	 */
	public NoUserDB() {
		super();
	}
	
	@Override
	public void startup() {
		LOG.info("No user DB found.");
	}

	@Override
	public CreateAccountResult createUser(String nickname) throws DBException {
		String uid = UUID.randomUUID().toString();
		String secret = UUID.randomUUID().toString();
		CreateAccountResult account = CreateAccountResult.create().setUid(uid).setSecret(secret);
		
		_accoundByUID.put(uid, Account.create().setNick(nickname).setUid(uid).setPassword(secret));
		
		return account;
	}
	
	@Override
	public boolean deleteUser(String eMail) {
		return false;
	}

	@Override
	public String login(String uid, String secret) throws DBException {
		Account account = _accoundByUID.get(uid);
		if (account == null) {
			throw new DBException("No such user or wrong password: " + uid);
		}
		if (!account.getPassword().equals(secret)) {
			throw new DBException("No such user or wrong password: " + uid);
		}
		return account.getNick();
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
