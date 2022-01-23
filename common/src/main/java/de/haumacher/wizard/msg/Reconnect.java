package de.haumacher.wizard.msg;

/**
 * Takes over a lost connection that was established before.
 */
public class Reconnect extends Cmd {

	/**
	 * Creates a {@link Reconnect} instance.
	 */
	public static Reconnect create() {
		return new Reconnect();
	}

	/** Identifier for the {@link Reconnect} type in JSON format. */
	public static final String RECONNECT__TYPE = "Reconnect";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID = "playerId";

	/** @see #getGameId() */
	private static final String GAME_ID = "gameId";

	private String _playerId = "";

	private String _gameId = "";

	/**
	 * Creates a {@link Reconnect} instance.
	 *
	 * @see #create()
	 */
	protected Reconnect() {
		super();
	}

	/**
	 * The ID that was assigned to the player in a former {@link Welcome} message.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public Reconnect setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}
	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}


	/**
	 * The ID of the game that this player would like to reconnect to.
	 */
	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public Reconnect setGameId(String value) {
		internalSetGameId(value);
		return this;
	}
	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}


	@Override
	public String jsonType() {
		return RECONNECT__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Reconnect readReconnect(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Reconnect result = new Reconnect();
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
		out.name(GAME_ID);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case GAME_ID: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
