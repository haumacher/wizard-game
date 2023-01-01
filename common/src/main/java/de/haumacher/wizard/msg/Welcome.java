package de.haumacher.wizard.msg;

/**
 * Response message upon successful {@link Login}.
 */
public class Welcome extends ResultMsg {

	/**
	 * Creates a {@link Welcome} instance.
	 */
	public static Welcome create() {
		return new de.haumacher.wizard.msg.Welcome();
	}

	/** Identifier for the {@link Welcome} type in JSON format. */
	public static final String WELCOME__TYPE = "Welcome";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID__PROP = "playerId";

	private String _playerId = "";

	/**
	 * Creates a {@link Welcome} instance.
	 *
	 * @see Welcome#create()
	 */
	protected Welcome() {
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
	public Welcome setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}

	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}

	@Override
	public String jsonType() {
		return WELCOME__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Welcome readWelcome(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Welcome result = new de.haumacher.wizard.msg.Welcome();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID__PROP);
		out.value(getPlayerId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID__PROP: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
