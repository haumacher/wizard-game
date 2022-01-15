package de.haumacher.wizard.msg;

public class JoinAnnounce extends Msg {

	/**
	 * Creates a {@link JoinAnnounce} instance.
	 */
	public static JoinAnnounce create() {
		return new JoinAnnounce();
	}

	/** Identifier for the {@link JoinAnnounce} type in JSON format. */
	public static final String JOIN_ANNOUNCE__TYPE = "JoinAnnounce";

	/** @see #getGameId() */
	private static final String GAME_ID = "gameId";

	/** @see #getPlayer() */
	private static final String PLAYER = "player";

	private String _gameId = "";

	private Player _player = null;

	/**
	 * Creates a {@link JoinAnnounce} instance.
	 *
	 * @see #create()
	 */
	protected JoinAnnounce() {
		super();
	}

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

	@Override
	public String jsonType() {
		return JOIN_ANNOUNCE__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static JoinAnnounce readJoinAnnounce(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		JoinAnnounce result = new JoinAnnounce();
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
		if (hasPlayer()) {
			out.name(PLAYER);
			getPlayer().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME_ID: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case PLAYER: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
