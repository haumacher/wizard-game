package de.haumacher.wizard.msg;

/**
 * Command that verifies the e-mail address added to an account with {@link AddEmail}.
 */
public class VerifyEmail extends LoginCmd {

	/**
	 * Creates a {@link VerifyEmail} instance.
	 */
	public static VerifyEmail create() {
		return new VerifyEmail();
	}

	/** Identifier for the {@link VerifyEmail} type in JSON format. */
	public static final String VERIFY_EMAIL__TYPE = "VerifyEmail";

	/** @see #getUid() */
	private static final String UID = "uid";

	/** @see #getToken() */
	private static final String TOKEN = "token";

	private String _uid = "";

	private String _token = "";

	/**
	 * Creates a {@link VerifyEmail} instance.
	 *
	 * @see #create()
	 */
	protected VerifyEmail() {
		super();
	}

	/**
	 * The user ID, see {@link CreateAccountResult#getUid()}.
	 */
	public final String getUid() {
		return _uid;
	}

	/**
	 * @see #getUid()
	 */
	public VerifyEmail setUid(String value) {
		internalSetUid(value);
		return this;
	}
	/** Internal setter for {@link #getUid()} without chain call utility. */
	protected final void internalSetUid(String value) {
		_uid = value;
	}


	/**
	 * The verification token that was sent to the e-mail address.
	 */
	public final String getToken() {
		return _token;
	}

	/**
	 * @see #getToken()
	 */
	public VerifyEmail setToken(String value) {
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

	/** Reads a new instance from the given reader. */
	public static VerifyEmail readVerifyEmail(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		VerifyEmail result = new VerifyEmail();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(UID);
		out.value(getUid());
		out.name(TOKEN);
		out.value(getToken());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case TOKEN: setToken(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
