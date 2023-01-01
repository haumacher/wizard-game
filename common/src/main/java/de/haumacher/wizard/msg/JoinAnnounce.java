package de.haumacher.wizard.msg;

/**
 * Announces that a player has joined a game.
 */
public interface JoinAnnounce extends Msg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.JoinAnnounce} instance.
	 */
	static de.haumacher.wizard.msg.JoinAnnounce create() {
		return new de.haumacher.wizard.msg.impl.JoinAnnounce_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.JoinAnnounce} type in JSON format. */
	String JOIN_ANNOUNCE__TYPE = "JoinAnnounce";

	/** @see #getPlayer() */
	String PLAYER__PROP = "player";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/**
	 * The player that joined the game.
	 */
	de.haumacher.wizard.msg.Player getPlayer();

	/**
	 * @see #getPlayer()
	 */
	de.haumacher.wizard.msg.JoinAnnounce setPlayer(de.haumacher.wizard.msg.Player value);

	/**
	 * Checks, whether {@link #getPlayer()} has a value.
	 */
	boolean hasPlayer();

	/**
	 * The ID of the game the player joined.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.JoinAnnounce setGameId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.JoinAnnounce readJoinAnnounce(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.JoinAnnounce_Impl result = new de.haumacher.wizard.msg.impl.JoinAnnounce_Impl();
		result.readContent(in);
		return result;
	}

}
