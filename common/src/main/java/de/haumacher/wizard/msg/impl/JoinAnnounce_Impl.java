package de.haumacher.wizard.msg.impl;

/**
 * Announces that a player has joined a game.
 */
public class JoinAnnounce_Impl extends de.haumacher.wizard.msg.impl.Msg_Impl implements de.haumacher.wizard.msg.JoinAnnounce {

	private de.haumacher.wizard.msg.Player _player = null;

	private String _gameId = "";

	/**
	 * Creates a {@link JoinAnnounce_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.JoinAnnounce#create()
	 */
	public JoinAnnounce_Impl() {
		super();
	}

	@Override
	public final de.haumacher.wizard.msg.Player getPlayer() {
		return _player;
	}

	@Override
	public de.haumacher.wizard.msg.JoinAnnounce setPlayer(de.haumacher.wizard.msg.Player value) {
		internalSetPlayer(value);
		return this;
	}

	/** Internal setter for {@link #getPlayer()} without chain call utility. */
	protected final void internalSetPlayer(de.haumacher.wizard.msg.Player value) {
		_player = value;
	}

	@Override
	public final boolean hasPlayer() {
		return _player != null;
	}

	@Override
	public final String getGameId() {
		return _gameId;
	}

	@Override
	public de.haumacher.wizard.msg.JoinAnnounce setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return JOIN_ANNOUNCE__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasPlayer()) {
			out.name(PLAYER__PROP);
			getPlayer().writeTo(out);
		}
		out.name(GAME_ID__PROP);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER__PROP: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			case GAME_ID__PROP: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
