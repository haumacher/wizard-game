package de.haumacher.wizard.msg;

/**
 * Internal information kept for each player of a game on the server.
 */
public interface PlayerState extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.PlayerState} instance.
	 */
	static de.haumacher.wizard.msg.PlayerState create() {
		return new de.haumacher.wizard.msg.impl.PlayerState_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.PlayerState} type in JSON format. */
	String PLAYER_STATE__TYPE = "PlayerState";

	/** @see #getPlayer() */
	String PLAYER__PROP = "player";

	/** @see #getPoints() */
	String POINTS__PROP = "points";

	/** @see #getRoundState() */
	String ROUND_STATE__PROP = "roundState";

	/**
	 * Player data.
	 */
	de.haumacher.wizard.msg.Player getPlayer();

	/**
	 * @see #getPlayer()
	 */
	de.haumacher.wizard.msg.PlayerState setPlayer(de.haumacher.wizard.msg.Player value);

	/**
	 * Checks, whether {@link #getPlayer()} has a value.
	 */
	boolean hasPlayer();

	/**
	 * Points won so far.
	 */
	int getPoints();

	/**
	 * @see #getPoints()
	 */
	de.haumacher.wizard.msg.PlayerState setPoints(int value);

	/**
	 * Information about the current round.
	 */
	de.haumacher.wizard.msg.RoundState getRoundState();

	/**
	 * @see #getRoundState()
	 */
	de.haumacher.wizard.msg.PlayerState setRoundState(de.haumacher.wizard.msg.RoundState value);

	/**
	 * Checks, whether {@link #getRoundState()} has a value.
	 */
	boolean hasRoundState();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.PlayerState readPlayerState(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.PlayerState_Impl result = new de.haumacher.wizard.msg.impl.PlayerState_Impl();
		result.readContent(in);
		return result;
	}

}
