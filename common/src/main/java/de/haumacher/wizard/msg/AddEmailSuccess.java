package de.haumacher.wizard.msg;

/**
 * Acknowledges adding the e-mail.
 */
public interface AddEmailSuccess extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.AddEmailSuccess} instance.
	 */
	static de.haumacher.wizard.msg.AddEmailSuccess create() {
		return new de.haumacher.wizard.msg.impl.AddEmailSuccess_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.AddEmailSuccess} type in JSON format. */
	String ADD_EMAIL_SUCCESS__TYPE = "AddEmailSuccess";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.AddEmailSuccess readAddEmailSuccess(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.AddEmailSuccess_Impl result = new de.haumacher.wizard.msg.impl.AddEmailSuccess_Impl();
		result.readContent(in);
		return result;
	}

}
