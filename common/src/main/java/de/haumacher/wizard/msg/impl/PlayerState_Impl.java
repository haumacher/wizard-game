package de.haumacher.wizard.msg.impl;

/**
 * Internal information kept for each player of a game on the server.
 */
public class PlayerState_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.PlayerState {

	private de.haumacher.wizard.msg.Player _player = null;

	private int _points = 0;

	private de.haumacher.wizard.msg.RoundState _roundState = null;

	/**
	 * Creates a {@link PlayerState_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.PlayerState#create()
	 */
	public PlayerState_Impl() {
		super();
	}

	@Override
	public final de.haumacher.wizard.msg.Player getPlayer() {
		return _player;
	}

	@Override
	public de.haumacher.wizard.msg.PlayerState setPlayer(de.haumacher.wizard.msg.Player value) {
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
	public final int getPoints() {
		return _points;
	}

	@Override
	public de.haumacher.wizard.msg.PlayerState setPoints(int value) {
		internalSetPoints(value);
		return this;
	}

	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(int value) {
		_points = value;
	}

	@Override
	public final de.haumacher.wizard.msg.RoundState getRoundState() {
		return _roundState;
	}

	@Override
	public de.haumacher.wizard.msg.PlayerState setRoundState(de.haumacher.wizard.msg.RoundState value) {
		internalSetRoundState(value);
		return this;
	}

	/** Internal setter for {@link #getRoundState()} without chain call utility. */
	protected final void internalSetRoundState(de.haumacher.wizard.msg.RoundState value) {
		_roundState = value;
	}

	@Override
	public final boolean hasRoundState() {
		return _roundState != null;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasPlayer()) {
			out.name(PLAYER__PROP);
			getPlayer().writeTo(out);
		}
		out.name(POINTS__PROP);
		out.value(getPoints());
		if (hasRoundState()) {
			out.name(ROUND_STATE__PROP);
			getRoundState().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER__PROP: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			case POINTS__PROP: setPoints(in.nextInt()); break;
			case ROUND_STATE__PROP: setRoundState(de.haumacher.wizard.msg.RoundState.readRoundState(in)); break;
			default: super.readField(in, field);
		}
	}

}
