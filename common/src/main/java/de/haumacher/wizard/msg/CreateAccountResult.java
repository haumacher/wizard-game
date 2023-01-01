package de.haumacher.wizard.msg;

/**
 * Result of {@link CreateAccount}, if successful.
 */
public interface CreateAccountResult extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.CreateAccountResult} instance.
	 */
	static de.haumacher.wizard.msg.CreateAccountResult create() {
		return new de.haumacher.wizard.msg.impl.CreateAccountResult_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.CreateAccountResult} type in JSON format. */
	String CREATE_ACCOUNT_RESULT__TYPE = "CreateAccountResult";

	/** @see #getUid() */
	String UID__PROP = "uid";

	/** @see #getSecret() */
	String SECRET__PROP = "secret";

	/**
	 * The user ID of the newly created account.
	 */
	String getUid();

	/**
	 * @see #getUid()
	 */
	de.haumacher.wizard.msg.CreateAccountResult setUid(String value);

	/**
	 * The login credentials to use for the newly created account.
	 */
	String getSecret();

	/**
	 * @see #getSecret()
	 */
	de.haumacher.wizard.msg.CreateAccountResult setSecret(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.CreateAccountResult readCreateAccountResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.CreateAccountResult_Impl result = new de.haumacher.wizard.msg.impl.CreateAccountResult_Impl();
		result.readContent(in);
		return result;
	}

}
