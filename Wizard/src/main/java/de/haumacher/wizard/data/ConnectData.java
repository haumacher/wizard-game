package de.haumacher.wizard.data;

public class ConnectData extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.msgbuf.binary.BinaryDataObject, de.haumacher.msgbuf.observer.Observable {

	/**
	 * Creates a {@link ConnectData} instance.
	 */
	public static ConnectData create() {
		return new ConnectData();
	}

	/** Identifier for the {@link ConnectData} type in JSON format. */
	public static final String CONNECT_DATA__TYPE = "ConnectData";

	/** @see #getNickName() */
	public static final String NICK_NAME = "nickName";

	/** @see #getServerAddr() */
	public static final String SERVER_ADDR = "serverAddr";

	/** Identifier for the property {@link #getNickName()} in binary format. */
	public static final int NICK_NAME__ID = 1;

	/** Identifier for the property {@link #getServerAddr()} in binary format. */
	public static final int SERVER_ADDR__ID = 2;

	private String _nickName = "";

	private String _serverAddr = "";

	/**
	 * Creates a {@link ConnectData} instance.
	 *
	 * @see #create()
	 */
	protected ConnectData() {
		super();
	}

	public final String getNickName() {
		return _nickName;
	}

	/**
	 * @see #getNickName()
	 */
	public ConnectData setNickName(String value) {
		internalSetNickName(value);
		return this;
	}
	/** Internal setter for {@link #getNickName()} without chain call utility. */
	protected final void internalSetNickName(String value) {
		_listener.beforeSet(this, NICK_NAME, value);
		_nickName = value;
	}


	public final String getServerAddr() {
		return _serverAddr;
	}

	/**
	 * @see #getServerAddr()
	 */
	public ConnectData setServerAddr(String value) {
		internalSetServerAddr(value);
		return this;
	}
	/** Internal setter for {@link #getServerAddr()} without chain call utility. */
	protected final void internalSetServerAddr(String value) {
		_listener.beforeSet(this, SERVER_ADDR, value);
		_serverAddr = value;
	}


	protected de.haumacher.msgbuf.observer.Listener _listener = de.haumacher.msgbuf.observer.Listener.NONE;

	@Override
	public ConnectData registerListener(de.haumacher.msgbuf.observer.Listener l) {
		internalRegisterListener(l);
		return this;
	}

	protected final void internalRegisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.register(_listener, l);
	}

	@Override
	public ConnectData unregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		internalUnregisterListener(l);
		return this;
	}

	protected final void internalUnregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.unregister(_listener, l);
	}

	@Override
	public String jsonType() {
		return CONNECT_DATA__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			NICK_NAME, 
			SERVER_ADDR));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case NICK_NAME: return getNickName();
			case SERVER_ADDR: return getServerAddr();
			default: return de.haumacher.msgbuf.observer.Observable.super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case NICK_NAME: setNickName((String) value); break;
			case SERVER_ADDR: setServerAddr((String) value); break;
		}
	}

	/** Reads a new instance from the given reader. */
	public static ConnectData readConnectData(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		ConnectData result = new ConnectData();
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
		out.name(NICK_NAME);
		out.value(getNickName());
		out.name(SERVER_ADDR);
		out.value(getServerAddr());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case NICK_NAME: setNickName(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case SERVER_ADDR: setServerAddr(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
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
		out.name(NICK_NAME__ID);
		out.value(getNickName());
		out.name(SERVER_ADDR__ID);
		out.value(getServerAddr());
	}

	/** Reads a new instance from the given reader. */
	public static ConnectData readConnectData(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		ConnectData result = new ConnectData();
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
			case NICK_NAME__ID: setNickName(in.nextString()); break;
			case SERVER_ADDR__ID: setServerAddr(in.nextString()); break;
			default: in.skipValue(); 
		}
	}

}
