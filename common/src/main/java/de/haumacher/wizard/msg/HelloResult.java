package de.haumacher.wizard.msg;

/**
 * Answer to {@link Hello}.
 */
public class HelloResult extends ResultMsg {

	/**
	 * Creates a {@link HelloResult} instance.
	 */
	public static HelloResult create() {
		return new de.haumacher.wizard.msg.HelloResult();
	}

	/** Identifier for the {@link HelloResult} type in JSON format. */
	public static final String HELLO_RESULT__TYPE = "HelloResult";

	/** @see #isOk() */
	private static final String OK__PROP = "ok";

	/** @see #getVersion() */
	private static final String VERSION__PROP = "version";

	/** @see #getMsg() */
	private static final String MSG__PROP = "msg";

	private boolean _ok = false;

	private int _version = 0;

	private String _msg = "";

	/**
	 * Creates a {@link HelloResult} instance.
	 *
	 * @see HelloResult#create()
	 */
	protected HelloResult() {
		super();
	}

	/**
	 * Whether protocol version matches and login can continue.
	 */
	public final boolean isOk() {
		return _ok;
	}

	/**
	 * @see #isOk()
	 */
	public HelloResult setOk(boolean value) {
		internalSetOk(value);
		return this;
	}

	/** Internal setter for {@link #isOk()} without chain call utility. */
	protected final void internalSetOk(boolean value) {
		_ok = value;
	}

	/**
	 * The protocol version of the server.
	 */
	public final int getVersion() {
		return _version;
	}

	/**
	 * @see #getVersion()
	 */
	public HelloResult setVersion(int value) {
		internalSetVersion(value);
		return this;
	}

	/** Internal setter for {@link #getVersion()} without chain call utility. */
	protected final void internalSetVersion(int value) {
		_version = value;
	}

	/**
	 * Description of the problem, if not ok.
	 */
	public final String getMsg() {
		return _msg;
	}

	/**
	 * @see #getMsg()
	 */
	public HelloResult setMsg(String value) {
		internalSetMsg(value);
		return this;
	}

	/** Internal setter for {@link #getMsg()} without chain call utility. */
	protected final void internalSetMsg(String value) {
		_msg = value;
	}

	@Override
	public String jsonType() {
		return HELLO_RESULT__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static HelloResult readHelloResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.HelloResult result = new de.haumacher.wizard.msg.HelloResult();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(OK__PROP);
		out.value(isOk());
		out.name(VERSION__PROP);
		out.value(getVersion());
		out.name(MSG__PROP);
		out.value(getMsg());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case OK__PROP: setOk(in.nextBoolean()); break;
			case VERSION__PROP: setVersion(in.nextInt()); break;
			case MSG__PROP: setMsg(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
