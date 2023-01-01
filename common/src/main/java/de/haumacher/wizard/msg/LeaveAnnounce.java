package de.haumacher.wizard.msg;

/**
 * Announces that a player has left a game.
 */
public interface LeaveAnnounce extends Msg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.LeaveAnnounce} instance.
	 */
	static de.haumacher.wizard.msg.LeaveAnnounce create() {
		return new de.haumacher.wizard.msg.impl.LeaveAnnounce_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.LeaveAnnounce} type in JSON format. */
	String LEAVE_ANNOUNCE__TYPE = "LeaveAnnounce";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/**
	 * The ID of the player that left a game.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.LeaveAnnounce setPlayerId(String value);

	/**
	 * The ID of the game the player left.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.LeaveAnnounce setGameId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.LeaveAnnounce readLeaveAnnounce(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.LeaveAnnounce_Impl result = new de.haumacher.wizard.msg.impl.LeaveAnnounce_Impl();
		result.readContent(in);
		return result;
	}

}
