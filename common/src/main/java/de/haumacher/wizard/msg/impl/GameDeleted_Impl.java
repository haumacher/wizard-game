package de.haumacher.wizard.msg.impl;

/**
 * Informs idle players that a game that was waiting for players has been deleted.
 */
public class GameDeleted_Impl extends de.haumacher.wizard.msg.impl.Msg_Impl implements de.haumacher.wizard.msg.GameDeleted {

	private String _gameId = "";

	/**
	 * Creates a {@link GameDeleted_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.GameDeleted#create()
	 */
	public GameDeleted_Impl() {
		super();
	}

	@Override
	public final String getGameId() {
		return _gameId;
	}

	@Override
	public de.haumacher.wizard.msg.GameDeleted setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return GAME_DELETED__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(GAME_ID__PROP);
		out.value(getGameId());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME_ID__PROP: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
