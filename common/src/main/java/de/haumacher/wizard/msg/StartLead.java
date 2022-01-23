package de.haumacher.wizard.msg;

/**
 * Message announcing that all bids are placed.
 */
public class StartLead extends GameMsg {

	/**
	 * Creates a {@link StartLead} instance.
	 */
	public static StartLead create() {
		return new StartLead();
	}

	/** Identifier for the {@link StartLead} type in JSON format. */
	public static final String START_LEAD__TYPE = "StartLead";

	/** @see #getState() */
	private static final String STATE = "state";

	/** @see #getCurrentTrick() */
	private static final String CURRENT_TRICK = "currentTrick";

	private final java.util.Map<String, PlayerInfo> _state = new java.util.HashMap<>();

	private final java.util.List<PlayedCard> _currentTrick = new java.util.ArrayList<>();

	/**
	 * Creates a {@link StartLead} instance.
	 *
	 * @see #create()
	 */
	protected StartLead() {
		super();
	}

	/**
	 * Score information of all players by their IDs.
	 */
	public final java.util.Map<String, PlayerInfo> getState() {
		return _state;
	}

	/**
	 * @see #getState()
	 */
	public StartLead setState(java.util.Map<String, PlayerInfo> value) {
		internalSetState(value);
		return this;
	}
	/** Internal setter for {@link #getState()} without chain call utility. */
	protected final void internalSetState(java.util.Map<String, PlayerInfo> value) {
		if (value == null) throw new IllegalArgumentException("Property 'state' cannot be null.");
		_state.clear();
		_state.putAll(value);
	}


	/**
	 * Adds a key value pair to the {@link #getState()} map.
	 */
	public StartLead putState(String key, PlayerInfo value) {
		internalPutState(key, value);
		return this;
	}

	/** Implementation of {@link #putState(String, PlayerInfo)} without chain call utility. */
	protected final void  internalPutState(String key, PlayerInfo value) {
		if (_state.containsKey(key)) {
			throw new IllegalArgumentException("Property 'state' already contains a value for key '" + key + "'.");
		}
		_state.put(key, value);
	}

	/**
	 * Removes a key from the {@link #getState()} map.
	 */
	public final void removeState(String key) {
		_state.remove(key);
	}

	/**
	 * The cards played so far. Only relevant when reconnecting to a game.
	 */
	public final java.util.List<PlayedCard> getCurrentTrick() {
		return _currentTrick;
	}

	/**
	 * @see #getCurrentTrick()
	 */
	public StartLead setCurrentTrick(java.util.List<PlayedCard> value) {
		internalSetCurrentTrick(value);
		return this;
	}
	/** Internal setter for {@link #getCurrentTrick()} without chain call utility. */
	protected final void internalSetCurrentTrick(java.util.List<PlayedCard> value) {
		if (value == null) throw new IllegalArgumentException("Property 'currentTrick' cannot be null.");
		_currentTrick.clear();
		_currentTrick.addAll(value);
	}


	/**
	 * Adds a value to the {@link #getCurrentTrick()} list.
	 */
	public StartLead addCurrentTrick(PlayedCard value) {
		internalAddCurrentTrick(value);
		return this;
	}

	/** Implementation of {@link #addCurrentTrick(PlayedCard)} without chain call utility. */
	protected final void internalAddCurrentTrick(PlayedCard value) {
		_currentTrick.add(value);
	}

	/**
	 * Removes a value from the {@link #getCurrentTrick()} list.
	 */
	public final void removeCurrentTrick(PlayedCard value) {
		_currentTrick.remove(value);
	}

	@Override
	public String jsonType() {
		return START_LEAD__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static StartLead readStartLead(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		StartLead result = new StartLead();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(STATE);
		out.beginObject();
		for (java.util.Map.Entry<String,PlayerInfo> entry : getState().entrySet()) {
			out.name(entry.getKey());
			entry.getValue().writeTo(out);
		}
		out.endObject();
		out.name(CURRENT_TRICK);
		out.beginArray();
		for (PlayedCard x : getCurrentTrick()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case STATE: {
				in.beginObject();
				while (in.hasNext()) {
					putState(in.nextName(), de.haumacher.wizard.msg.PlayerInfo.readPlayerInfo(in));
				}
				in.endObject();
				break;
			}
			case CURRENT_TRICK: {
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
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
