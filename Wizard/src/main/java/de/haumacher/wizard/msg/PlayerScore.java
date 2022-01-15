package de.haumacher.wizard.msg;

public class PlayerScore extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link PlayerScore} instance.
	 */
	public static PlayerScore create() {
		return new PlayerScore();
	}

	/** Identifier for the {@link PlayerScore} type in JSON format. */
	public static final String PLAYER_SCORE__TYPE = "PlayerScore";

	/** @see #getPlayer() */
	private static final String PLAYER = "player";

	/** @see #getScore() */
	private static final String SCORE = "score";

	private Player _player = null;

	private int _score = 0;

	/**
	 * Creates a {@link PlayerScore} instance.
	 *
	 * @see #create()
	 */
	protected PlayerScore() {
		super();
	}

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

	public final int getScore() {
		return _score;
	}

	/**
	 * @see #getScore()
	 */
	public PlayerScore setScore(int value) {
		internalSetScore(value);
		return this;
	}
	/** Internal setter for {@link #getScore()} without chain call utility. */
	protected final void internalSetScore(int value) {
		_score = value;
	}


	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return PLAYER_SCORE__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static PlayerScore readPlayerScore(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		PlayerScore result = new PlayerScore();
		in.beginObject();
		result.readFields(in);
		in.endObject();
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
			out.name(PLAYER);
			getPlayer().writeTo(out);
		}
		out.name(SCORE);
		out.value(getScore());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			case SCORE: setScore(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

}
