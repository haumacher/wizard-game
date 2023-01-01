package de.haumacher.wizard.msg.impl;

/**
 * Command to assign an email to an existing account. An e-mail is send to the provided address with a verification token that must be provided to {@link VerifyEmail} in a following request.
 */
public class AddEmail_Impl extends de.haumacher.wizard.msg.impl.LoginCmd_Impl implements de.haumacher.wizard.msg.AddEmail {

	private String _uid = "";

	private String _secret = "";

	private String _email = "";

	/**
	 * Creates a {@link AddEmail_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.AddEmail#create()
	 */
	public AddEmail_Impl() {
		super();
	}

	@Override
	public final String getUid() {
		return _uid;
	}

	@Override
	public de.haumacher.wizard.msg.AddEmail setUid(String value) {
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
	public de.haumacher.wizard.msg.AddEmail setSecret(String value) {
		internalSetSecret(value);
		return this;
	}

	/** Internal setter for {@link #getSecret()} without chain call utility. */
	protected final void internalSetSecret(String value) {
		_secret = value;
	}

	@Override
	public final String getEmail() {
		return _email;
	}

	@Override
	public de.haumacher.wizard.msg.AddEmail setEmail(String value) {
		internalSetEmail(value);
		return this;
	}

	/** Internal setter for {@link #getEmail()} without chain call utility. */
	protected final void internalSetEmail(String value) {
		_email = value;
	}

	@Override
	public String jsonType() {
		return ADD_EMAIL__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(UID__PROP);
		out.value(getUid());
		out.name(SECRET__PROP);
		out.value(getSecret());
		out.name(EMAIL__PROP);
		out.value(getEmail());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID__PROP: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case SECRET__PROP: setSecret(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case EMAIL__PROP: setEmail(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
