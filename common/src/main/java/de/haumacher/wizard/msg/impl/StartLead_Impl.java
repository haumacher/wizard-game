package de.haumacher.wizard.msg.impl;

/**
 * Message announcing that all bids are placed.
 */
public class StartLead_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.StartLead {

	private final java.util.Map<String, de.haumacher.wizard.msg.PlayerInfo> _state = new java.util.HashMap<>();

	private final java.util.List<de.haumacher.wizard.msg.PlayedCard> _currentTrick = new java.util.ArrayList<>();

	/**
	 * Creates a {@link StartLead_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.StartLead#create()
	 */
	public StartLead_Impl() {
		super();
	}

	@Override
	public final java.util.Map<String, de.haumacher.wizard.msg.PlayerInfo> getState() {
		return _state;
	}

	@Override
	public de.haumacher.wizard.msg.StartLead setState(java.util.Map<String, de.haumacher.wizard.msg.PlayerInfo> value) {
		internalSetState(value);
		return this;
	}

	/** Internal setter for {@link #getState()} without chain call utility. */
	protected final void internalSetState(java.util.Map<String, de.haumacher.wizard.msg.PlayerInfo> value) {
		if (value == null) throw new IllegalArgumentException("Property 'state' cannot be null.");
		_state.clear();
		_state.putAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.StartLead putState(String key, de.haumacher.wizard.msg.PlayerInfo value) {
		internalPutState(key, value);
		return this;
	}

	/** Implementation of {@link #putState(String, de.haumacher.wizard.msg.PlayerInfo)} without chain call utility. */
	protected final void  internalPutState(String key, de.haumacher.wizard.msg.PlayerInfo value) {
		if (_state.containsKey(key)) {
			throw new IllegalArgumentException("Property 'state' already contains a value for key '" + key + "'.");
		}
		_state.put(key, value);
	}

	@Override
	public final void removeState(String key) {
		_state.remove(key);
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.PlayedCard> getCurrentTrick() {
		return _currentTrick;
	}

	@Override
	public de.haumacher.wizard.msg.StartLead setCurrentTrick(java.util.List<? extends de.haumacher.wizard.msg.PlayedCard> value) {
		internalSetCurrentTrick(value);
		return this;
	}

	/** Internal setter for {@link #getCurrentTrick()} without chain call utility. */
	protected final void internalSetCurrentTrick(java.util.List<? extends de.haumacher.wizard.msg.PlayedCard> value) {
		if (value == null) throw new IllegalArgumentException("Property 'currentTrick' cannot be null.");
		_currentTrick.clear();
		_currentTrick.addAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.StartLead addCurrentTrick(de.haumacher.wizard.msg.PlayedCard value) {
		internalAddCurrentTrick(value);
		return this;
	}

	/** Implementation of {@link #addCurrentTrick(de.haumacher.wizard.msg.PlayedCard)} without chain call utility. */
	protected final void internalAddCurrentTrick(de.haumacher.wizard.msg.PlayedCard value) {
		_currentTrick.add(value);
	}

	@Override
	public final void removeCurrentTrick(de.haumacher.wizard.msg.PlayedCard value) {
		_currentTrick.remove(value);
	}

	@Override
	public String jsonType() {
		return START_LEAD__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(STATE__PROP);
		out.beginObject();
		for (java.util.Map.Entry<String,de.haumacher.wizard.msg.PlayerInfo> entry : getState().entrySet()) {
			out.name(entry.getKey());
			entry.getValue().writeTo(out);
		}
		out.endObject();
		out.name(CURRENT_TRICK__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.PlayedCard x : getCurrentTrick()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case STATE__PROP: {
				in.beginObject();
				while (in.hasNext()) {
					putState(in.nextName(), de.haumacher.wizard.msg.PlayerInfo.readPlayerInfo(in));
				}
				in.endObject();
				break;
			}
			case CURRENT_TRICK__PROP: {
				in.beginArray();
				while (in.hasNext()) {
					addCurrentTrick(de.haumacher.wizard.msg.PlayedCard.readPlayedCard(in));
				}
				in.endArray();
			}
			break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
