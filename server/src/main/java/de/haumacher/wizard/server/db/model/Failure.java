package de.haumacher.wizard.server.db.model;

/**
 * Generic error response.
 */
public class Failure extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.msgbuf.binary.BinaryDataObject, de.haumacher.msgbuf.observer.Observable {

	/**
	 * Creates a {@link Failure} instance.
	 */
	public static Failure create() {
		return new Failure();
	}

	/** Identifier for the {@link Failure} type in JSON format. */
	public static final String FAILURE__TYPE = "Failure";

	/** @see #getMessage() */
	public static final String MESSAGE = "message";

	/** Identifier for the property {@link #getMessage()} in binary format. */
	public static final int MESSAGE__ID = 1;

	private String _message = "";

	/**
	 * Creates a {@link Failure} instance.
	 *
	 * @see #create()
	 */
	protected Failure() {
		super();
	}

	/**
	 * Generic error message.
	 */
	public final String getMessage() {
		return _message;
	}

	/**
	 * @see #getMessage()
	 */
	public Failure setMessage(String value) {
		internalSetMessage(value);
		return this;
	}
	/** Internal setter for {@link #getMessage()} without chain call utility. */
	protected final void internalSetMessage(String value) {
		_listener.beforeSet(this, MESSAGE, value);
		_message = value;
	}


	protected de.haumacher.msgbuf.observer.Listener _listener = de.haumacher.msgbuf.observer.Listener.NONE;

	@Override
	public Failure registerListener(de.haumacher.msgbuf.observer.Listener l) {
		internalRegisterListener(l);
		return this;
	}

	protected final void internalRegisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.register(_listener, l);
	}

	@Override
	public Failure unregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		internalUnregisterListener(l);
		return this;
	}

	protected final void internalUnregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.unregister(_listener, l);
	}

	@Override
	public String jsonType() {
		return FAILURE__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			MESSAGE));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case MESSAGE: return getMessage();
			default: return de.haumacher.msgbuf.observer.Observable.super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case MESSAGE: setMessage((String) value); break;
		}
	}

	/** Reads a new instance from the given reader. */
	public static Failure readFailure(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Failure result = new Failure();
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
		out.name(MESSAGE);
		out.value(getMessage());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case MESSAGE: setMessage(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
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
		out.name(MESSAGE__ID);
		out.value(getMessage());
	}

	/** Reads a new instance from the given reader. */
	public static Failure readFailure(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		Failure result = new Failure();
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
			case MESSAGE__ID: setMessage(in.nextString()); break;
			default: in.skipValue(); 
		}
	}

}
