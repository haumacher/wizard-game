package de.haumacher.wizard.msg;

/**
 * Information about a game.
 */
public interface Game extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Game} instance.
	 */
	static de.haumacher.wizard.msg.Game create() {
		return new de.haumacher.wizard.msg.impl.Game_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Game} type in JSON format. */
	String GAME__TYPE = "Game";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/** @see #getPlayers() */
	String PLAYERS__PROP = "players";

	/**
	 * A unique identifier of the game used to reference this game in messages.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.Game setGameId(String value);

	/**
	 * The players that have joined this game.
	 */
	java.util.List<de.haumacher.wizard.msg.Player> getPlayers();

	/**
	 * @see #getPlayers()
	 */
	de.haumacher.wizard.msg.Game setPlayers(java.util.List<? extends de.haumacher.wizard.msg.Player> value);

	/**
	 * Adds a value to the {@link #getPlayers()} list.
	 */
	de.haumacher.wizard.msg.Game addPlayer(de.haumacher.wizard.msg.Player value);

	/**
	 * Removes a value from the {@link #getPlayers()} list.
	 */
	void removePlayer(de.haumacher.wizard.msg.Player value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Game readGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Game_Impl result = new de.haumacher.wizard.msg.impl.Game_Impl();
		result.readContent(in);
		return result;
	}

}
