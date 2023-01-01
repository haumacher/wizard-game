package de.haumacher.wizard.msg;

/**
 * Internal information kept for each player of a game on the server.
 */
public class PlayerState extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link PlayerState} instance.
	 */
	public static PlayerState create() {
		return new de.haumacher.wizard.msg.PlayerState();
	}

	/** Identifier for the {@link PlayerState} type in JSON format. */
	public static final String PLAYER_STATE__TYPE = "PlayerState";

	/** @see #getPlayer() */
	private static final String PLAYER__PROP = "player";

	/** @see #getPoints() */
	private static final String POINTS__PROP = "points";

	/** @see #getRoundState() */
	private static final String ROUND_STATE__PROP = "roundState";

	private Player _player = null;

	private int _points = 0;

	private RoundState _roundState = null;

	/**
	 * Creates a {@link PlayerState} instance.
	 *
	 * @see PlayerState#create()
	 */
	protected PlayerState() {
		super();
	}

	/**
	 * Player data.
	 */
	public final Player getPlayer() {
		return _player;
	}

	/**
	 * @see #getPlayer()
	 */
	public PlayerState setPlayer(Player value) {
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
	 * Points won so far.
	 */
	public final int getPoints() {
		return _points;
	}

	/**
	 * @see #getPoints()
	 */
	public PlayerState setPoints(int value) {
		internalSetPoints(value);
		return this;
	}

	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(int value) {
		_points = value;
	}

	/**
	 * Information about the current round.
	 */
	public final RoundState getRoundState() {
		return _roundState;
	}

	/**
	 * @see #getRoundState()
	 */
	public PlayerState setRoundState(RoundState value) {
		internalSetRoundState(value);
		return this;
	}

	/** Internal setter for {@link #getRoundState()} without chain call utility. */
	protected final void internalSetRoundState(RoundState value) {
		_roundState = value;
	}

	/**
	 * Checks, whether {@link #getRoundState()} has a value.
	 */
	public final boolean hasRoundState() {
		return _roundState != null;
	}

	/** Reads a new instance from the given reader. */
	public static PlayerState readPlayerState(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.PlayerState result = new de.haumacher.wizard.msg.PlayerState();
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
		if (hasRoundState()) {
			out.name(ROUND_STATE__PROP);
			getRoundState().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER__PROP: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			case POINTS__PROP: setPoints(in.nextInt()); break;
			case ROUND_STATE__PROP: setRoundState(de.haumacher.wizard.msg.RoundState.readRoundState(in)); break;
			default: super.readField(in, field);
		}
	}

}
