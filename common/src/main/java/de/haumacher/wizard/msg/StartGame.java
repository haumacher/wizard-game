package de.haumacher.wizard.msg;

/**
 * Informs members of a game and idle players that a game has started and is not accepting players anymore.
 */
public interface StartGame extends Cmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.StartGame} instance.
	 */
	static de.haumacher.wizard.msg.StartGame create() {
		return new de.haumacher.wizard.msg.impl.StartGame_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.StartGame} type in JSON format. */
	String START_GAME__TYPE = "StartGame";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/**
	 * The ID of the game that has started.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.StartGame setGameId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.StartGame readStartGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.StartGame_Impl result = new de.haumacher.wizard.msg.impl.StartGame_Impl();
		result.readContent(in);
		return result;
	}

}
