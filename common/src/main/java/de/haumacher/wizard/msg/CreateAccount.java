package de.haumacher.wizard.msg;

/**
 * Command requesting the creation of a new account. Result is either {@link CreateAccountResult} on success, or {@link Error} on error.
 */
public interface CreateAccount extends LoginCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.CreateAccount} instance.
	 */
	static de.haumacher.wizard.msg.CreateAccount create() {
		return new de.haumacher.wizard.msg.impl.CreateAccount_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.CreateAccount} type in JSON format. */
	String CREATE_ACCOUNT__TYPE = "CreateAccount";

	/** @see #getNickname() */
	String NICKNAME__PROP = "nickname";

	/**
	 * The nickname for the newly created account.
	 */
	String getNickname();

	/**
	 * @see #getNickname()
	 */
	de.haumacher.wizard.msg.CreateAccount setNickname(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.CreateAccount readCreateAccount(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.CreateAccount_Impl result = new de.haumacher.wizard.msg.impl.CreateAccount_Impl();
		result.readContent(in);
		return result;
	}

}
