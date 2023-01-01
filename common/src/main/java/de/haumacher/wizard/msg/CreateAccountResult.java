package de.haumacher.wizard.msg;

/**
 * Result of {@link CreateAccount}, if successful.
 */
public class CreateAccountResult extends ResultMsg {

	/**
	 * Creates a {@link CreateAccountResult} instance.
	 */
	public static CreateAccountResult create() {
		return new de.haumacher.wizard.msg.CreateAccountResult();
	}

	/** Identifier for the {@link CreateAccountResult} type in JSON format. */
	public static final String CREATE_ACCOUNT_RESULT__TYPE = "CreateAccountResult";

	/** @see #getUid() */
	private static final String UID__PROP = "uid";

	/** @see #getSecret() */
	private static final String SECRET__PROP = "secret";

	private String _uid = "";

	private String _secret = "";

	/**
	 * Creates a {@link CreateAccountResult} instance.
	 *
	 * @see CreateAccountResult#create()
	 */
	protected CreateAccountResult() {
		super();
	}

	/**
	 * The user ID of the newly created account.
	 */
	public final String getUid() {
		return _uid;
	}

	/**
	 * @see #getUid()
	 */
	public CreateAccountResult setUid(String value) {
		internalSetUid(value);
		return this;
	}

	/** Internal setter for {@link #getUid()} without chain call utility. */
	protected final void internalSetUid(String value) {
		_uid = value;
	}

	/**
	 * The login credentials to use for the newly created account.
	 */
	public final String getSecret() {
		return _secret;
	}

	/**
	 * @see #getSecret()
	 */
	public CreateAccountResult setSecret(String value) {
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

	/** Reads a new instance from the given reader. */
	public static CreateAccountResult readCreateAccountResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.CreateAccountResult result = new de.haumacher.wizard.msg.CreateAccountResult();
		result.readContent(in);
		return result;
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
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
