/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db;

import de.haumacher.wizard.server.db.model.CreateAccountResult;
import de.haumacher.wizard.server.db.model.UserInfo;

/**
 * Interface for the Wizard user database.
 */
public interface UserDB {

	CreateAccountResult createUser(String nickname, String language) throws DBException;

	UserInfo login(String uid, String secret) throws DBException;

	/**
	 * Request lost login credentials.
	 * 
	 * @return A verification token that must be provided to {@link #fetchSecret(String, String)} later on.
	 */
	String requestSecret(String email) throws DBException;

	/**
	 * Fetches existing login credentials.
	 *
	 * @param email
	 *        The email address that was used for authentication.
	 * @param token
	 *        The authentication token created in {@link #requestSecret(String)}.
	 * @return The login credentials for the account associated with the given email address.
	 */
	CreateAccountResult fetchSecret(String email, String token) throws DBException;

	/**
	 * Adds an (unverified) email for the given user.
	 * 
	 * @return A verification token that must be provided to {@link #verifyEmail(String, String, String)} later on.
	 */
	String addEmail(String uid, String secret, String email) throws DBException;

	/**
	 * Verifies the email token and sets the user's email address, if successful.
	 *
	 * @param uid
	 *        The user ID.
	 * @param secret
	 *        The users secret.
	 * @param token
	 *        The email token created by {@link #addEmail(String, String, String)}.
	 * @throws DBException
	 *         If an error occurs such as a token mismatch.
	 */
	void verifyEmail(String uid, String secret, String token) throws DBException;

}
