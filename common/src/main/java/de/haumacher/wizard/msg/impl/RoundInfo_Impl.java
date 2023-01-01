package de.haumacher.wizard.msg.impl;

/**
 * Current player status at the end of a round.
 */
public class RoundInfo_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.RoundInfo {

	private int _points = 0;

	private int _total = 0;

	/**
	 * Creates a {@link RoundInfo_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.RoundInfo#create()
	 */
	public RoundInfo_Impl() {
		super();
	}

	@Override
	public final int getPoints() {
		return _points;
	}

	@Override
	public de.haumacher.wizard.msg.RoundInfo setPoints(int value) {
		internalSetPoints(value);
		return this;
	}

	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(int value) {
		_points = value;
	}

	@Override
	public final int getTotal() {
		return _total;
	}

	@Override
	public de.haumacher.wizard.msg.RoundInfo setTotal(int value) {
		internalSetTotal(value);
		return this;
	}

	/** Internal setter for {@link #getTotal()} without chain call utility. */
	protected final void internalSetTotal(int value) {
		_total = value;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(POINTS__PROP);
		out.value(getPoints());
		out.name(TOTAL__PROP);
		out.value(getTotal());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case POINTS__PROP: setPoints(in.nextInt()); break;
			case TOTAL__PROP: setTotal(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

}
