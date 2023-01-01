package de.haumacher.wizard.msg;

/**
 * Acknowledges the e-mail verification.
 */
public interface VerifyEmailSuccess extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.VerifyEmailSuccess} instance.
	 */
	static de.haumacher.wizard.msg.VerifyEmailSuccess create() {
		return new de.haumacher.wizard.msg.impl.VerifyEmailSuccess_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.VerifyEmailSuccess} type in JSON format. */
	String VERIFY_EMAIL_SUCCESS__TYPE = "VerifyEmailSuccess";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.VerifyEmailSuccess readVerifyEmailSuccess(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.VerifyEmailSuccess_Impl result = new de.haumacher.wizard.msg.impl.VerifyEmailSuccess_Impl();
		result.readContent(in);
		return result;
	}

}
