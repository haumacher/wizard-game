package de.haumacher.wizard.msg;

/**
 * Acknowledges adding the e-mail.
 */
public class AddEmailSuccess extends ResultMsg {

	/**
	 * Creates a {@link AddEmailSuccess} instance.
	 */
	public static AddEmailSuccess create() {
		return new AddEmailSuccess();
	}

	/** Identifier for the {@link AddEmailSuccess} type in JSON format. */
	public static final String ADD_EMAIL_SUCCESS__TYPE = "AddEmailSuccess";

	/**
	 * Creates a {@link AddEmailSuccess} instance.
	 *
	 * @see #create()
	 */
	protected AddEmailSuccess() {
		super();
	}

	@Override
	public String jsonType() {
		return ADD_EMAIL_SUCCESS__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static AddEmailSuccess readAddEmailSuccess(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		AddEmailSuccess result = new AddEmailSuccess();
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
