/*
 * Copyright (c) 2023 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.mail;

import jakarta.mail.MessagingException;

/**
 * Mail service for user registration.
 */
public interface MailService {

	/** 
	 * Hook called during startup.
	 */
	void startUp();

	/** 
	 * Hook called during shutdown.
	 */
	void shutdown();

	/** 
	 * Sends the given mail to the given user.
	 *
	 * @param email The mail text.
	 * @param uid The user ID.
	 * @param token The verification token.
	 */
	void sendActivationMail(String email, String uid, String token) throws MessagingException;

}
