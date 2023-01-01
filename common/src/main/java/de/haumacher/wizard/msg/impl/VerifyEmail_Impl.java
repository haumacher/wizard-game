package de.haumacher.wizard.msg.impl;

/**
 * Command that verifies the e-mail address added to an account with {@link AddEmail}.
 */
public class VerifyEmail_Impl extends de.haumacher.wizard.msg.impl.LoginCmd_Impl implements de.haumacher.wizard.msg.VerifyEmail {

	private String _uid = "";

	private String _token = "";

	/**
	 * Creates a {@link VerifyEmail_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.VerifyEmail#create()
	 */
	public VerifyEmail_Impl() {
		super();
	}

	@Override
	public final String getUid() {
		return _uid;
	}

	@Override
	public de.haumacher.wizard.msg.VerifyEmail setUid(String value) {
		internalSetUid(value);
		return this;
	}

	/** Internal setter for {@link #getUid()} without chain call utility. */
	protected final void internalSetUid(String value) {
		_uid = value;
	}

	@Override
	public final String getToken() {
		return _token;
	}

	@Override
	public de.haumacher.wizard.msg.VerifyEmail setToken(String value) {
		internalSetToken(value);
		return this;
	}

	/** Internal setter for {@link #getToken()} without chain call utility. */
	protected final void internalSetToken(String value) {
		_token = value;
	}

	@Override
	public String jsonType() {
		return VERIFY_EMAIL__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(UID__PROP);
		out.value(getUid());
		out.name(TOKEN__PROP);
		out.value(getToken());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID__PROP: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case TOKEN__PROP: setToken(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
