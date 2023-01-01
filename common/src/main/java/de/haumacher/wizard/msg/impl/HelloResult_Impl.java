package de.haumacher.wizard.msg.impl;

/**
 * Answer to {@link Hello}.
 */
public class HelloResult_Impl extends de.haumacher.wizard.msg.impl.ResultMsg_Impl implements de.haumacher.wizard.msg.HelloResult {

	private boolean _ok = false;

	private int _version = 0;

	private String _msg = "";

	/**
	 * Creates a {@link HelloResult_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.HelloResult#create()
	 */
	public HelloResult_Impl() {
		super();
	}

	@Override
	public final boolean isOk() {
		return _ok;
	}

	@Override
	public de.haumacher.wizard.msg.HelloResult setOk(boolean value) {
		internalSetOk(value);
		return this;
	}

	/** Internal setter for {@link #isOk()} without chain call utility. */
	protected final void internalSetOk(boolean value) {
		_ok = value;
	}

	@Override
	public final int getVersion() {
		return _version;
	}

	@Override
	public de.haumacher.wizard.msg.HelloResult setVersion(int value) {
		internalSetVersion(value);
		return this;
	}

	/** Internal setter for {@link #getVersion()} without chain call utility. */
	protected final void internalSetVersion(int value) {
		_version = value;
	}

	@Override
	public final String getMsg() {
		return _msg;
	}

	@Override
	public de.haumacher.wizard.msg.HelloResult setMsg(String value) {
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
