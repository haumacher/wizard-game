package de.haumacher.wizard.msg;

/**
 * Command to request leaving a game.
 */
public class LeaveGame extends Cmd {

	/**
	 * Creates a {@link LeaveGame} instance.
	 */
	public static LeaveGame create() {
		return new LeaveGame();
	}

	/** Identifier for the {@link LeaveGame} type in JSON format. */
	public static final String LEAVE_GAME__TYPE = "LeaveGame";

	/** @see #getGameId() */
	private static final String GAME_ID = "gameId";

	private String _gameId = "";

	/**
	 * Creates a {@link LeaveGame} instance.
	 *
	 * @see #create()
	 */
	protected LeaveGame() {
		super();
	}

	/**
	 * The ID of the game to leave.
	 */
	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public LeaveGame setGameId(String value) {
		internalSetGameId(value);
		return this;
	}
	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}


	@Override
	public String jsonType() {
		return LEAVE_GAME__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static LeaveGame readLeaveGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		LeaveGame result = new LeaveGame();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(GAME_ID);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME_ID: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
