package de.haumacher.wizard.msg.impl;

/**
 * Message sent after each round that announces the points received in this round.
 */
public class FinishRound_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.FinishRound {

	private final java.util.Map<String, de.haumacher.wizard.msg.RoundInfo> _info = new java.util.HashMap<>();

	/**
	 * Creates a {@link FinishRound_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.FinishRound#create()
	 */
	public FinishRound_Impl() {
		super();
	}

	@Override
	public final java.util.Map<String, de.haumacher.wizard.msg.RoundInfo> getInfo() {
		return _info;
	}

	@Override
	public de.haumacher.wizard.msg.FinishRound setInfo(java.util.Map<String, de.haumacher.wizard.msg.RoundInfo> value) {
		internalSetInfo(value);
		return this;
	}

	/** Internal setter for {@link #getInfo()} without chain call utility. */
	protected final void internalSetInfo(java.util.Map<String, de.haumacher.wizard.msg.RoundInfo> value) {
		if (value == null) throw new IllegalArgumentException("Property 'info' cannot be null.");
		_info.clear();
		_info.putAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.FinishRound putInfo(String key, de.haumacher.wizard.msg.RoundInfo value) {
		internalPutInfo(key, value);
		return this;
	}

	/** Implementation of {@link #putInfo(String, de.haumacher.wizard.msg.RoundInfo)} without chain call utility. */
	protected final void  internalPutInfo(String key, de.haumacher.wizard.msg.RoundInfo value) {
		if (_info.containsKey(key)) {
			throw new IllegalArgumentException("Property 'info' already contains a value for key '" + key + "'.");
		}
		_info.put(key, value);
	}

	@Override
	public final void removeInfo(String key) {
		_info.remove(key);
	}

	@Override
	public String jsonType() {
		return FINISH_ROUND__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(INFO__PROP);
		out.beginObject();
		for (java.util.Map.Entry<String,de.haumacher.wizard.msg.RoundInfo> entry : getInfo().entrySet()) {
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
