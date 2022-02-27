package de.haumacher.wizard.msg;

/**
 * Current player status at the end of a round.
 */
public class RoundInfo extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link RoundInfo} instance.
	 */
	public static RoundInfo create() {
		return new RoundInfo();
	}

	/** Identifier for the {@link RoundInfo} type in JSON format. */
	public static final String ROUND_INFO__TYPE = "RoundInfo";

	/** @see #getPoints() */
	private static final String POINTS = "points";

	/** @see #getTotal() */
	private static final String TOTAL = "total";

	private int _points = 0;

	private int _total = 0;

	/**
	 * Creates a {@link RoundInfo} instance.
	 *
	 * @see #create()
	 */
	protected RoundInfo() {
		super();
	}

	/**
	 * The points won by the player during the last round. The number may be negative.
	 */
	public final int getPoints() {
		return _points;
	}

	/**
	 * @see #getPoints()
	 */
	public RoundInfo setPoints(int value) {
		internalSetPoints(value);
		return this;
	}
	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(int value) {
		_points = value;
	}


	/**
	 * The total amount of points the player won so far. The number may be negative.
	 */
	public final int getTotal() {
		return _total;
	}

	/**
	 * @see #getTotal()
	 */
	public RoundInfo setTotal(int value) {
		internalSetTotal(value);
		return this;
	}
	/** Internal setter for {@link #getTotal()} without chain call utility. */
	protected final void internalSetTotal(int value) {
		_total = value;
	}


	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return ROUND_INFO__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static RoundInfo readRoundInfo(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		RoundInfo result = new RoundInfo();
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
		out.name(POINTS);
		out.value(getPoints());
		out.name(TOTAL);
		out.value(getTotal());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case POINTS: setPoints(in.nextInt()); break;
			case TOTAL: setTotal(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

}
