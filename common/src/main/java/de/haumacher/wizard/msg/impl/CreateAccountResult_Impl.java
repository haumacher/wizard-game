package de.haumacher.wizard.msg.impl;

/**
 * Result of {@link CreateAccount}, if successful.
 */
public class CreateAccountResult_Impl extends de.haumacher.wizard.msg.impl.ResultMsg_Impl implements de.haumacher.wizard.msg.CreateAccountResult {

	private String _uid = "";

	private String _secret = "";

	/**
	 * Creates a {@link CreateAccountResult_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.CreateAccountResult#create()
	 */
	public CreateAccountResult_Impl() {
		super();
	}

	@Override
	public final String getUid() {
		return _uid;
	}

	@Override
	public de.haumacher.wizard.msg.CreateAccountResult setUid(String value) {
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
	public de.haumacher.wizard.msg.CreateAccountResult setSecret(String value) {
		internalSetSecret(value);
		return this;
	}

	/** Internal setter for {@link #getSecret()} without chain call utility. */
	protected final void internalSetSecret(String value) {
		_secret = value;
	}

	@Override
	public String jsonType() {
		return CREATE_ACCOUNT_RESULT__TYPE;
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
