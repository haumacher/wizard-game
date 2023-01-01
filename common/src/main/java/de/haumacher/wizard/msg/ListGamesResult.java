package de.haumacher.wizard.msg;

/**
 * Provides a listing of games waiting for players.
 */
public interface ListGamesResult extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.ListGamesResult} instance.
	 */
	static de.haumacher.wizard.msg.ListGamesResult create() {
		return new de.haumacher.wizard.msg.impl.ListGamesResult_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.ListGamesResult} type in JSON format. */
	String LIST_GAMES_RESULT__TYPE = "ListGamesResult";

	/** @see #getGames() */
	String GAMES__PROP = "games";

	/**
	 * List of all games on the server currently accepting new players.
	 */
	java.util.List<de.haumacher.wizard.msg.Game> getGames();

	/**
	 * @see #getGames()
	 */
	de.haumacher.wizard.msg.ListGamesResult setGames(java.util.List<? extends de.haumacher.wizard.msg.Game> value);

	/**
	 * Adds a value to the {@link #getGames()} list.
	 */
	de.haumacher.wizard.msg.ListGamesResult addGame(de.haumacher.wizard.msg.Game value);

	/**
	 * Removes a value from the {@link #getGames()} list.
	 */
	void removeGame(de.haumacher.wizard.msg.Game value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.ListGamesResult readListGamesResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.ListGamesResult_Impl result = new de.haumacher.wizard.msg.impl.ListGamesResult_Impl();
		result.readContent(in);
		return result;
	}

}
