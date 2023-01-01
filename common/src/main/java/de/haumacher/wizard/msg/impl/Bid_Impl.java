package de.haumacher.wizard.msg.impl;

/**
 * Message announcing a player's bid.
 */
public class Bid_Impl extends de.haumacher.wizard.msg.impl.GameCmd_Impl implements de.haumacher.wizard.msg.Bid {

	private int _cnt = 0;

	private int _expected = 0;

	/**
	 * Creates a {@link Bid_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Bid#create()
	 */
	public Bid_Impl() {
		super();
	}

	@Override
	public final int getCnt() {
		return _cnt;
	}

	@Override
	public de.haumacher.wizard.msg.Bid setCnt(int value) {
		internalSetCnt(value);
		return this;
	}

	/** Internal setter for {@link #getCnt()} without chain call utility. */
	protected final void internalSetCnt(int value) {
		_cnt = value;
	}

	@Override
	public final int getExpected() {
		return _expected;
	}

	@Override
	public de.haumacher.wizard.msg.Bid setExpected(int value) {
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
