package de.haumacher.wizard.msg;

/**
 * Message sent after the last round of a game.
 */
public interface FinishGame extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.FinishGame} instance.
	 */
	static de.haumacher.wizard.msg.FinishGame create() {
		return new de.haumacher.wizard.msg.impl.FinishGame_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.FinishGame} type in JSON format. */
	String FINISH_GAME__TYPE = "FinishGame";

	/** @see #getScores() */
	String SCORES__PROP = "scores";

	/**
	 * The total score for each player. The list is ordered by the points the players have won.
	 */
	java.util.List<de.haumacher.wizard.msg.PlayerScore> getScores();

	/**
	 * @see #getScores()
	 */
	de.haumacher.wizard.msg.FinishGame setScores(java.util.List<? extends de.haumacher.wizard.msg.PlayerScore> value);

	/**
	 * Adds a value to the {@link #getScores()} list.
	 */
	de.haumacher.wizard.msg.FinishGame addScore(de.haumacher.wizard.msg.PlayerScore value);

	/**
	 * Removes a value from the {@link #getScores()} list.
	 */
	void removeScore(de.haumacher.wizard.msg.PlayerScore value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.FinishGame readFinishGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.FinishGame_Impl result = new de.haumacher.wizard.msg.impl.FinishGame_Impl();
		result.readContent(in);
		return result;
	}

}
