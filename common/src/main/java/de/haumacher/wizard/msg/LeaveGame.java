package de.haumacher.wizard.msg;

/**
 * Command to request leaving a game.
 */
public interface LeaveGame extends Cmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.LeaveGame} instance.
	 */
	static de.haumacher.wizard.msg.LeaveGame create() {
		return new de.haumacher.wizard.msg.impl.LeaveGame_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.LeaveGame} type in JSON format. */
	String LEAVE_GAME__TYPE = "LeaveGame";

	/** @see #getGameId() */
	String GAME_ID__PROP = "gameId";

	/**
	 * The ID of the game to leave.
	 */
	String getGameId();

	/**
	 * @see #getGameId()
	 */
	de.haumacher.wizard.msg.LeaveGame setGameId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.LeaveGame readLeaveGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.LeaveGame_Impl result = new de.haumacher.wizard.msg.impl.LeaveGame_Impl();
		result.readContent(in);
		return result;
	}

}
