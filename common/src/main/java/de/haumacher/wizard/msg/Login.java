package de.haumacher.wizard.msg;

/**
 * First message that must be sent after connecting to a game server.
 *
 * <p>
 * On success, a {@link Welcome} message is sent back.
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

	/** @see #getVersion() */
	private static final String VERSION = "version";

	/** @see #getUid() */
	private static final String UID = "uid";

	/** @see #getSecret() */
	private static final String SECRET = "secret";

	private int _version = 0;

	private String _uid = "";

	private String _secret = "";

	/**
	 * Creates a {@link Login} instance.
	 *
	 * @see #create()
	 */
	protected Login() {
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
	public Login setVersion(int value) {
		internalSetVersion(value);
		return this;
	}
	/** Internal setter for {@link #getVersion()} without chain call utility. */
	protected final void internalSetVersion(int value) {
		_version = value;
	}


	/**
	 * The UID of the player.
	 */
	public final String getUid() {
		return _uid;
	}

	/**
	 * @see #getUid()
	 */
	public Login setUid(String value) {
		internalSetUid(value);
		return this;
	}
	/** Internal setter for {@link #getUid()} without chain call utility. */
	protected final void internalSetUid(String value) {
		_uid = value;
	}


	/**
	 * The user's login secret.
	 */
	public final String getSecret() {
		return _secret;
	}

	/**
	 * @see #getSecret()
	 */
	public Login setSecret(String value) {
		internalSetSecret(value);
		return this;
	}
	/** Internal setter for {@link #getSecret()} without chain call utility. */
	protected final void internalSetSecret(String value) {
		_secret = value;
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
		out.name(VERSION);
		out.value(getVersion());
		out.name(UID);
		out.value(getUid());
		out.name(SECRET);
		out.value(getSecret());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case VERSION: setVersion(in.nextInt()); break;
			case UID: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case SECRET: setSecret(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
