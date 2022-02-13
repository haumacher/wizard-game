package de.haumacher.wizard.server.db.model;

/**
 * Result of {@link CreateAccount}, if successful.
 */
public class CreateAccountResult extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.msgbuf.binary.BinaryDataObject, de.haumacher.msgbuf.observer.Observable {

	/**
	 * Creates a {@link CreateAccountResult} instance.
	 */
	public static CreateAccountResult create() {
		return new CreateAccountResult();
	}

	/** Identifier for the {@link CreateAccountResult} type in JSON format. */
	public static final String CREATE_ACCOUNT_RESULT__TYPE = "CreateAccountResult";

	/** @see #getUid() */
	public static final String UID = "uid";

	/** @see #getSecret() */
	public static final String SECRET = "secret";

	/** Identifier for the property {@link #getUid()} in binary format. */
	public static final int UID__ID = 1;

	/** Identifier for the property {@link #getSecret()} in binary format. */
	public static final int SECRET__ID = 2;

	private String _uid = "";

	private String _secret = "";

	/**
	 * Creates a {@link CreateAccountResult} instance.
	 *
	 * @see #create()
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
		_listener.beforeSet(this, UID, value);
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
		_listener.beforeSet(this, SECRET, value);
		_secret = value;
	}


	protected de.haumacher.msgbuf.observer.Listener _listener = de.haumacher.msgbuf.observer.Listener.NONE;

	@Override
	public CreateAccountResult registerListener(de.haumacher.msgbuf.observer.Listener l) {
		internalRegisterListener(l);
		return this;
	}

	protected final void internalRegisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.register(_listener, l);
	}

	@Override
	public CreateAccountResult unregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		internalUnregisterListener(l);
		return this;
	}

	protected final void internalUnregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.unregister(_listener, l);
	}

	@Override
	public String jsonType() {
		return CREATE_ACCOUNT_RESULT__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			UID, 
			SECRET));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case UID: return getUid();
			case SECRET: return getSecret();
			default: return de.haumacher.msgbuf.observer.Observable.super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case UID: setUid((String) value); break;
			case SECRET: setSecret((String) value); break;
		}
	}

	/** Reads a new instance from the given reader. */
	public static CreateAccountResult readCreateAccountResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		CreateAccountResult result = new CreateAccountResult();
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
		out.name(UID);
		out.value(getUid());
		out.name(SECRET);
		out.value(getSecret());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case SECRET: setSecret(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
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
		out.name(UID__ID);
		out.value(getUid());
		out.name(SECRET__ID);
		out.value(getSecret());
	}

	/** Reads a new instance from the given reader. */
	public static CreateAccountResult readCreateAccountResult(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		CreateAccountResult result = new CreateAccountResult();
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
			case UID__ID: setUid(in.nextString()); break;
			case SECRET__ID: setSecret(in.nextString()); break;
			default: in.skipValue(); 
		}
	}

}
