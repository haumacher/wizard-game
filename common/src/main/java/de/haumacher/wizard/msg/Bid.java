package de.haumacher.wizard.msg;

/**
 * Message announcing a player's bid.
 */
public class Bid extends GameCmd {

	/**
	 * Creates a {@link Bid} instance.
	 */
	public static Bid create() {
		return new de.haumacher.wizard.msg.Bid();
	}

	/** Identifier for the {@link Bid} type in JSON format. */
	public static final String BID__TYPE = "Bid";

	/** @see #getCnt() */
	private static final String CNT__PROP = "cnt";

	/** @see #getExpected() */
	private static final String EXPECTED__PROP = "expected";

	private int _cnt = 0;

	private int _expected = 0;

	/**
	 * Creates a {@link Bid} instance.
	 *
	 * @see Bid#create()
	 */
	protected Bid() {
		super();
	}

	/**
	 * The bid of the current player
	 */
	public final int getCnt() {
		return _cnt;
	}

	/**
	 * @see #getCnt()
	 */
	public Bid setCnt(int value) {
		internalSetCnt(value);
		return this;
	}

	/** Internal setter for {@link #getCnt()} without chain call utility. */
	protected final void internalSetCnt(int value) {
		_cnt = value;
	}

	/**
	 * The sum of tricks expected by all players so far.
	 *
	 * <p>
	 * The value is only relevant when the message is announced to all players. It is not required to set this value when sending the command to the server.
	 * </p>
	 */
	public final int getExpected() {
		return _expected;
	}

	/**
	 * @see #getExpected()
	 */
	public Bid setExpected(int value) {
		internalSetExpected(value);
		return this;
	}

	/** Internal setter for {@link #getExpected()} without chain call utility. */
	protected final void internalSetExpected(int value) {
		_expected = value;
	}

	@Override
	public String jsonType() {
		return BID__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Bid readBid(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Bid result = new de.haumacher.wizard.msg.Bid();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(CNT__PROP);
		out.value(getCnt());
		out.name(EXPECTED__PROP);
		out.value(getExpected());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case CNT__PROP: setCnt(in.nextInt()); break;
			case EXPECTED__PROP: setExpected(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
