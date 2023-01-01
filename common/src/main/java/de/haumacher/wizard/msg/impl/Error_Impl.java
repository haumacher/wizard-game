package de.haumacher.wizard.msg.impl;

/**
 * Informs the client that the last {@link Cmd} failed.
 */
public class Error_Impl extends de.haumacher.wizard.msg.impl.ResultMsg_Impl implements de.haumacher.wizard.msg.Error {

	private String _message = "";

	/**
	 * Creates a {@link Error_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Error#create()
	 */
	public Error_Impl() {
		super();
	}

	@Override
	public final String getMessage() {
		return _message;
	}

	@Override
	public de.haumacher.wizard.msg.Error setMessage(String value) {
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
