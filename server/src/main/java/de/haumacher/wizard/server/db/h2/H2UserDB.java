/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db.h2;

import static de.haumacher.wizard.server.db.h2.schema.tables.EmailToken.*;
import static de.haumacher.wizard.server.db.h2.schema.tables.UserSession.*;
import static de.haumacher.wizard.server.db.h2.schema.tables.Users.*;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.Arrays;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.msg.CreateAccountResult;
import de.haumacher.wizard.server.db.DBException;
import de.haumacher.wizard.server.db.UserDB;
import de.haumacher.wizard.server.db.h2.schema.Public;

/**
 * The {@link UserDB} stored in a H2 relational database.
 */
public class H2UserDB  implements UserDB {
	
	private static final Logger LOG = LoggerFactory.getLogger(H2UserDB.class);

	private static final int ONE_MINUTE = 60 * 1000;
	private static final Charset UTF_8 = Charset.forName("utf-8");
	private static final long ONE_HOUR = 60 * ONE_MINUTE;
	private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private DSLContext _context;
	private SecureRandom _random;
	
	/** 
	 * Creates a {@link H2UserDB}.
	 */
	public H2UserDB(Connection connection) {
		_context = DSL.using(connection, SQLDialect.H2);
		_random = new SecureRandom();
	}
	
	@Override
	public void startup() {
		// _context.createDatabaseIfNotExists(DefaultCatalog.DEFAULT_CATALOG).execute();
		// _context.createSchemaIfNotExists(Public.PUBLIC).execute();
		Public.PUBLIC.tableStream().map(_context::createTableIfNotExists).forEach(Query::execute);
		
		int userCnt = _context.fetchCount(USERS);
		LOG.info("User DB startup, " + userCnt + " users registered.");
	}

	@Override
	public CreateAccountResult createUser(String nickname) throws DBException {
		Record result = _context.select().from(USERS).where(USERS.NICKNAME.eq(nickname)).fetchAny();
		if (result != null) {
			// Fail fast.
			throw new DBException("Nickname already exists.");
		}

		String uid = newUUID();
		String secret = newSecret();
		byte[] hash = hash(secret);
		
		long now = System.currentTimeMillis();
		
		try {
			// TODO: Remove language from user table. Language is handled dynamically.
			
// Seems not to work due to a bug in org.jooq SQL generation for H2?
//			_context.begin(
				_context.insertInto(USERS, USERS.UID, USERS.NICKNAME, USERS.LANGUAGE, USERS.VERIFIED, USERS.CREATED, USERS.LAST_LOGIN)
					.values(uid, nickname, "en", false, now, now).execute();
				_context.insertInto(USER_SESSION, USER_SESSION.UID, USER_SESSION.HASH)
					.values(uid, hash).execute();
//			).execute();
		} catch (DataAccessException ex) {
			throw new DBException("User creation for '" + nickname + "' failed.", ex);
		}
		
		return CreateAccountResult.create().setUid(uid).setSecret(secret);
	}
	
	@Override
	public String login(String uid, String secret) throws DBException {
		checkCredentials(uid, secret);
		
		long now = System.currentTimeMillis();
		_context.update(USERS)
			.set(USERS.LAST_LOGIN, now)
			.where(USERS.UID.eq(uid))
			.execute();
		
		return getUserInfo(uid);
	}

	private void checkCredentials(String uid, String secret) throws DBException {
		byte[] hash = hash(secret);

		Record result = _context.select()
			.from(USER_SESSION)
			.where(USER_SESSION.UID.eq(uid))
			.fetchAny();

		if (result == null) {
			throw new DBException("User does not exist.");
		}
		
		byte[] expected = result.getValue(USER_SESSION.HASH);
		boolean ok = Arrays.equals(hash, expected);
		if (!ok) {
			throw new DBException("Invalid login credentials.");
		}
	}

	private String getUserInfo(String uid) {
		Record info = _context.select().from(USERS).where(USERS.UID.eq(uid)).fetchOne();
		return info.getValue(USERS.NICKNAME);
	}
	
	@Override
	public String addEmail(String uid, String secret, String email) throws DBException {
		checkCredentials(uid, secret);
		
		String token = generateToken();
		byte[] tokenHash = hash(token);
		long notAfter = System.currentTimeMillis() + ONE_HOUR;
		
		byte[] emailHash = hashEMail(email);

		//		_context.begin(
			_context.deleteFrom(EMAIL_TOKEN)
				.where(EMAIL_TOKEN.UID.eq(uid)).execute();
			_context.update(USERS)
				.set(USERS.EMAIL, emailHash)
				.set(USERS.VERIFIED, false)
				.where(USERS.UID.eq(uid)).execute();
			_context.insertInto(EMAIL_TOKEN)
				.columns(EMAIL_TOKEN.UID, EMAIL_TOKEN.EMAIL, EMAIL_TOKEN.HASH, EMAIL_TOKEN.NOT_AFTER)
				.values(uid, emailHash, tokenHash, notAfter).execute();
//		).execute();
		
		return token;
	}

	private byte[] hashEMail(String email) {
		return hash(email.trim().toLowerCase());
	}
	
