package de.haumacher.wizard.msg;

public class PlayerInfo extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link PlayerInfo} instance.
	 */
	public static PlayerInfo create() {
		return new PlayerInfo();
	}

	/** Identifier for the {@link PlayerInfo} type in JSON format. */
	public static final String PLAYER_INFO__TYPE = "PlayerInfo";

	/** @see #getBid() */
	private static final String BID = "bid";

	/** @see #getTricks() */
	private static final String TRICKS = "tricks";

	/** @see #getScore() */
	private static final String SCORE = "score";

	private int _bid = 0;

	private int _tricks = 0;

	private int _score = 0;

	/**
	 * Creates a {@link PlayerInfo} instance.
	 *
	 * @see #create()
	 */
	protected PlayerInfo() {
		super();
	}

	public final int getBid() {
		return _bid;
	}

	/**
	 * @see #getBid()
	 */
	public PlayerInfo setBid(int value) {
		internalSetBid(value);
		return this;
	}
	/** Internal setter for {@link #getBid()} without chain call utility. */
	protected final void internalSetBid(int value) {
		_bid = value;
	}


	public final int getTricks() {
		return _tricks;
	}

	/**
	 * @see #getTricks()
	 */
	public PlayerInfo setTricks(int value) {
		internalSetTricks(value);
		return this;
	}
	/** Internal setter for {@link #getTricks()} without chain call utility. */
	protected final void internalSetTricks(int value) {
		_tricks = value;
	}


	public final int getScore() {
		return _score;
	}

	/**
	 * @see #getScore()
	 */
	public PlayerInfo setScore(int value) {
		internalSetScore(value);
		return this;
	}
	/** Internal setter for {@link #getScore()} without chain call utility. */
	protected final void internalSetScore(int value) {
		_score = value;
	}


	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return PLAYER_INFO__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static PlayerInfo readPlayerInfo(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		PlayerInfo result = new PlayerInfo();
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
		out.name(BID);
		out.value(getBid());
		out.name(TRICKS);
		out.value(getTricks());
		out.name(SCORE);
		out.value(getScore());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case BID: setBid(in.nextInt()); break;
			case TRICKS: setTricks(in.nextInt()); break;
			case SCORE: setScore(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

}
