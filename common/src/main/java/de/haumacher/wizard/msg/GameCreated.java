package de.haumacher.wizard.msg;

/**
 * Informs idle players that a new game was created.
 */
public interface GameCreated extends Msg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.GameCreated} instance.
	 */
	static de.haumacher.wizard.msg.GameCreated create() {
		return new de.haumacher.wizard.msg.impl.GameCreated_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.GameCreated} type in JSON format. */
	String GAME_CREATED__TYPE = "GameCreated";

	/** @see #getOwnerId() */
	String OWNER_ID__PROP = "ownerId";

	/** @see #getGame() */
	String GAME__PROP = "game";

	/**
	 * Id of the player that created the game. This player is implicitly the first player in the game without explicitly joining the game.
	 */
	String getOwnerId();

	/**
	 * @see #getOwnerId()
	 */
	de.haumacher.wizard.msg.GameCreated setOwnerId(String value);

	/**
	 * The newly created game.
	 */
	de.haumacher.wizard.msg.Game getGame();

	/**
	 * @see #getGame()
	 */
	de.haumacher.wizard.msg.GameCreated setGame(de.haumacher.wizard.msg.Game value);

	/**
	 * Checks, whether {@link #getGame()} has a value.
	 */
	boolean hasGame();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.GameCreated readGameCreated(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.GameCreated_Impl result = new de.haumacher.wizard.msg.impl.GameCreated_Impl();
		result.readContent(in);
		return result;
	}

}
