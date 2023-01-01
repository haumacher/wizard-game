package de.haumacher.wizard.msg.impl;

/**
 * Sent, when {@link Login} is not successful.
 */
public class LoginFailed_Impl extends de.haumacher.wizard.msg.impl.ResultMsg_Impl implements de.haumacher.wizard.msg.LoginFailed {

	private String _msg = "";

	/**
	 * Creates a {@link LoginFailed_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.LoginFailed#create()
	 */
	public LoginFailed_Impl() {
		super();
	}

	@Override
	public final String getMsg() {
		return _msg;
	}

	@Override
	public de.haumacher.wizard.msg.LoginFailed setMsg(String value) {
		internalSetMsg(value);
		return this;
	}

	/** Internal setter for {@link #getMsg()} without chain call utility. */
	protected final void internalSetMsg(String value) {
		_msg = value;
	}

	@Override
	public String jsonType() {
		return LOGIN_FAILED__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(MSG__PROP);
		out.value(getMsg());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case MSG__PROP: setMsg(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
