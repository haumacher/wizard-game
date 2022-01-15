package de.haumacher.wizard.msg;

public class GameCreated extends Msg {

	/**
	 * Creates a {@link GameCreated} instance.
	 */
	public static GameCreated create() {
		return new GameCreated();
	}

	/** Identifier for the {@link GameCreated} type in JSON format. */
	public static final String GAME_CREATED__TYPE = "GameCreated";

	/** @see #getOwnerId() */
	private static final String OWNER_ID = "ownerId";

	/** @see #getGame() */
	private static final String GAME = "game";

	private String _ownerId = "";

	private Game _game = null;

	/**
	 * Creates a {@link GameCreated} instance.
	 *
	 * @see #create()
	 */
	protected GameCreated() {
		super();
	}

	/**
	 * Id of the player that created the game. This player is implicitly the first player in the game without explicitly joining the game.
	 */
	public final String getOwnerId() {
		return _ownerId;
	}

	/**
	 * @see #getOwnerId()
	 */
	public GameCreated setOwnerId(String value) {
		internalSetOwnerId(value);
		return this;
	}
	/** Internal setter for {@link #getOwnerId()} without chain call utility. */
	protected final void internalSetOwnerId(String value) {
		_ownerId = value;
	}


	/**
	 * The newly created game.
	 */
	public final Game getGame() {
		return _game;
	}

	/**
	 * @see #getGame()
	 */
	public GameCreated setGame(Game value) {
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
		return GAME_CREATED__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static GameCreated readGameCreated(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		GameCreated result = new GameCreated();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(OWNER_ID);
		out.value(getOwnerId());
		if (hasGame()) {
			out.name(GAME);
			getGame().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case OWNER_ID: setOwnerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case GAME: setGame(de.haumacher.wizard.msg.Game.readGame(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
