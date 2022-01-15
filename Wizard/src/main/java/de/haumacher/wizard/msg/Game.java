package de.haumacher.wizard.msg;

public class Game extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link Game} instance.
	 */
	public static Game create() {
		return new Game();
	}

	/** Identifier for the {@link Game} type in JSON format. */
	public static final String GAME__TYPE = "Game";

	/** @see #getGameId() */
	private static final String GAME_ID = "gameId";

	/** @see #getPlayers() */
	private static final String PLAYERS = "players";

	private String _gameId = "";

	private final java.util.List<Player> _players = new java.util.ArrayList<>();

	/**
	 * Creates a {@link Game} instance.
	 *
	 * @see #create()
	 */
	protected Game() {
		super();
	}

	public final String getGameId() {
		return _gameId;
	}

	/**
	 * @see #getGameId()
	 */
	public Game setGameId(String value) {
		internalSetGameId(value);
		return this;
	}
	/** Internal setter for {@link #getGameId()} without chain call utility. */
	protected final void internalSetGameId(String value) {
		_gameId = value;
	}


	public final java.util.List<Player> getPlayers() {
		return _players;
	}

	/**
	 * @see #getPlayers()
	 */
	public Game setPlayers(java.util.List<Player> value) {
		internalSetPlayers(value);
		return this;
	}
	/** Internal setter for {@link #getPlayers()} without chain call utility. */
	protected final void internalSetPlayers(java.util.List<Player> value) {
		if (value == null) throw new IllegalArgumentException("Property 'players' cannot be null.");
		_players.clear();
		_players.addAll(value);
	}


	/**
	 * Adds a value to the {@link #getPlayers()} list.
	 */
	public Game addPlayer(Player value) {
		internalAddPlayer(value);
		return this;
	}

	/** Implementation of {@link #addPlayer(Player)} without chain call utility. */
	protected final void internalAddPlayer(Player value) {
		_players.add(value);
	}

	/**
	 * Removes a value from the {@link #getPlayers()} list.
	 */
	public final void removePlayer(Player value) {
		_players.remove(value);
	}

	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return GAME__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Game readGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Game result = new Game();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(GAME_ID);
		out.value(getGameId());
		out.name(PLAYERS);
		out.beginArray();
		for (Player x : getPlayers()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAME_ID: setGameId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case PLAYERS: {
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
