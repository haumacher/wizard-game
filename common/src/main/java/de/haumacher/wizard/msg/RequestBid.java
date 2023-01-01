package de.haumacher.wizard.msg;

/**
 * Requests a bid.
 */
public class RequestBid extends GameMsg {

	/**
	 * Creates a {@link RequestBid} instance.
	 */
	public static RequestBid create() {
		return new de.haumacher.wizard.msg.RequestBid();
	}

	/** Identifier for the {@link RequestBid} type in JSON format. */
	public static final String REQUEST_BID__TYPE = "RequestBid";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID__PROP = "playerId";

	/** @see #getExpected() */
	private static final String EXPECTED__PROP = "expected";

	/** @see #getRound() */
	private static final String ROUND__PROP = "round";

	private String _playerId = "";

	private int _expected = 0;

	private int _round = 0;

	/**
	 * Creates a {@link RequestBid} instance.
	 *
	 * @see RequestBid#create()
	 */
	protected RequestBid() {
		super();
	}

	/**
	 * The player that is expected to place a bid.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public RequestBid setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}

	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}

	/**
	 * The number of tricks expected by other players so far.
	 */
	public final int getExpected() {
		return _expected;
	}

	/**
	 * @see #getExpected()
	 */
	public RequestBid setExpected(int value) {
		internalSetExpected(value);
		return this;
	}

	/** Internal setter for {@link #getExpected()} without chain call utility. */
	protected final void internalSetExpected(int value) {
		_expected = value;
	}

	/**
	 * The round number and maximum number of tricks possible in that round.
	 */
	public final int getRound() {
		return _round;
	}

	/**
	 * @see #getRound()
	 */
	public RequestBid setRound(int value) {
		internalSetRound(value);
		return this;
	}

	/** Internal setter for {@link #getRound()} without chain call utility. */
	protected final void internalSetRound(int value) {
		_round = value;
	}

	@Override
	public String jsonType() {
		return REQUEST_BID__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static RequestBid readRequestBid(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.RequestBid result = new de.haumacher.wizard.msg.RequestBid();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID__PROP);
		out.value(getPlayerId());
		out.name(EXPECTED__PROP);
		out.value(getExpected());
		out.name(ROUND__PROP);
		out.value(getRound());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID__PROP: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case EXPECTED__PROP: setExpected(in.nextInt()); break;
			case ROUND__PROP: setRound(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
