package de.haumacher.wizard.msg;

/**
 * Takes over a lost connection that was established before.
 */
public class Reconnect extends LoginCmd {

	/**
	 * Creates a {@link Reconnect} instance.
	 */
	public static Reconnect create() {
		return new de.haumacher.wizard.msg.Reconnect();
	}

	/** Identifier for the {@link Reconnect} type in JSON format. */
	public static final String RECONNECT__TYPE = "Reconnect";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID__PROP = "playerId";

	/** @see #getGameId() */
	private static final String GAME_ID__PROP = "gameId";

	private String _playerId = "";

	private String _gameId = "";

	/**
	 * Creates a {@link Reconnect} instance.
	 *
	 * @see Reconnect#create()
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
		de.haumacher.wizard.msg.Reconnect result = new de.haumacher.wizard.msg.Reconnect();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID__PROP);
		out.value(getPlayerId());
		out.name(GAME_ID__PROP);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID__PROP: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case GAME_ID__PROP: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(LoginCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
