package de.haumacher.wizard.msg;

public class RequestBid extends Msg {

	/**
	 * Creates a {@link RequestBid} instance.
	 */
	public static RequestBid create() {
		return new RequestBid();
	}

	/** Identifier for the {@link RequestBid} type in JSON format. */
	public static final String REQUEST_BID__TYPE = "RequestBid";

	/** @see #getExpected() */
	private static final String EXPECTED = "expected";

	/** @see #getRound() */
	private static final String ROUND = "round";

	private int _expected = 0;

	private int _round = 0;

	/**
	 * Creates a {@link RequestBid} instance.
	 *
	 * @see #create()
	 */
	protected RequestBid() {
		super();
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
		RequestBid result = new RequestBid();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(EXPECTED);
		out.value(getExpected());
		out.name(ROUND);
		out.value(getRound());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case EXPECTED: setExpected(in.nextInt()); break;
			case ROUND: setRound(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