	@Override
	public void verifyEmail(String uid, String token) throws DBException {
		checkToken(uid, token);

//		_context.begin(
			_context.deleteFrom(EMAIL_TOKEN).where(EMAIL_TOKEN.UID.eq(uid)).execute();
			_context.update(USERS).set(USERS.VERIFIED, true).where(USERS.UID.eq(uid)).execute();
//		).execute();
	}

	private void checkToken(String uid, String token) throws DBException {
		Record result = _context.select()
			.from(EMAIL_TOKEN)
			.where(EMAIL_TOKEN.UID.eq(uid))
			.fetchAny();
		checkToken(token, result);
	}

	private void checkToken(byte[] emailHash, String token) throws DBException {
		Record result = _context.select()
				.from(EMAIL_TOKEN)
				.where(EMAIL_TOKEN.EMAIL.eq(emailHash))
				.fetchAny();
		checkToken(token, result);
	}
	
	private void checkToken(String token, Record tokenResult) throws DBException {
		if (tokenResult == null) {
			throw new DBException("No token found.");
		}
		
		byte[] expected = tokenResult.getValue(EMAIL_TOKEN.HASH);
		byte[] hash = hash(token);
		boolean ok = Arrays.equals(hash, expected);
		if (!ok) {
			throw new DBException("Token missmatch.");
		}
	}
	
	@Override
	public String requestSecret(String email) throws DBException {
		byte[] emailHash = hashEMail(email);
		Record tokenRecord = _context.select().from(EMAIL_TOKEN).where(EMAIL_TOKEN.EMAIL.eq(emailHash)).fetchAny();
		if (tokenRecord != null) {
			long notAfter = tokenRecord.getValue(EMAIL_TOKEN.NOT_AFTER);
			long now = System.currentTimeMillis();
			if (now < notAfter) {
				long minutes = (notAfter - now) / ONE_MINUTE;
				throw new DBException("Login request already sent, try again in " + minutes + " minutes.");
			}
			_context.deleteFrom(EMAIL_TOKEN).where(EMAIL_TOKEN.EMAIL.eq(emailHash));
		}
		
		Record userRecord = _context.select().from(USERS).where(USERS.EMAIL.eq(emailHash)).fetchAny();
		if (userRecord == null) {
			throw new DBException("E-mail not registered.");
		}
		
		String token = generateToken();
		byte[] tokenHash = hash(token);
		long notAfter = System.currentTimeMillis() + ONE_HOUR;
		
		_context.insertInto(EMAIL_TOKEN)
			.columns(EMAIL_TOKEN.UID, EMAIL_TOKEN.EMAIL, EMAIL_TOKEN.HASH, EMAIL_TOKEN.NOT_AFTER)
			.values(userRecord.getValue(USERS.UID), emailHash, tokenHash, notAfter)
			.execute();
		
		return token;
	}

	private String generateToken() {
		int tokenNum = _random.nextInt(100000000);
		String token = Integer.toString(tokenNum);
		while (token.length() < 8) {
			token = '0' + token;
		}
		return token;
	}

	@Override
	public CreateAccountResult newSecret(String email, String token) throws DBException {
		byte[] emailHash = hashEMail(email);
		checkToken(emailHash, token);
		
		Record info = _context.select().from(USERS).where(USERS.EMAIL.eq(emailHash)).fetchAny();
		if (info == null) {
			throw new DBException("E-mail not registered.");
		}
		
		String uid = info.getValue(USERS.UID);
		Record result = _context.select().from(USER_SESSION).where(USER_SESSION.UID.eq(uid)).fetchAny();
		if (result == null) {
			throw new DBException("Session not found.");
		}
		
		String secret = newUUID();
		byte[] hash = hash(secret);
		_context.update(USER_SESSION)
			.set(USER_SESSION.HASH, hash)
			.where(USER_SESSION.UID.eq(uid))
			.execute();
		
		return CreateAccountResult.create().setUid(uid).setSecret(secret);
	}
	
	private String newUUID() {
		return toID(randomBytes());
	}

	private String newSecret() {
		return toHex(randomBytes());
	}
	
	private byte[] randomBytes() {
		byte[] secretBytes = new byte[16];
		_random.nextBytes(secretBytes);
		return secretBytes;
	}

	private static String toHex(byte[] secretBytes) {
		StringBuilder result = new StringBuilder(secretBytes.length * 2);
		for (int n = 0, cnt = secretBytes.length; n < cnt; n++) {
			byte data = secretBytes[n];
			result.append(HEX[(data >>> 4) & 0x0F]);
			result.append(HEX[data & 0x0F]);
		}
		return result.toString();
	}
	
	private static String toID(byte[] secretBytes) {
		StringBuilder result = new StringBuilder(secretBytes.length * 2 + 2);
		for (int n = 0, cnt = secretBytes.length; n < cnt; n++) {
			if (n == 8 || n == 24) {
				result.append('-');
			}
			
			byte data = secretBytes[n];
			result.append(HEX[(data >>> 4) & 0x0F]);
			result.append(HEX[data & 0x0F]);
		}
		return result.toString();
	}

	private byte[] hash(String secret) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(secret.getBytes(UTF_8));
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException("Cannot create hash value.", ex);
		}
	}

}
