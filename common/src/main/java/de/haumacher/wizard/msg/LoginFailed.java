package de.haumacher.wizard.msg;

/**
 * Sent, when {@link Login} is not successful.
 */
public class LoginFailed extends ResultMsg {

	/**
	 * Creates a {@link LoginFailed} instance.
	 */
	public static LoginFailed create() {
		return new de.haumacher.wizard.msg.LoginFailed();
	}

	/** Identifier for the {@link LoginFailed} type in JSON format. */
	public static final String LOGIN_FAILED__TYPE = "LoginFailed";

	/** @see #getMsg() */
	private static final String MSG__PROP = "msg";

	private String _msg = "";

	/**
	 * Creates a {@link LoginFailed} instance.
	 *
	 * @see LoginFailed#create()
	 */
	protected LoginFailed() {
		super();
	}

	/**
	 * Further description of the problem
	 */
	public final String getMsg() {
		return _msg;
	}

	/**
	 * @see #getMsg()
	 */
	public LoginFailed setMsg(String value) {
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

	/** Reads a new instance from the given reader. */
	public static LoginFailed readLoginFailed(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.LoginFailed result = new de.haumacher.wizard.msg.LoginFailed();
		result.readContent(in);
		return result;
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
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
