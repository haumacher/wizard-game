package de.haumacher.wizard.msg;

/**
 * Message sent after each round that announces the points received in this round.
 */
public class FinishRound extends GameMsg {

	/**
	 * Creates a {@link FinishRound} instance.
	 */
	public static FinishRound create() {
		return new FinishRound();
	}

	/** Identifier for the {@link FinishRound} type in JSON format. */
	public static final String FINISH_ROUND__TYPE = "FinishRound";

	/** @see #getPoints() */
	private static final String POINTS = "points";

	private final java.util.Map<String, Integer> _points = new java.util.HashMap<>();

	/**
	 * Creates a {@link FinishRound} instance.
	 *
	 * @see #create()
	 */
	protected FinishRound() {
		super();
	}

	/**
	 * For each player ID the number of points this player wins. The number may be negative.
	 */
	public final java.util.Map<String, Integer> getPoints() {
		return _points;
	}

	/**
	 * @see #getPoints()
	 */
	public FinishRound setPoints(java.util.Map<String, Integer> value) {
		internalSetPoints(value);
		return this;
	}
	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(java.util.Map<String, Integer> value) {
		if (value == null) throw new IllegalArgumentException("Property 'points' cannot be null.");
		_points.clear();
		_points.putAll(value);
	}


	/**
	 * Adds a key value pair to the {@link #getPoints()} map.
	 */
	public FinishRound putPoint(String key, int value) {
		internalPutPoint(key, value);
		return this;
	}

	/** Implementation of {@link #putPoint(String, int)} without chain call utility. */
	protected final void  internalPutPoint(String key, int value) {
		if (_points.containsKey(key)) {
			throw new IllegalArgumentException("Property 'points' already contains a value for key '" + key + "'.");
		}
		_points.put(key, value);
	}

	/**
	 * Removes a key from the {@link #getPoints()} map.
	 */
	public final void removePoint(String key) {
		_points.remove(key);
	}

	@Override
	public String jsonType() {
		return FINISH_ROUND__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static FinishRound readFinishRound(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		FinishRound result = new FinishRound();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(POINTS);
		out.beginObject();
		for (java.util.Map.Entry<String,Integer> entry : getPoints().entrySet()) {
			out.name(entry.getKey());
			out.value(entry.getValue());
		}
		out.endObject();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case POINTS: {
				in.beginObject();
				while (in.hasNext()) {
					putPoint(in.nextName(), in.nextInt());
				}
				in.endObject();
				break;
			}
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
