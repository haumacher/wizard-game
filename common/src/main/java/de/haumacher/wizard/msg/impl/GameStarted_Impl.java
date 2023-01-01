package de.haumacher.wizard.msg.impl;

/**
 * Message sent in response to {@link StartGame} to announce the final game configuration.
 */
public class GameStarted_Impl extends de.haumacher.wizard.msg.impl.Msg_Impl implements de.haumacher.wizard.msg.GameStarted {

	private de.haumacher.wizard.msg.Game _game = null;

	/**
	 * Creates a {@link GameStarted_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.GameStarted#create()
	 */
	public GameStarted_Impl() {
		super();
	}

	@Override
	public final de.haumacher.wizard.msg.Game getGame() {
		return _game;
	}

	@Override
	public de.haumacher.wizard.msg.GameStarted setGame(de.haumacher.wizard.msg.Game value) {
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
		return GAME_STARTED__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasGame()) {
			out.name(GAME__PROP);
			getGame().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME__PROP: setGame(de.haumacher.wizard.msg.Game.readGame(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
