package de.haumacher.wizard.msg;

/**
 * Message sent to all players that announces the player that is about to put a card on the table.
 */
public class RequestLead extends GameMsg {

	/**
	 * Creates a {@link RequestLead} instance.
	 */
	public static RequestLead create() {
		return new de.haumacher.wizard.msg.RequestLead();
	}

	/** Identifier for the {@link RequestLead} type in JSON format. */
	public static final String REQUEST_LEAD__TYPE = "RequestLead";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID__PROP = "playerId";

	private String _playerId = "";

	/**
	 * Creates a {@link RequestLead} instance.
	 *
	 * @see RequestLead#create()
	 */
	protected RequestLead() {
		super();
	}

	/**
	 * The ID of the player that is expected to send a {@link Lead} message. All other playes are only informed about the player that is in command.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public RequestLead setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}

	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}

	@Override
	public String jsonType() {
		return REQUEST_LEAD__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static RequestLead readRequestLead(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.RequestLead result = new de.haumacher.wizard.msg.RequestLead();
		result.readContent(in);
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID__PROP);
		out.value(getPlayerId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID__PROP: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
