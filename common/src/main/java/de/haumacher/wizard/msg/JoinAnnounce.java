package de.haumacher.wizard.msg;

/**
 * Announces that a player has joined a game.
 */
public class JoinAnnounce extends Msg {

	/**
	 * Creates a {@link JoinAnnounce} instance.
	 */
	public static JoinAnnounce create() {
		return new de.haumacher.wizard.msg.JoinAnnounce();
	}

	/** Identifier for the {@link JoinAnnounce} type in JSON format. */
	public static final String JOIN_ANNOUNCE__TYPE = "JoinAnnounce";

	/** @see #getPlayer() */
	private static final String PLAYER__PROP = "player";

	/** @see #getGameId() */
	private static final String GAME_ID__PROP = "gameId";

	private Player _player = null;

	private String _gameId = "";

	/**
	 * Creates a {@link JoinAnnounce} instance.
	 *
	 * @see JoinAnnounce#create()
	 */
	protected JoinAnnounce() {
		super();
	}

	/**
	 * The player that joined the game.
	 */
	public final Player getPlayer() {
		return _player;
	}

	/**
	 * @see #getPlayer()
	 */
	public JoinAnnounce setPlayer(Player value) {
		internalSetPlayer(value);
		return this;
	}

	/** Internal setter for {@link #getPlayer()} without chain call utility. */
	protected final void internalSetPlayer(Player value) {
		_player = value;
	}

	/**
	 * Checks, whether {@link #getPlayer()} has a value.
	 */
	public final boolean hasPlayer() {
		return _player != null;
	}

	/**
	 * The ID of the game the player joined.
	 */
	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public JoinAnnounce setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return JOIN_ANNOUNCE__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static JoinAnnounce readJoinAnnounce(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.JoinAnnounce result = new de.haumacher.wizard.msg.JoinAnnounce();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasPlayer()) {
			out.name(PLAYER__PROP);
			getPlayer().writeTo(out);
		}
		out.name(GAME_ID__PROP);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER__PROP: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			case GAME_ID__PROP: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
