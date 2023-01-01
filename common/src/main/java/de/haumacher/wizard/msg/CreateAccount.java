package de.haumacher.wizard.msg;

/**
 * Command requesting the creation of a new account. Result is either {@link CreateAccountResult} on success, or {@link Error} on error.
 */
public class CreateAccount extends LoginCmd {

	/**
	 * Creates a {@link CreateAccount} instance.
	 */
	public static CreateAccount create() {
		return new de.haumacher.wizard.msg.CreateAccount();
	}

	/** Identifier for the {@link CreateAccount} type in JSON format. */
	public static final String CREATE_ACCOUNT__TYPE = "CreateAccount";

	/** @see #getNickname() */
	private static final String NICKNAME__PROP = "nickname";

	private String _nickname = "";

	/**
	 * Creates a {@link CreateAccount} instance.
	 *
	 * @see CreateAccount#create()
	 */
	protected CreateAccount() {
		super();
	}

	/**
	 * The nickname for the newly created account.
	 */
	public final String getNickname() {
		return _nickname;
	}

	/**
	 * @see #getNickname()
	 */
	public CreateAccount setNickname(String value) {
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

	/** Reads a new instance from the given reader. */
	public static CreateAccount readCreateAccount(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.CreateAccount result = new de.haumacher.wizard.msg.CreateAccount();
		result.readContent(in);
		return result;
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
	public <R,A,E extends Throwable> R visit(LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
