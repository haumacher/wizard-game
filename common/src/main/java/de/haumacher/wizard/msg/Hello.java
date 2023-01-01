package de.haumacher.wizard.msg;

/**
 * The first method sent after opening a connection.
 */
public class Hello extends LoginCmd {

	/**
	 * Creates a {@link Hello} instance.
	 */
	public static Hello create() {
		return new de.haumacher.wizard.msg.Hello();
	}

	/** Identifier for the {@link Hello} type in JSON format. */
	public static final String HELLO__TYPE = "Hello";

	/** @see #getVersion() */
	private static final String VERSION__PROP = "version";

	/** @see #getLanguage() */
	private static final String LANGUAGE__PROP = "language";

	private int _version = 0;

	private String _language = "";

	/**
	 * Creates a {@link Hello} instance.
	 *
	 * @see Hello#create()
	 */
	protected Hello() {
		super();
	}

	/**
	 * The protocol version of the connecting client. This can be used to detect version mismatch of client and server.
	 */
	public final int getVersion() {
		return _version;
	}

	/**
	 * @see #getVersion()
	 */
	public Hello setVersion(int value) {
		internalSetVersion(value);
		return this;
	}

	/** Internal setter for {@link #getVersion()} without chain call utility. */
	protected final void internalSetVersion(int value) {
		_version = value;
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
	public Hello setLanguage(String value) {
		internalSetLanguage(value);
		return this;
	}

	/** Internal setter for {@link #getLanguage()} without chain call utility. */
	protected final void internalSetLanguage(String value) {
		_language = value;
	}

	@Override
	public String jsonType() {
		return HELLO__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Hello readHello(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Hello result = new de.haumacher.wizard.msg.Hello();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(VERSION__PROP);
		out.value(getVersion());
		out.name(LANGUAGE__PROP);
		out.value(getLanguage());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case VERSION__PROP: setVersion(in.nextInt()); break;
			case LANGUAGE__PROP: setLanguage(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
