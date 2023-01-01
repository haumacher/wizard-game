package de.haumacher.wizard.msg.impl;

/**
 * Announces that a player has left a game.
 */
public class LeaveAnnounce_Impl extends de.haumacher.wizard.msg.impl.Msg_Impl implements de.haumacher.wizard.msg.LeaveAnnounce {

	private String _playerId = "";

	private String _gameId = "";

	/**
	 * Creates a {@link LeaveAnnounce_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.LeaveAnnounce#create()
	 */
	public LeaveAnnounce_Impl() {
		super();
	}

	@Override
	public final String getPlayerId() {
		return _playerId;
	}

	@Override
	public de.haumacher.wizard.msg.LeaveAnnounce setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}

	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}

	@Override
	public final String getGameId() {
		return _gameId;
	}

	@Override
	public de.haumacher.wizard.msg.LeaveAnnounce setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return LEAVE_ANNOUNCE__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID__PROP);
		out.value(getPlayerId());
		out.name(GAME_ID__PROP);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID__PROP: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case GAME_ID__PROP: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
