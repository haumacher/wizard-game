package de.haumacher.wizard.msg.impl;

/**
 * Informs idle players that a new game was created.
 */
public class GameCreated_Impl extends de.haumacher.wizard.msg.impl.Msg_Impl implements de.haumacher.wizard.msg.GameCreated {

	private String _ownerId = "";

	private de.haumacher.wizard.msg.Game _game = null;

	/**
	 * Creates a {@link GameCreated_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.GameCreated#create()
	 */
	public GameCreated_Impl() {
		super();
	}

	@Override
	public final String getOwnerId() {
		return _ownerId;
	}

	@Override
	public de.haumacher.wizard.msg.GameCreated setOwnerId(String value) {
		internalSetOwnerId(value);
		return this;
	}

	/** Internal setter for {@link #getOwnerId()} without chain call utility. */
	protected final void internalSetOwnerId(String value) {
		_ownerId = value;
	}

	@Override
	public final de.haumacher.wizard.msg.Game getGame() {
		return _game;
	}

	@Override
	public de.haumacher.wizard.msg.GameCreated setGame(de.haumacher.wizard.msg.Game value) {
		internalSetGame(value);
		return this;
	}

	/** Internal setter for {@link #getGame()} without chain call utility. */
	protected final void internalSetGame(de.haumacher.wizard.msg.Game value) {
		_game = value;
	}

	@Override
	public final boolean hasGame() {
		return _game != null;
	}

	@Override
	public String jsonType() {
		return GAME_CREATED__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(OWNER_ID__PROP);
		out.value(getOwnerId());
		if (hasGame()) {
			out.name(GAME__PROP);
			getGame().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case OWNER_ID__PROP: setOwnerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case GAME__PROP: setGame(de.haumacher.wizard.msg.Game.readGame(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
