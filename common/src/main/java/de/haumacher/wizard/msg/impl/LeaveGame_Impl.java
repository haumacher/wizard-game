package de.haumacher.wizard.msg.impl;

/**
 * Command to request leaving a game.
 */
public class LeaveGame_Impl extends de.haumacher.wizard.msg.impl.Cmd_Impl implements de.haumacher.wizard.msg.LeaveGame {

	private String _gameId = "";

	/**
	 * Creates a {@link LeaveGame_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.LeaveGame#create()
	 */
	public LeaveGame_Impl() {
		super();
	}

	@Override
	public final String getGameId() {
		return _gameId;
	}

	@Override
	public de.haumacher.wizard.msg.LeaveGame setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public String jsonType() {
		return LEAVE_GAME__TYPE;
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
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
