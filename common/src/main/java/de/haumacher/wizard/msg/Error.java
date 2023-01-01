package de.haumacher.wizard.msg;

/**
 * Informs the client that the last {@link Cmd} failed.
 */
public class Error extends ResultMsg {

	/**
	 * Creates a {@link Error} instance.
	 */
	public static Error create() {
		return new de.haumacher.wizard.msg.Error();
	}

	/** Identifier for the {@link Error} type in JSON format. */
	public static final String ERROR__TYPE = "Error";

	/** @see #getMessage() */
	private static final String MESSAGE__PROP = "message";

	private String _message = "";

	/**
	 * Creates a {@link Error} instance.
	 *
	 * @see Error#create()
	 */
	protected Error() {
		super();
	}

	/**
	 * Description of the error that has been detected.
	 */
	public final String getMessage() {
		return _message;
	}

	/**
	 * @see #getMessage()
	 */
	public Error setMessage(String value) {
		internalSetMessage(value);
		return this;
	}

	/** Internal setter for {@link #getMessage()} without chain call utility. */
	protected final void internalSetMessage(String value) {
		_message = value;
	}

	@Override
	public String jsonType() {
		return ERROR__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Error readError(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Error result = new de.haumacher.wizard.msg.Error();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(MESSAGE__PROP);
		out.value(getMessage());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case MESSAGE__PROP: setMessage(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
