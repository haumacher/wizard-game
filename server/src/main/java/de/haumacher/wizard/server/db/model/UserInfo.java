package de.haumacher.wizard.server.db.model;

/**
 * Description of a user account
 */
public class UserInfo extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.msgbuf.binary.BinaryDataObject, de.haumacher.msgbuf.observer.Observable {

	/**
	 * Creates a {@link UserInfo} instance.
	 */
	public static UserInfo create() {
		return new UserInfo();
	}

	/** Identifier for the {@link UserInfo} type in JSON format. */
	public static final String USER_INFO__TYPE = "UserInfo";

	/** @see #getNickname() */
	public static final String NICKNAME = "nickname";

	/** @see #getLanguage() */
	public static final String LANGUAGE = "language";

	/** Identifier for the property {@link #getNickname()} in binary format. */
	public static final int NICKNAME__ID = 1;

	/** Identifier for the property {@link #getLanguage()} in binary format. */
	public static final int LANGUAGE__ID = 2;

	private String _nickname = "";

	private String _language = "";

	/**
	 * Creates a {@link UserInfo} instance.
	 *
	 * @see #create()
	 */
	protected UserInfo() {
		super();
	}

	/**
	 * The nickname for the user.
	 */
	public final String getNickname() {
		return _nickname;
	}

	/**
	 * @see #getNickname()
	 */
	public UserInfo setNickname(String value) {
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
	public UserInfo setLanguage(String value) {
		internalSetLanguage(value);
		return this;
	}
	/** Internal setter for {@link #getLanguage()} without chain call utility. */
	protected final void internalSetLanguage(String value) {
		_listener.beforeSet(this, LANGUAGE, value);
		_language = value;
	}


	protected de.haumacher.msgbuf.observer.Listener _listener = de.haumacher.msgbuf.observer.Listener.NONE;

	@Override
	public UserInfo registerListener(de.haumacher.msgbuf.observer.Listener l) {
		internalRegisterListener(l);
		return this;
	}

	protected final void internalRegisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.register(_listener, l);
	}

	@Override
	public UserInfo unregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		internalUnregisterListener(l);
		return this;
	}

	protected final void internalUnregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.unregister(_listener, l);
	}

	@Override
	public String jsonType() {
		return USER_INFO__TYPE;
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
			default: return de.haumacher.msgbuf.observer.Observable.super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case NICKNAME: setNickname((String) value); break;
			case LANGUAGE: setLanguage((String) value); break;
		}
	}

	/** Reads a new instance from the given reader. */
	public static UserInfo readUserInfo(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		UserInfo result = new UserInfo();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
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
	public final void writeTo(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		out.beginObject();
		writeFields(out);
		out.endObject();
	}

	/**
	 * Serializes all fields of this instance to the given binary output.
	 *
	 * @param out
	 *        The binary output to write to.
	 * @throws java.io.IOException If writing fails.
	 */
	protected void writeFields(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		out.name(NICKNAME__ID);
		out.value(getNickname());
		out.name(LANGUAGE__ID);
		out.value(getLanguage());
	}

	/** Reads a new instance from the given reader. */
	public static UserInfo readUserInfo(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		UserInfo result = new UserInfo();
		while (in.hasNext()) {
			int field = in.nextName();
			result.readField(in, field);
		}
		in.endObject();
		return result;
	}

	/** Consumes the value for the field with the given ID and assigns its value. */
	protected void readField(de.haumacher.msgbuf.binary.DataReader in, int field) throws java.io.IOException {
		switch (field) {
			case NICKNAME__ID: setNickname(in.nextString()); break;
			case LANGUAGE__ID: setLanguage(in.nextString()); break;
			default: in.skipValue(); 
		}
	}

}
