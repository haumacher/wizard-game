package de.haumacher.wizard.msg;

/**
 * Answer to {@link Hello}.
 */
public interface HelloResult extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.HelloResult} instance.
	 */
	static de.haumacher.wizard.msg.HelloResult create() {
		return new de.haumacher.wizard.msg.impl.HelloResult_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.HelloResult} type in JSON format. */
	String HELLO_RESULT__TYPE = "HelloResult";

	/** @see #isOk() */
	String OK__PROP = "ok";

	/** @see #getVersion() */
	String VERSION__PROP = "version";

	/** @see #getMsg() */
	String MSG__PROP = "msg";

	/**
	 * Whether protocol version matches and login can continue.
	 */
	boolean isOk();

	/**
	 * @see #isOk()
	 */
	de.haumacher.wizard.msg.HelloResult setOk(boolean value);

	/**
	 * The protocol version of the server.
	 */
	int getVersion();

	/**
	 * @see #getVersion()
	 */
	de.haumacher.wizard.msg.HelloResult setVersion(int value);

	/**
	 * Description of the problem, if not ok.
	 */
	String getMsg();

	/**
	 * @see #getMsg()
	 */
	de.haumacher.wizard.msg.HelloResult setMsg(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.HelloResult readHelloResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.HelloResult_Impl result = new de.haumacher.wizard.msg.impl.HelloResult_Impl();
		result.readContent(in);
		return result;
	}

}
