package de.haumacher.wizard.server.db.model;

/**
 * Command requesting the creation of a new account. Result is either {@link CreateAccountResult} on success, or {@link Error} on error.
 */
public class CreateAccount extends LoginCmd {

	/**
	 * Creates a {@link CreateAccount} instance.
	 */
	public static CreateAccount create() {
		return new CreateAccount();
	}

	/** Identifier for the {@link CreateAccount} type in JSON format. */
	public static final String CREATE_ACCOUNT__TYPE = "CreateAccount";

	/** @see #getNickname() */
	public static final String NICKNAME = "nickname";

	/** @see #getLanguage() */
	public static final String LANGUAGE = "language";

	/** Identifier for the {@link CreateAccount} type in binary format. */
	public static final int CREATE_ACCOUNT__TYPE_ID = 1;

	/** Identifier for the property {@link #getNickname()} in binary format. */
	public static final int NICKNAME__ID = 1;

	/** Identifier for the property {@link #getLanguage()} in binary format. */
	public static final int LANGUAGE__ID = 2;

	private String _nickname = "";

	private String _language = "";

	/**
	 * Creates a {@link CreateAccount} instance.
	 *
	 * @see #create()
	 */
	protected CreateAccount() {
		super();
	}

	@Override
	public TypeKind kind() {
		return TypeKind.CREATE_ACCOUNT;
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
		_listener.beforeSet(this, NICKNAME, value);
		_nickname = value;
	}


	/**
	 * The language the server should talk to the user.
	 */
	public final String getLanguage() {
		return _language;
	}

	/**
	 * @see #getLanguage()
	 */
	public CreateAccount setLanguage(String value) {
		internalSetLanguage(value);
		return this;
	}
	/** Internal setter for {@link #getLanguage()} without chain call utility. */
	protected final void internalSetLanguage(String value) {
		_listener.beforeSet(this, LANGUAGE, value);
		_language = value;
	}


	@Override
	public String jsonType() {
		return CREATE_ACCOUNT__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			NICKNAME, 
			LANGUAGE));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case NICKNAME: return getNickname();
			case LANGUAGE: return getLanguage();
			default: return super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case NICKNAME: setNickname((String) value); break;
			case LANGUAGE: setLanguage((String) value); break;
			default: super.set(field, value); break;
		}
	}

	/** Reads a new instance from the given reader. */
	public static CreateAccount readCreateAccount(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		CreateAccount result = new CreateAccount();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(NICKNAME);
		out.value(getNickname());
		out.name(LANGUAGE);
		out.value(getLanguage());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case NICKNAME: setNickname(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case LANGUAGE: setLanguage(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public int typeId() {
		return CREATE_ACCOUNT__TYPE_ID;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(NICKNAME__ID);
		out.value(getNickname());
		out.name(LANGUAGE__ID);
		out.value(getLanguage());
	}

	/** Reads a new instance from the given reader. */
	public static CreateAccount readCreateAccount(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		CreateAccount result = new CreateAccount();
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
			case NICKNAME__ID: setNickname(in.nextString()); break;
			case LANGUAGE__ID: setLanguage(in.nextString()); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
