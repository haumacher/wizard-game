package de.haumacher.wizard.msg;

/**
 * Provides a listing of games waiting for players.
 */
public class ListGamesResult extends ResultMsg {

	/**
	 * Creates a {@link ListGamesResult} instance.
	 */
	public static ListGamesResult create() {
		return new ListGamesResult();
	}

	/** Identifier for the {@link ListGamesResult} type in JSON format. */
	public static final String LIST_GAMES_RESULT__TYPE = "ListGamesResult";

	/** @see #getGames() */
	private static final String GAMES = "games";

	private final java.util.List<Game> _games = new java.util.ArrayList<>();

	/**
	 * Creates a {@link ListGamesResult} instance.
	 *
	 * @see #create()
	 */
	protected ListGamesResult() {
		super();
	}

	/**
	 * List of all games on the server currently accepting new players.
	 */
	public final java.util.List<Game> getGames() {
		return _games;
	}

	/**
	 * @see #getGames()
	 */
	public ListGamesResult setGames(java.util.List<Game> value) {
		internalSetGames(value);
		return this;
	}
	/** Internal setter for {@link #getGames()} without chain call utility. */
	protected final void internalSetGames(java.util.List<Game> value) {
		if (value == null) throw new IllegalArgumentException("Property 'games' cannot be null.");
		_games.clear();
		_games.addAll(value);
	}


	/**
	 * Adds a value to the {@link #getGames()} list.
	 */
	public ListGamesResult addGame(Game value) {
		internalAddGame(value);
		return this;
	}

	/** Implementation of {@link #addGame(Game)} without chain call utility. */
	protected final void internalAddGame(Game value) {
		_games.add(value);
	}

	/**
	 * Removes a value from the {@link #getGames()} list.
	 */
	public final void removeGame(Game value) {
		_games.remove(value);
	}

	@Override
	public String jsonType() {
		return LIST_GAMES_RESULT__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static ListGamesResult readListGamesResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		ListGamesResult result = new ListGamesResult();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(GAMES);
		out.beginArray();
		for (Game x : getGames()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAMES: {
				in.beginArray();
				while (in.hasNext()) {
					addGame(de.haumacher.wizard.msg.Game.readGame(in));
				}
				in.endArray();
			}
			break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
