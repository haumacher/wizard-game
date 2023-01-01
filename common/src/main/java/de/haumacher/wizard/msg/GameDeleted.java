package de.haumacher.wizard.msg;

/**
 * Informs idle players that a game that was waiting for players has been deleted.
 */
public class GameDeleted extends Msg {

	/**
	 * Creates a {@link GameDeleted} instance.
	 */
	public static GameDeleted create() {
		return new de.haumacher.wizard.msg.GameDeleted();
	}

	/** Identifier for the {@link GameDeleted} type in JSON format. */
	public static final String GAME_DELETED__TYPE = "GameDeleted";

	/** @see #getGameId() */
	private static final String GAME_ID__PROP = "gameId";

	private String _gameId = "";

	/**
	 * Creates a {@link GameDeleted} instance.
	 *
	 * @see GameDeleted#create()
	 */
	protected GameDeleted() {
		super();
	}

	/**
	 * The ID of the game that has been deleted.
	 */
	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public GameDeleted setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return GAME_DELETED__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static GameDeleted readGameDeleted(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.GameDeleted result = new de.haumacher.wizard.msg.GameDeleted();
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
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
