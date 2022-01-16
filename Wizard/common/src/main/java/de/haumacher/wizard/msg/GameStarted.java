package de.haumacher.wizard.msg;

/**
 * Message sent in response to {@link StartGame} to announce the final game configuration.
 */
public class GameStarted extends Msg {

	/**
	 * Creates a {@link GameStarted} instance.
	 */
	public static GameStarted create() {
		return new GameStarted();
	}

	/** Identifier for the {@link GameStarted} type in JSON format. */
	public static final String GAME_STARTED__TYPE = "GameStarted";

	/** @see #getGame() */
	private static final String GAME = "game";

	private Game _game = null;

	/**
	 * Creates a {@link GameStarted} instance.
	 *
	 * @see #create()
	 */
	protected GameStarted() {
		super();
	}

	/**
	 * The final settings of the game that is started
	 */
	public final Game getGame() {
		return _game;
	}

	/**
	 * @see #getGame()
	 */
	public GameStarted setGame(Game value) {
		internalSetGame(value);
		return this;
	}
	/** Internal setter for {@link #getGame()} without chain call utility. */
	protected final void internalSetGame(Game value) {
		_game = value;
	}


	/**
	 * Checks, whether {@link #getGame()} has a value.
	 */
	public final boolean hasGame() {
		return _game != null;
	}

	@Override
	public String jsonType() {
		return GAME_STARTED__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static GameStarted readGameStarted(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		GameStarted result = new GameStarted();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasGame()) {
			out.name(GAME);
			getGame().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME: setGame(de.haumacher.wizard.msg.Game.readGame(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
