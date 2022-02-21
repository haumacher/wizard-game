package de.haumacher.wizard.msg;

/**
 * Acknowledges the e-mail verification.
 */
public class VerifyEmailSuccess extends ResultMsg {

	/**
	 * Creates a {@link VerifyEmailSuccess} instance.
	 */
	public static VerifyEmailSuccess create() {
		return new VerifyEmailSuccess();
	}

	/** Identifier for the {@link VerifyEmailSuccess} type in JSON format. */
	public static final String VERIFY_EMAIL_SUCCESS__TYPE = "VerifyEmailSuccess";

	/**
	 * Creates a {@link VerifyEmailSuccess} instance.
	 *
	 * @see #create()
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
		VerifyEmailSuccess result = new VerifyEmailSuccess();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
