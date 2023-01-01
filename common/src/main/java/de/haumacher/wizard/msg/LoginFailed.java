package de.haumacher.wizard.msg;

/**
 * Sent, when {@link Login} is not successful.
 */
public interface LoginFailed extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.LoginFailed} instance.
	 */
	static de.haumacher.wizard.msg.LoginFailed create() {
		return new de.haumacher.wizard.msg.impl.LoginFailed_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.LoginFailed} type in JSON format. */
	String LOGIN_FAILED__TYPE = "LoginFailed";

	/** @see #getMsg() */
	String MSG__PROP = "msg";

	/**
	 * Further description of the problem
	 */
	String getMsg();

	/**
	 * @see #getMsg()
	 */
	de.haumacher.wizard.msg.LoginFailed setMsg(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.LoginFailed readLoginFailed(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.LoginFailed_Impl result = new de.haumacher.wizard.msg.impl.LoginFailed_Impl();
		result.readContent(in);
		return result;
	}

}
