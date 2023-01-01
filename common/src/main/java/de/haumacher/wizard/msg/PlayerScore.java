package de.haumacher.wizard.msg;

/**
 * Score info for a player.
 */
public class PlayerScore extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link PlayerScore} instance.
	 */
	public static PlayerScore create() {
		return new de.haumacher.wizard.msg.PlayerScore();
	}

	/** Identifier for the {@link PlayerScore} type in JSON format. */
	public static final String PLAYER_SCORE__TYPE = "PlayerScore";

	/** @see #getPlayer() */
	private static final String PLAYER__PROP = "player";

	/** @see #getPoints() */
	private static final String POINTS__PROP = "points";

	private Player _player = null;

	private int _points = 0;

	/**
	 * Creates a {@link PlayerScore} instance.
	 *
	 * @see PlayerScore#create()
	 */
	protected PlayerScore() {
		super();
	}

	/**
	 * The player.
	 */
	public final Player getPlayer() {
		return _player;
	}

	/**
	 * @see #getPlayer()
	 */
	public PlayerScore setPlayer(Player value) {
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
	 * The player's points.
	 */
	public final int getPoints() {
		return _points;
	}

	/**
	 * @see #getPoints()
	 */
	public PlayerScore setPoints(int value) {
		internalSetPoints(value);
		return this;
	}

	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(int value) {
		_points = value;
	}

	/** Reads a new instance from the given reader. */
	public static PlayerScore readPlayerScore(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.PlayerScore result = new de.haumacher.wizard.msg.PlayerScore();
		result.readContent(in);
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasPlayer()) {
			out.name(PLAYER__PROP);
			getPlayer().writeTo(out);
		}
		out.name(POINTS__PROP);
		out.value(getPoints());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER__PROP: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			case POINTS__PROP: setPoints(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

}
