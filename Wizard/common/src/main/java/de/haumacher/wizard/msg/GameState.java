package de.haumacher.wizard.msg;

public class GameState extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.msgbuf.binary.BinaryDataObject, de.haumacher.msgbuf.observer.Observable {

	/**
	 * Creates a {@link GameState} instance.
	 */
	public static GameState create() {
		return new GameState();
	}

	/** Identifier for the {@link GameState} type in JSON format. */
	public static final String GAME_STATE__TYPE = "GameState";

	/** @see #getPoints() */
	public static final String POINTS = "points";

	/** Identifier for the property {@link #getPoints()} in binary format. */
	public static final int POINTS__ID = 1;

	private final java.util.Map<String, Integer> _points = new java.util.HashMap<>();

	/**
	 * Creates a {@link GameState} instance.
	 *
	 * @see #create()
	 */
	protected GameState() {
		super();
	}

	public final java.util.Map<String, Integer> getPoints() {
		return _points;
	}

	/**
	 * @see #getPoints()
	 */
	public GameState setPoints(java.util.Map<String, Integer> value) {
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
	public GameState putPoint(String key, int value) {
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

	protected de.haumacher.msgbuf.observer.Listener _listener = de.haumacher.msgbuf.observer.Listener.NONE;

	@Override
	public GameState registerListener(de.haumacher.msgbuf.observer.Listener l) {
		internalRegisterListener(l);
		return this;
	}

	protected final void internalRegisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.register(_listener, l);
	}

	@Override
	public GameState unregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		internalUnregisterListener(l);
		return this;
	}

	protected final void internalUnregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.unregister(_listener, l);
	}

	@Override
	public String jsonType() {
		return GAME_STATE__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			POINTS));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case POINTS: return getPoints();
			default: return de.haumacher.msgbuf.observer.Observable.super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case POINTS: setPoints((java.util.Map<String, Integer>) value); break;
		}
	}

	/** Reads a new instance from the given reader. */
	public static GameState readGameState(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		GameState result = new GameState();
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
	public final void writeTo(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		out.beginObject();
		writeFields(out);
		out.endObject();
	}

	/**
	 * Serializes all fields of this instance to the given binary output.
	 *
	 * @param out
	 *        The binary output to write to.
	 * @throws java.io.IOException If writing fails.
	 */
	protected void writeFields(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		out.name(POINTS__ID);
	}

	/** Reads a new instance from the given reader. */
	public static GameState readGameState(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		GameState result = new GameState();
		while (in.hasNext()) {
			int field = in.nextName();
			result.readField(in, field);
		}
		in.endObject();
		return result;
	}

	/** Consumes the value for the field with the given ID and assigns its value. */
	protected void readField(de.haumacher.msgbuf.binary.DataReader in, int field) throws java.io.IOException {
		switch (field) {
			case POINTS__ID: {
				in.beginArray();
				while (in.hasNext()) {
					in.beginObject();
					String key = "";
					int value = 0;
					while (in.hasNext()) {
						switch (in.nextName()) {
							case 1: key = in.nextString(); break;
							case 2: value = in.nextInt(); break;
							default: in.skipValue(); break;
						}
					}
					putPoint(key, value);
					in.endObject();
				}
				in.endArray();
				break;
			}
			default: in.skipValue(); 
		}
	}

}
