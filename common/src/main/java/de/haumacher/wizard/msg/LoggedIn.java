package de.haumacher.wizard.msg;

/**
 * Response message upon successful {@link Login}.
 */
public class LoggedIn extends ResultMsg {

	/**
	 * Creates a {@link LoggedIn} instance.
	 */
	public static LoggedIn create() {
		return new LoggedIn();
	}

	/** Identifier for the {@link LoggedIn} type in JSON format. */
	public static final String LOGGED_IN__TYPE = "LoggedIn";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID = "playerId";

	private String _playerId = "";

	/**
	 * Creates a {@link LoggedIn} instance.
	 *
	 * @see #create()
	 */
	protected LoggedIn() {
		super();
	}

	/**
	 * The ID of the player that logged in to the server.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public LoggedIn setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}
	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}


	@Override
	public String jsonType() {
		return LOGGED_IN__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static LoggedIn readLoggedIn(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		LoggedIn result = new LoggedIn();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID);
		out.value(getPlayerId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
