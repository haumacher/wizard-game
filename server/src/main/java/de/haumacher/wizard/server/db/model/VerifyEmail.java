package de.haumacher.wizard.server.db.model;

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
	public static final String UID = "uid";

	/** @see #getSecret() */
	public static final String SECRET = "secret";

	/** @see #getToken() */
	public static final String TOKEN = "token";

	/** Identifier for the {@link VerifyEmail} type in binary format. */
	public static final int VERIFY_EMAIL__TYPE_ID = 3;

	/** Identifier for the property {@link #getUid()} in binary format. */
	public static final int UID__ID = 1;

	/** Identifier for the property {@link #getSecret()} in binary format. */
	public static final int SECRET__ID = 2;

	/** Identifier for the property {@link #getToken()} in binary format. */
	public static final int TOKEN__ID = 3;

	private String _uid = "";

	private String _secret = "";

	private String _token = "";

	/**
	 * Creates a {@link VerifyEmail} instance.
	 *
	 * @see #create()
	 */
	protected VerifyEmail() {
		super();
	}

	@Override
	public TypeKind kind() {
		return TypeKind.VERIFY_EMAIL;
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
		_listener.beforeSet(this, UID, value);
		_uid = value;
	}


	/**
	 * The user's login credentials, see {@link CreateAccountResult#getSecret()}.
	 */
	public final String getSecret() {
		return _secret;
	}

	/**
	 * @see #getSecret()
	 */
	public VerifyEmail setSecret(String value) {
		internalSetSecret(value);
		return this;
	}
	/** Internal setter for {@link #getSecret()} without chain call utility. */
	protected final void internalSetSecret(String value) {
		_listener.beforeSet(this, SECRET, value);
		_secret = value;
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
		_listener.beforeSet(this, TOKEN, value);
		_token = value;
	}


	@Override
	public String jsonType() {
		return VERIFY_EMAIL__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			UID, 
			SECRET, 
			TOKEN));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case UID: return getUid();
			case SECRET: return getSecret();
			case TOKEN: return getToken();
			default: return super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case UID: setUid((String) value); break;
			case SECRET: setSecret((String) value); break;
			case TOKEN: setToken((String) value); break;
			default: super.set(field, value); break;
		}
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
		out.name(SECRET);
		out.value(getSecret());
		out.name(TOKEN);
		out.value(getToken());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case SECRET: setSecret(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case TOKEN: setToken(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public int typeId() {
		return VERIFY_EMAIL__TYPE_ID;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(UID__ID);
		out.value(getUid());
		out.name(SECRET__ID);
		out.value(getSecret());
		out.name(TOKEN__ID);
		out.value(getToken());
	}

	/** Reads a new instance from the given reader. */
	public static VerifyEmail readVerifyEmail(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		VerifyEmail result = new VerifyEmail();
		while (in.hasNext()) {
			int field = in.nextName();
			result.readField(in, field);
		}
		in.endObject();
		return result;
	}

	@Override
	protected void readField(de.haumacher.msgbuf.binary.DataReader in, int field) throws java.io.IOException {
		switch (field) {
			case UID__ID: setUid(in.nextString()); break;
			case SECRET__ID: setSecret(in.nextString()); break;
			case TOKEN__ID: setToken(in.nextString()); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
