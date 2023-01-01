package de.haumacher.wizard.msg.impl;

/**
 * Score info for a player.
 */
public class PlayerScore_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.PlayerScore {

	private de.haumacher.wizard.msg.Player _player = null;

	private int _points = 0;

	/**
	 * Creates a {@link PlayerScore_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.PlayerScore#create()
	 */
	public PlayerScore_Impl() {
		super();
	}

	@Override
	public final de.haumacher.wizard.msg.Player getPlayer() {
		return _player;
	}

	@Override
	public de.haumacher.wizard.msg.PlayerScore setPlayer(de.haumacher.wizard.msg.Player value) {
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
	public de.haumacher.wizard.msg.PlayerScore setPoints(int value) {
		internalSetPoints(value);
		return this;
	}

	/** Internal setter for {@link #getPoints()} without chain call utility. */
	protected final void internalSetPoints(int value) {
		_points = value;
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
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER__PROP: setPlayer(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			case POINTS__PROP: setPoints(in.nextInt()); break;
			default: super.readField(in, field);
		}
	}

}
