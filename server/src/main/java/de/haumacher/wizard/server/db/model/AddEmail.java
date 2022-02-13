package de.haumacher.wizard.server.db.model;

/**
 * Command to assign an email to an existing account. An e-mail is send to the provided address with a verification token that must be provided to {@link VerifyEmail} in a following request.
 */
public class AddEmail extends LoginCmd {

	/**
	 * Creates a {@link AddEmail} instance.
	 */
	public static AddEmail create() {
		return new AddEmail();
	}

	/** Identifier for the {@link AddEmail} type in JSON format. */
	public static final String ADD_EMAIL__TYPE = "AddEmail";

	/** @see #getUid() */
	public static final String UID = "uid";

	/** @see #getSecret() */
	public static final String SECRET = "secret";

	/** @see #getEmail() */
	public static final String EMAIL = "email";

	/** Identifier for the {@link AddEmail} type in binary format. */
	public static final int ADD_EMAIL__TYPE_ID = 2;

	/** Identifier for the property {@link #getUid()} in binary format. */
	public static final int UID__ID = 1;

	/** Identifier for the property {@link #getSecret()} in binary format. */
	public static final int SECRET__ID = 2;

	/** Identifier for the property {@link #getEmail()} in binary format. */
	public static final int EMAIL__ID = 3;

	private String _uid = "";

	private String _secret = "";

	private String _email = "";

	/**
	 * Creates a {@link AddEmail} instance.
	 *
	 * @see #create()
	 */
	protected AddEmail() {
		super();
	}

	@Override
	public TypeKind kind() {
		return TypeKind.ADD_EMAIL;
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
	public AddEmail setUid(String value) {
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
	public AddEmail setSecret(String value) {
		internalSetSecret(value);
		return this;
	}
	/** Internal setter for {@link #getSecret()} without chain call utility. */
	protected final void internalSetSecret(String value) {
		_listener.beforeSet(this, SECRET, value);
		_secret = value;
	}


	/**
	 * The e-mail address to assign to the user's account.
	 */
	public final String getEmail() {
		return _email;
	}

	/**
	 * @see #getEmail()
	 */
	public AddEmail setEmail(String value) {
		internalSetEmail(value);
		return this;
	}
	/** Internal setter for {@link #getEmail()} without chain call utility. */
	protected final void internalSetEmail(String value) {
		_listener.beforeSet(this, EMAIL, value);
		_email = value;
	}


	@Override
	public String jsonType() {
		return ADD_EMAIL__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			UID, 
			SECRET, 
			EMAIL));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case UID: return getUid();
			case SECRET: return getSecret();
			case EMAIL: return getEmail();
			default: return super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case UID: setUid((String) value); break;
			case SECRET: setSecret((String) value); break;
			case EMAIL: setEmail((String) value); break;
			default: super.set(field, value); break;
		}
	}

	/** Reads a new instance from the given reader. */
	public static AddEmail readAddEmail(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		AddEmail result = new AddEmail();
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
		out.name(EMAIL);
		out.value(getEmail());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case SECRET: setSecret(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case EMAIL: setEmail(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public int typeId() {
		return ADD_EMAIL__TYPE_ID;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(UID__ID);
		out.value(getUid());
		out.name(SECRET__ID);
		out.value(getSecret());
		out.name(EMAIL__ID);
		out.value(getEmail());
	}

	/** Reads a new instance from the given reader. */
	public static AddEmail readAddEmail(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		AddEmail result = new AddEmail();
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
			case EMAIL__ID: setEmail(in.nextString()); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
