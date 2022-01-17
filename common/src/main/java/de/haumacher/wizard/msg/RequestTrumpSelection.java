package de.haumacher.wizard.msg;

/**
 * Requests a player to select the trump suit in case the trump card was a wizard.
 */
public class RequestTrumpSelection extends Msg {

	/**
	 * Creates a {@link RequestTrumpSelection} instance.
	 */
	public static RequestTrumpSelection create() {
		return new RequestTrumpSelection();
	}

	/** Identifier for the {@link RequestTrumpSelection} type in JSON format. */
	public static final String REQUEST_TRUMP_SELECTION__TYPE = "RequestTrumpSelection";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID = "playerId";

	private String _playerId = "";

	/**
	 * Creates a {@link RequestTrumpSelection} instance.
	 *
	 * @see #create()
	 */
	protected RequestTrumpSelection() {
		super();
	}

	/**
	 * The ID of the player that is expected to select the trump suit.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public RequestTrumpSelection setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}
	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}


	@Override
	public String jsonType() {
		return REQUEST_TRUMP_SELECTION__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static RequestTrumpSelection readRequestTrumpSelection(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		RequestTrumpSelection result = new RequestTrumpSelection();
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
