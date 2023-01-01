package de.haumacher.wizard.msg;

/**
 * Informs idle players that a game that was waiting for players has been deleted.
 */
public interface GameDeleted extends Msg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.GameDeleted} instance.
	 */
	static de.haumacher.wizard.msg.GameDeleted create() {
		return new de.haumacher.wizard.msg.impl.GameDeleted_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.GameDeleted} type in JSON format. */
	String GAME_DELETED__TYPE = "GameDeleted";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/**
	 * The ID of the game that has been deleted.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.GameDeleted setGameId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.GameDeleted readGameDeleted(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.GameDeleted_Impl result = new de.haumacher.wizard.msg.impl.GameDeleted_Impl();
		result.readContent(in);
		return result;
	}

}
