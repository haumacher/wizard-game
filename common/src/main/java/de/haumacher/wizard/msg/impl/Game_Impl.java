package de.haumacher.wizard.msg.impl;

/**
 * Information about a game.
 */
public class Game_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.Game {

	private String _gameId = "";

	private final java.util.List<de.haumacher.wizard.msg.Player> _players = new java.util.ArrayList<>();

	/**
	 * Creates a {@link Game_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Game#create()
	 */
	public Game_Impl() {
		super();
	}

	@Override
	public final String getGameId() {
		return _gameId;
	}

	@Override
	public de.haumacher.wizard.msg.Game setGameId(String value) {
		internalSetGameId(value);
		return this;
	}

	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.Player> getPlayers() {
		return _players;
	}

	@Override
	public de.haumacher.wizard.msg.Game setPlayers(java.util.List<? extends de.haumacher.wizard.msg.Player> value) {
		internalSetPlayers(value);
		return this;
	}

	/** Internal setter for {@link #getPlayers()} without chain call utility. */
	protected final void internalSetPlayers(java.util.List<? extends de.haumacher.wizard.msg.Player> value) {
		if (value == null) throw new IllegalArgumentException("Property 'players' cannot be null.");
		_players.clear();
		_players.addAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.Game addPlayer(de.haumacher.wizard.msg.Player value) {
		internalAddPlayer(value);
		return this;
	}

	/** Implementation of {@link #addPlayer(de.haumacher.wizard.msg.Player)} without chain call utility. */
	protected final void internalAddPlayer(de.haumacher.wizard.msg.Player value) {
		_players.add(value);
	}

	@Override
	public final void removePlayer(de.haumacher.wizard.msg.Player value) {
		_players.remove(value);
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(GAME_ID__PROP);
		out.value(getGameId());
		out.name(PLAYERS__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.Player x : getPlayers()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME_ID__PROP: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case PLAYERS__PROP: {
				in.beginArray();
				while (in.hasNext()) {
					addPlayer(de.haumacher.wizard.msg.Player.readPlayer(in));
				}
				in.endArray();
			}
			break;
			default: super.readField(in, field);
		}
	}

}
