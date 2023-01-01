package de.haumacher.wizard.msg;

/**
 * Command to assign an email to an existing account. An e-mail is send to the provided address with a verification token that must be provided to {@link VerifyEmail} in a following request.
 */
public interface AddEmail extends LoginCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.AddEmail} instance.
	 */
	static de.haumacher.wizard.msg.AddEmail create() {
		return new de.haumacher.wizard.msg.impl.AddEmail_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.AddEmail} type in JSON format. */
	String ADD_EMAIL__TYPE = "AddEmail";

	/** @see #getUid() */
	String UID__PROP = "uid";

	/** @see #getSecret() */
	String SECRET__PROP = "secret";

	/** @see #getEmail() */
	String EMAIL__PROP = "email";

	/**
	 * The user ID, see {@link CreateAccountResult#getUid()}.
	 */
	String getUid();

	/**
	 * @see #getUid()
	 */
	de.haumacher.wizard.msg.AddEmail setUid(String value);

	/**
	 * The user's login credentials, see {@link CreateAccountResult#getSecret()}.
	 */
	String getSecret();

	/**
	 * @see #getSecret()
	 */
	de.haumacher.wizard.msg.AddEmail setSecret(String value);

	/**
	 * The e-mail address to assign to the user's account.
	 */
	String getEmail();

	/**
	 * @see #getEmail()
	 */
	de.haumacher.wizard.msg.AddEmail setEmail(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.AddEmail readAddEmail(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.AddEmail_Impl result = new de.haumacher.wizard.msg.impl.AddEmail_Impl();
		result.readContent(in);
		return result;
	}

}
