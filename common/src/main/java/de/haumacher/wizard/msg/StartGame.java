package de.haumacher.wizard.msg;

/**
 * Informs members of a game and idle players that a game has started and is not accepting players anymore.
 */
public class StartGame extends Cmd {

	/**
	 * Creates a {@link StartGame} instance.
	 */
	public static StartGame create() {
		return new de.haumacher.wizard.msg.StartGame();
	}

	/** Identifier for the {@link StartGame} type in JSON format. */
	public static final String START_GAME__TYPE = "StartGame";

	/** @see #getGameId() */
	private static final String GAME_ID__PROP = "gameId";

	private String _gameId = "";

	/**
	 * Creates a {@link StartGame} instance.
	 *
	 * @see StartGame#create()
	 */
	protected StartGame() {
		super();
	}

	/**
	 * The ID of the game that has started.
	 */
	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public StartGame setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return START_GAME__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static StartGame readStartGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.StartGame result = new de.haumacher.wizard.msg.StartGame();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(GAME_ID__PROP);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME_ID__PROP: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
