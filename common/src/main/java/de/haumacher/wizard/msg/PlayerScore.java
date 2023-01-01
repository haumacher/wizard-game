package de.haumacher.wizard.msg;

/**
 * Score info for a player.
 */
public interface PlayerScore extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.PlayerScore} instance.
	 */
	static de.haumacher.wizard.msg.PlayerScore create() {
		return new de.haumacher.wizard.msg.impl.PlayerScore_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.PlayerScore} type in JSON format. */
	String PLAYER_SCORE__TYPE = "PlayerScore";

	/** @see #getPlayer() */
	String PLAYER__PROP = "player";

	/** @see #getPoints() */
	String POINTS__PROP = "points";

	/**
	 * The player.
	 */
	de.haumacher.wizard.msg.Player getPlayer();

	/**
	 * @see #getPlayer()
	 */
	de.haumacher.wizard.msg.PlayerScore setPlayer(de.haumacher.wizard.msg.Player value);

	/**
	 * Checks, whether {@link #getPlayer()} has a value.
	 */
	boolean hasPlayer();

	/**
	 * The player's points.
	 */
	int getPoints();

	/**
	 * @see #getPoints()
	 */
	de.haumacher.wizard.msg.PlayerScore setPoints(int value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.PlayerScore readPlayerScore(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.PlayerScore_Impl result = new de.haumacher.wizard.msg.impl.PlayerScore_Impl();
		result.readContent(in);
		return result;
	}

}
