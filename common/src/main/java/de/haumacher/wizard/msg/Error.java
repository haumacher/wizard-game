package de.haumacher.wizard.msg;

/**
 * Informs the client that the last {@link Cmd} failed.
 */
public interface Error extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Error} instance.
	 */
	static de.haumacher.wizard.msg.Error create() {
		return new de.haumacher.wizard.msg.impl.Error_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Error} type in JSON format. */
	String ERROR__TYPE = "Error";

	/** @see #getMessage() */
	String MESSAGE__PROP = "message";

	/**
	 * Description of the error that has been detected.
	 */
	String getMessage();

	/**
	 * @see #getMessage()
	 */
	de.haumacher.wizard.msg.Error setMessage(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Error readError(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Error_Impl result = new de.haumacher.wizard.msg.impl.Error_Impl();
		result.readContent(in);
		return result;
	}

}
