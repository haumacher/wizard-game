package de.haumacher.wizard.msg.impl;

/**
 * Requests a player to select the trump suit in case the trump card was a wizard.
 */
public class RequestTrumpSelection_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.RequestTrumpSelection {

	private String _playerId = "";

	/**
	 * Creates a {@link RequestTrumpSelection_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.RequestTrumpSelection#create()
	 */
	public RequestTrumpSelection_Impl() {
		super();
	}

	@Override
	public final String getPlayerId() {
		return _playerId;
	}

	@Override
	public de.haumacher.wizard.msg.RequestTrumpSelection setPlayerId(String value) {
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
