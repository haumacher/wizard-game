package de.haumacher.wizard.msg.impl;

/**
 * First message that must be sent after connecting to a game server.
 *
 * <p>
 * On success, a {@link Welcome} message is sent back.
 * </p>
 */
public class Login_Impl extends de.haumacher.wizard.msg.impl.LoginCmd_Impl implements de.haumacher.wizard.msg.Login {

	private String _uid = "";

	private String _secret = "";

	/**
	 * Creates a {@link Login_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Login#create()
	 */
	public Login_Impl() {
		super();
	}

	@Override
	public final String getUid() {
		return _uid;
	}

	@Override
	public de.haumacher.wizard.msg.Login setUid(String value) {
		internalSetUid(value);
		return this;
	}

	/** Internal setter for {@link #getUid()} without chain call utility. */
	protected final void internalSetUid(String value) {
		_uid = value;
	}

	@Override
	public final String getSecret() {
		return _secret;
	}

	@Override
	public de.haumacher.wizard.msg.Login setSecret(String value) {
		internalSetSecret(value);
		return this;
	}

	/** Internal setter for {@link #getSecret()} without chain call utility. */
	protected final void internalSetSecret(String value) {
		_secret = value;
	}

	@Override
	public String jsonType() {
		return LOGIN__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(UID__PROP);
		out.value(getUid());
		out.name(SECRET__PROP);
		out.value(getSecret());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID__PROP: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case SECRET__PROP: setSecret(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
