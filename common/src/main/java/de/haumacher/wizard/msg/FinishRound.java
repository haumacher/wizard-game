package de.haumacher.wizard.msg;

/**
 * Message sent after each round that announces the points received in this round.
 */
public class FinishRound extends GameMsg {

	/**
	 * Creates a {@link FinishRound} instance.
	 */
	public static FinishRound create() {
		return new de.haumacher.wizard.msg.FinishRound();
	}

	/** Identifier for the {@link FinishRound} type in JSON format. */
	public static final String FINISH_ROUND__TYPE = "FinishRound";

	/** @see #getInfo() */
	private static final String INFO__PROP = "info";

	private final java.util.Map<String, RoundInfo> _info = new java.util.HashMap<>();

	/**
	 * Creates a {@link FinishRound} instance.
	 *
	 * @see FinishRound#create()
	 */
	protected FinishRound() {
		super();
	}

	/**
	 * For each player ID the number of points this player wins. The number may be negative.
	 */
	public final java.util.Map<String, RoundInfo> getInfo() {
		return _info;
	}

	/**
	 * @see #getInfo()
	 */
	public FinishRound setInfo(java.util.Map<String, RoundInfo> value) {
		internalSetInfo(value);
		return this;
	}

	/** Internal setter for {@link #getInfo()} without chain call utility. */
	protected final void internalSetInfo(java.util.Map<String, RoundInfo> value) {
		if (value == null) throw new IllegalArgumentException("Property 'info' cannot be null.");
		_info.clear();
		_info.putAll(value);
	}

	/**
	 * Adds a key value pair to the {@link #getInfo()} map.
	 */
	public FinishRound putInfo(String key, RoundInfo value) {
		internalPutInfo(key, value);
		return this;
	}

	/** Implementation of {@link #putInfo(String, RoundInfo)} without chain call utility. */
	protected final void  internalPutInfo(String key, RoundInfo value) {
		if (_info.containsKey(key)) {
			throw new IllegalArgumentException("Property 'info' already contains a value for key '" + key + "'.");
		}
		_info.put(key, value);
	}

	/**
	 * Removes a key from the {@link #getInfo()} map.
	 */
	public final void removeInfo(String key) {
		_info.remove(key);
	}

	@Override
	public String jsonType() {
		return FINISH_ROUND__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static FinishRound readFinishRound(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.FinishRound result = new de.haumacher.wizard.msg.FinishRound();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(INFO__PROP);
		out.beginObject();
		for (java.util.Map.Entry<String,RoundInfo> entry : getInfo().entrySet()) {
			out.name(entry.getKey());
			entry.getValue().writeTo(out);
		}
		out.endObject();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case INFO__PROP: {
				in.beginObject();
				while (in.hasNext()) {
					putInfo(in.nextName(), de.haumacher.wizard.msg.RoundInfo.readRoundInfo(in));
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
