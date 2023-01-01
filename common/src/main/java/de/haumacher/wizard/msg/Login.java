package de.haumacher.wizard.msg;

/**
 * First message that must be sent after connecting to a game server.
 *
 * <p>
 * On success, a {@link Welcome} message is sent back.
 * </p>
 */
public interface Login extends LoginCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Login} instance.
	 */
	static de.haumacher.wizard.msg.Login create() {
		return new de.haumacher.wizard.msg.impl.Login_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Login} type in JSON format. */
	String LOGIN__TYPE = "Login";

	/** @see #getUid() */
	String UID__PROP = "uid";

	/** @see #getSecret() */
	String SECRET__PROP = "secret";

	/**
	 * The UID of the player.
	 */
	String getUid();

	/**
	 * @see #getUid()
	 */
	de.haumacher.wizard.msg.Login setUid(String value);

	/**
	 * The user's login secret.
	 */
	String getSecret();

	/**
	 * @see #getSecret()
	 */
	de.haumacher.wizard.msg.Login setSecret(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Login readLogin(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Login_Impl result = new de.haumacher.wizard.msg.impl.Login_Impl();
		result.readContent(in);
		return result;
	}

}
