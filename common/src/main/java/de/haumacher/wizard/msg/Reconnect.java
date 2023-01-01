package de.haumacher.wizard.msg;

/**
 * Takes over a lost connection that was established before.
 */
public interface Reconnect extends LoginCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Reconnect} instance.
	 */
	static de.haumacher.wizard.msg.Reconnect create() {
		return new de.haumacher.wizard.msg.impl.Reconnect_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Reconnect} type in JSON format. */
	String RECONNECT__TYPE = "Reconnect";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/**
	 * The ID that was assigned to the player in a former {@link Welcome} message.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.Reconnect setPlayerId(String value);

	/**
	 * The ID of the game that this player would like to reconnect to.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.Reconnect setGameId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Reconnect readReconnect(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Reconnect_Impl result = new de.haumacher.wizard.msg.impl.Reconnect_Impl();
		result.readContent(in);
		return result;
	}

}
