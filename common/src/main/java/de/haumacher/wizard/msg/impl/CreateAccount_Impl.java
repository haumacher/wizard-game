package de.haumacher.wizard.msg.impl;

/**
 * Command requesting the creation of a new account. Result is either {@link CreateAccountResult} on success, or {@link Error} on error.
 */
public class CreateAccount_Impl extends de.haumacher.wizard.msg.impl.LoginCmd_Impl implements de.haumacher.wizard.msg.CreateAccount {

	private String _nickname = "";

	/**
	 * Creates a {@link CreateAccount_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.CreateAccount#create()
	 */
	public CreateAccount_Impl() {
		super();
	}

	@Override
	public final String getNickname() {
		return _nickname;
	}

	@Override
	public de.haumacher.wizard.msg.CreateAccount setNickname(String value) {
		internalSetNickname(value);
		return this;
	}

	/** Internal setter for {@link #getNickname()} without chain call utility. */
	protected final void internalSetNickname(String value) {
		_nickname = value;
	}

	@Override
	public String jsonType() {
		return CREATE_ACCOUNT__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(NICKNAME__PROP);
		out.value(getNickname());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case NICKNAME__PROP: setNickname(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
