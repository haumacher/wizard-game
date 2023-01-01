package de.haumacher.wizard.msg.impl;

/**
 * Requests a bid.
 */
public class RequestBid_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.RequestBid {

	private String _playerId = "";

	private int _expected = 0;

	private int _round = 0;

	/**
	 * Creates a {@link RequestBid_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.RequestBid#create()
	 */
	public RequestBid_Impl() {
		super();
	}

	@Override
	public final String getPlayerId() {
		return _playerId;
	}

	@Override
	public de.haumacher.wizard.msg.RequestBid setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}

	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}

	@Override
	public final int getExpected() {
		return _expected;
	}

	@Override
	public de.haumacher.wizard.msg.RequestBid setExpected(int value) {
		internalSetExpected(value);
		return this;
	}

	/** Internal setter for {@link #getExpected()} without chain call utility. */
	protected final void internalSetExpected(int value) {
		_expected = value;
	}

	@Override
	public final int getRound() {
		return _round;
	}

	@Override
	public de.haumacher.wizard.msg.RequestBid setRound(int value) {
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
