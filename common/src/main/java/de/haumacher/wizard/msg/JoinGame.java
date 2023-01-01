package de.haumacher.wizard.msg;

/**
 * Requests joining a game.
 */
public class JoinGame extends Cmd {

	/**
	 * Creates a {@link JoinGame} instance.
	 */
	public static JoinGame create() {
		return new de.haumacher.wizard.msg.JoinGame();
	}

	/** Identifier for the {@link JoinGame} type in JSON format. */
	public static final String JOIN_GAME__TYPE = "JoinGame";

	/** @see #getGameId() */
	private static final String GAME_ID__PROP = "gameId";

	private String _gameId = "";

	/**
	 * Creates a {@link JoinGame} instance.
	 *
	 * @see JoinGame#create()
	 */
	protected JoinGame() {
		super();
	}

	/**
	 * The ID of the game the sender wants to join.
	 */
	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public JoinGame setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return JOIN_GAME__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static JoinGame readJoinGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.JoinGame result = new de.haumacher.wizard.msg.JoinGame();
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
