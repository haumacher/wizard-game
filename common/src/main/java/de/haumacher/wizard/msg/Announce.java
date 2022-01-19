package de.haumacher.wizard.msg;

/**
 * Message to forward a {@link GameCmd} sent by one player of a game to all other players.
 */
public class Announce extends GameMsg {

	/**
	 * Creates a {@link Announce} instance.
	 */
	public static Announce create() {
		return new Announce();
	}

	/** Identifier for the {@link Announce} type in JSON format. */
	public static final String ANNOUNCE__TYPE = "Announce";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID = "playerId";

	/** @see #getCmd() */
	private static final String CMD = "cmd";

	private String _playerId = "";

	private GameCmd _cmd = null;

	/**
	 * Creates a {@link Announce} instance.
	 *
	 * @see #create()
	 */
	protected Announce() {
		super();
	}

	/**
	 * The ID of the player that sent the command.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public Announce setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}
	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}


	/**
	 * The command sent by the player with the ID given in {@link #getPlayerId()}.
	 */
	public final GameCmd getCmd() {
		return _cmd;
	}

	/**
	 * @see #getCmd()
	 */
	public Announce setCmd(GameCmd value) {
		internalSetCmd(value);
		return this;
	}
	/** Internal setter for {@link #getCmd()} without chain call utility. */
	protected final void internalSetCmd(GameCmd value) {
		_cmd = value;
	}


	/**
	 * Checks, whether {@link #getCmd()} has a value.
	 */
	public final boolean hasCmd() {
		return _cmd != null;
	}

	@Override
	public String jsonType() {
		return ANNOUNCE__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Announce readAnnounce(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Announce result = new Announce();
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
		if (hasCmd()) {
			out.name(CMD);
			getCmd().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case CMD: setCmd(de.haumacher.wizard.msg.GameCmd.readGameCmd(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
