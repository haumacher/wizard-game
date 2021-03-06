package de.haumacher.wizard.msg;

/**
 * Announces that a player has left a game.
 */
public class LeaveAnnounce extends Msg {

	/**
	 * Creates a {@link LeaveAnnounce} instance.
	 */
	public static LeaveAnnounce create() {
		return new LeaveAnnounce();
	}

	/** Identifier for the {@link LeaveAnnounce} type in JSON format. */
	public static final String LEAVE_ANNOUNCE__TYPE = "LeaveAnnounce";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID = "playerId";

	/** @see #getGameId() */
	private static final String GAME_ID = "gameId";

	private String _playerId = "";

	private String _gameId = "";

	/**
	 * Creates a {@link LeaveAnnounce} instance.
	 *
	 * @see #create()
	 */
	protected LeaveAnnounce() {
		super();
	}

	/**
	 * The ID of the player that left a game.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public LeaveAnnounce setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}
	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}


	/**
	 * The ID of the game the player left.
	 */
	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public LeaveAnnounce setGameId(String value) {
		internalSetGameId(value);
		return this;
	}
	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}


	@Override
	public String jsonType() {
		return LEAVE_ANNOUNCE__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static LeaveAnnounce readLeaveAnnounce(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		LeaveAnnounce result = new LeaveAnnounce();
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
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
