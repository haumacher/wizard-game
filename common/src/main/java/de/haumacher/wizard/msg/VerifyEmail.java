package de.haumacher.wizard.msg;

/**
 * Command that verifies the e-mail address added to an account with {@link AddEmail}.
 */
public interface VerifyEmail extends LoginCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.VerifyEmail} instance.
	 */
	static de.haumacher.wizard.msg.VerifyEmail create() {
		return new de.haumacher.wizard.msg.impl.VerifyEmail_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.VerifyEmail} type in JSON format. */
	String VERIFY_EMAIL__TYPE = "VerifyEmail";

	/** @see #getUid() */
	String UID__PROP = "uid";

	/** @see #getToken() */
	String TOKEN__PROP = "token";

	/**
	 * The user ID, see {@link CreateAccountResult#getUid()}.
	 */
	String getUid();

	/**
	 * @see #getUid()
	 */
	de.haumacher.wizard.msg.VerifyEmail setUid(String value);

	/**
	 * The verification token that was sent to the e-mail address.
	 */
	String getToken();

	/**
	 * @see #getToken()
	 */
	de.haumacher.wizard.msg.VerifyEmail setToken(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.VerifyEmail readVerifyEmail(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.VerifyEmail_Impl result = new de.haumacher.wizard.msg.impl.VerifyEmail_Impl();
		result.readContent(in);
		return result;
	}

}
