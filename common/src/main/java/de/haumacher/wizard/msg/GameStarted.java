package de.haumacher.wizard.msg;

/**
 * Message sent in response to {@link StartGame} to announce the final game configuration.
 */
public interface GameStarted extends Msg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.GameStarted} instance.
	 */
	static de.haumacher.wizard.msg.GameStarted create() {
		return new de.haumacher.wizard.msg.impl.GameStarted_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.GameStarted} type in JSON format. */
	String GAME_STARTED__TYPE = "GameStarted";

	/** @see #getGame() */
	String GAME__PROP = "game";

	/**
	 * The final settings of the game that is started
	 */
	de.haumacher.wizard.msg.Game getGame();

	/**
	 * @see #getGame()
	 */
	de.haumacher.wizard.msg.GameStarted setGame(de.haumacher.wizard.msg.Game value);

	/**
	 * Checks, whether {@link #getGame()} has a value.
	 */
	boolean hasGame();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.GameStarted readGameStarted(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.GameStarted_Impl result = new de.haumacher.wizard.msg.impl.GameStarted_Impl();
		result.readContent(in);
		return result;
	}

}
