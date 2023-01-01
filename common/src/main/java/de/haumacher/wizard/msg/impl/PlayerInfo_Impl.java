package de.haumacher.wizard.msg.impl;

/**
 * Score information for a single player, see {@link StartLead}.
 */
public class PlayerInfo_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.PlayerInfo {

	private int _bid = 0;

	private int _tricks = 0;

	private int _points = 0;

	/**
	 * Creates a {@link PlayerInfo_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.PlayerInfo#create()
	 */
	public PlayerInfo_Impl() {
		super();
	}

	@Override
	public final int getBid() {
		return _bid;
	}

	@Override
	public de.haumacher.wizard.msg.PlayerInfo setBid(int value) {
		internalSetBid(value);
		return this;
	}

	/** Internal setter for {@link #getBid()} without chain call utility. */
	protected final void internalSetBid(int value) {
		_bid = value;
	}

	@Override
	public final int getTricks() {
		return _tricks;
	}

	@Override
	public de.haumacher.wizard.msg.PlayerInfo setTricks(int value) {
		internalSetTricks(value);
		return this;
	}

	/** Internal setter for {@link #getTricks()} without chain call utility. */
	protected final void internalSetTricks(int value) {
		_tricks = value;
	}

	@Override
	public final int getPoints() {
		return _points;
	}

	@Override
	public de.haumacher.wizard.msg.PlayerInfo setPoints(int value) {
		internalSetPoints(value);
		return this;
	}

	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(int value) {
		_points = value;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(BID__PROP);
		out.value(getBid());
		out.name(TRICKS__PROP);
		out.value(getTricks());
		out.name(POINTS__PROP);
		out.value(getPoints());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case BID__PROP: setBid(in.nextInt()); break;
			case TRICKS__PROP: setTricks(in.nextInt()); break;
			case POINTS__PROP: setPoints(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

}
