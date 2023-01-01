package de.haumacher.wizard.msg;

/**
 * The first method sent after opening a connection.
 */
public interface Hello extends LoginCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Hello} instance.
	 */
	static de.haumacher.wizard.msg.Hello create() {
		return new de.haumacher.wizard.msg.impl.Hello_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Hello} type in JSON format. */
	String HELLO__TYPE = "Hello";

	/** @see #getVersion() */
	String VERSION__PROP = "version";

	/** @see #getLanguage() */
	String LANGUAGE__PROP = "language";

	/**
	 * The protocol version of the connecting client. This can be used to detect version mismatch of client and server.
	 */
	int getVersion();

	/**
	 * @see #getVersion()
	 */
	de.haumacher.wizard.msg.Hello setVersion(int value);

	/**
	 * The language the server should talk to the user.
	 */
	String getLanguage();

	/**
	 * @see #getLanguage()
	 */
	de.haumacher.wizard.msg.Hello setLanguage(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Hello readHello(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Hello_Impl result = new de.haumacher.wizard.msg.impl.Hello_Impl();
		result.readContent(in);
		return result;
	}

}
