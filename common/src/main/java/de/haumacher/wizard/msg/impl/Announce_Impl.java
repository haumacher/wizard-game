package de.haumacher.wizard.msg.impl;

/**
 * Message to forward a {@link GameCmd} sent by one player of a game to all other players.
 */
public class Announce_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.Announce {

	private String _playerId = "";

	private de.haumacher.wizard.msg.GameCmd _cmd = null;

	/**
	 * Creates a {@link Announce_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Announce#create()
	 */
	public Announce_Impl() {
		super();
	}

	@Override
	public final String getPlayerId() {
		return _playerId;
	}

	@Override
	public de.haumacher.wizard.msg.Announce setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}

	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}

	@Override
	public final de.haumacher.wizard.msg.GameCmd getCmd() {
		return _cmd;
	}

	@Override
	public de.haumacher.wizard.msg.Announce setCmd(de.haumacher.wizard.msg.GameCmd value) {
		internalSetCmd(value);
		return this;
	}

	/** Internal setter for {@link #getCmd()} without chain call utility. */
	protected final void internalSetCmd(de.haumacher.wizard.msg.GameCmd value) {
		_cmd = value;
	}

	@Override
	public final boolean hasCmd() {
		return _cmd != null;
	}

	@Override
	public String jsonType() {
		return ANNOUNCE__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID__PROP);
		out.value(getPlayerId());
		if (hasCmd()) {
			out.name(CMD__PROP);
			getCmd().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID__PROP: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case CMD__PROP: setCmd(de.haumacher.wizard.msg.GameCmd.readGameCmd(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
