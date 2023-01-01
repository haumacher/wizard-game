package de.haumacher.wizard.msg;

/**
 * Acknowledges the e-mail verification.
 */
public class VerifyEmailSuccess extends ResultMsg {

	/**
	 * Creates a {@link VerifyEmailSuccess} instance.
	 */
	public static VerifyEmailSuccess create() {
		return new de.haumacher.wizard.msg.VerifyEmailSuccess();
	}

	/** Identifier for the {@link VerifyEmailSuccess} type in JSON format. */
	public static final String VERIFY_EMAIL_SUCCESS__TYPE = "VerifyEmailSuccess";

	/**
	 * Creates a {@link VerifyEmailSuccess} instance.
	 *
	 * @see VerifyEmailSuccess#create()
	 */
	protected VerifyEmailSuccess() {
		super();
	}

	@Override
	public String jsonType() {
		return VERIFY_EMAIL_SUCCESS__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static VerifyEmailSuccess readVerifyEmailSuccess(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.VerifyEmailSuccess result = new de.haumacher.wizard.msg.VerifyEmailSuccess();
		result.readContent(in);
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
