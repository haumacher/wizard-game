package de.haumacher.wizard.msg;

/**
 * Message sent to all players that announces the player that is about to put a card on the table.
 */
public class RequestPut extends Msg {

	/**
	 * Creates a {@link RequestPut} instance.
	 */
	public static RequestPut create() {
		return new RequestPut();
	}

	/** Identifier for the {@link RequestPut} type in JSON format. */
	public static final String REQUEST_PUT__TYPE = "RequestPut";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID = "playerId";

	private String _playerId = "";

	/**
	 * Creates a {@link RequestPut} instance.
	 *
	 * @see #create()
	 */
	protected RequestPut() {
		super();
	}

	/**
	 * The ID of the player that is expected to send a {@link Put} message. All other playes are only informed about the player that is in command.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public RequestPut setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}
	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}


	@Override
	public String jsonType() {
		return REQUEST_PUT__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static RequestPut readRequestPut(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		RequestPut result = new RequestPut();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID);
		out.value(getPlayerId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
