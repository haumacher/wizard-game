package de.haumacher.wizard.msg;

/**
 * Informs idle players that a new game was created.
 */
public class GameCreated extends Msg {

	/**
	 * Creates a {@link GameCreated} instance.
	 */
	public static GameCreated create() {
		return new de.haumacher.wizard.msg.GameCreated();
	}

	/** Identifier for the {@link GameCreated} type in JSON format. */
	public static final String GAME_CREATED__TYPE = "GameCreated";

	/** @see #getOwnerId() */
	private static final String OWNER_ID__PROP = "ownerId";

	/** @see #getGame() */
	private static final String GAME__PROP = "game";

	private String _ownerId = "";

	private Game _game = null;

	/**
	 * Creates a {@link GameCreated} instance.
	 *
	 * @see GameCreated#create()
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
		de.haumacher.wizard.msg.GameCreated result = new de.haumacher.wizard.msg.GameCreated();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(OWNER_ID__PROP);
		out.value(getOwnerId());
		if (hasGame()) {
			out.name(GAME__PROP);
			getGame().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case OWNER_ID__PROP: setOwnerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case GAME__PROP: setGame(de.haumacher.wizard.msg.Game.readGame(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
