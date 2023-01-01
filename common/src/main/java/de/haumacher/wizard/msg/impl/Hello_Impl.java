package de.haumacher.wizard.msg.impl;

/**
 * The first method sent after opening a connection.
 */
public class Hello_Impl extends de.haumacher.wizard.msg.impl.LoginCmd_Impl implements de.haumacher.wizard.msg.Hello {

	private int _version = 0;

	private String _language = "";

	/**
	 * Creates a {@link Hello_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Hello#create()
	 */
	public Hello_Impl() {
		super();
	}

	@Override
	public final int getVersion() {
		return _version;
	}

	@Override
	public de.haumacher.wizard.msg.Hello setVersion(int value) {
		internalSetVersion(value);
		return this;
	}

	/** Internal setter for {@link #getVersion()} without chain call utility. */
	protected final void internalSetVersion(int value) {
		_version = value;
	}

	@Override
	public final String getLanguage() {
		return _language;
	}

	@Override
	public de.haumacher.wizard.msg.Hello setLanguage(String value) {
		internalSetLanguage(value);
		return this;
	}

	/** Internal setter for {@link #getLanguage()} without chain call utility. */
	protected final void internalSetLanguage(String value) {
		_language = value;
	}

	@Override
	public String jsonType() {
		return HELLO__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(VERSION__PROP);
		out.value(getVersion());
		out.name(LANGUAGE__PROP);
		out.value(getLanguage());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case VERSION__PROP: setVersion(in.nextInt()); break;
			case LANGUAGE__PROP: setLanguage(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
