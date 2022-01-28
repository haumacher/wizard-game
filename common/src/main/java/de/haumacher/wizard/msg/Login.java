package de.haumacher.wizard.msg;

/**
 * First message that must be sent after connecting to a game server.
 *
 * <p>
 * On success, a {@link LoggedIn} message is sent back.
 * </p>
 */
public class Login extends Cmd {

	/**
	 * Creates a {@link Login} instance.
	 */
	public static Login create() {
		return new Login();
	}

	/** Identifier for the {@link Login} type in JSON format. */
	public static final String LOGIN__TYPE = "Login";

	/** @see #getName() */
	private static final String NAME = "name";

	/** @see #getVersion() */
	private static final String VERSION = "version";

	/** @see #getLocale() */
	private static final String LOCALE = "locale";

	private String _name = "";

	private int _version = 0;

	private String _locale = "";

	/**
	 * Creates a {@link Login} instance.
	 *
	 * @see #create()
	 */
	protected Login() {
		super();
	}

	/**
	 * The nick name of the player.
	 */
	public final String getName() {
		return _name;
	}

	/**
	 * @see #getName()
	 */
	public Login setName(String value) {
		internalSetName(value);
		return this;
	}
	/** Internal setter for {@link #getName()} without chain call utility. */
	protected final void internalSetName(String value) {
		_name = value;
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
	public Login setVersion(int value) {
		internalSetVersion(value);
		return this;
	}
	/** Internal setter for {@link #getVersion()} without chain call utility. */
	protected final void internalSetVersion(int value) {
		_version = value;
	}


	/**
	 * The locale of the client. The server tries to localize error message in that locale.
	 */
	public final String getLocale() {
		return _locale;
	}

	/**
	 * @see #getLocale()
	 */
	public Login setLocale(String value) {
		internalSetLocale(value);
		return this;
	}
	/** Internal setter for {@link #getLocale()} without chain call utility. */
	protected final void internalSetLocale(String value) {
		_locale = value;
	}


	@Override
	public String jsonType() {
		return LOGIN__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Login readLogin(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Login result = new Login();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(NAME);
		out.value(getName());
		out.name(VERSION);
		out.value(getVersion());
		out.name(LOCALE);
		out.value(getLocale());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case NAME: setName(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case VERSION: setVersion(in.nextInt()); break;
			case LOCALE: setLocale(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
