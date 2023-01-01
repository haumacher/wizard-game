package de.haumacher.wizard.msg;

/**
 * Requests joining a game.
 */
public interface JoinGame extends Cmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.JoinGame} instance.
	 */
	static de.haumacher.wizard.msg.JoinGame create() {
		return new de.haumacher.wizard.msg.impl.JoinGame_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.JoinGame} type in JSON format. */
	String JOIN_GAME__TYPE = "JoinGame";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/**
	 * The ID of the game the sender wants to join.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.JoinGame setGameId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.JoinGame readJoinGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.JoinGame_Impl result = new de.haumacher.wizard.msg.impl.JoinGame_Impl();
		result.readContent(in);
		return result;
	}

}
