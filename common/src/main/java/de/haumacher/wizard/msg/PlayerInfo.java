package de.haumacher.wizard.msg;

/**
 * Score information for a single player, see {@link StartLead}.
 */
public interface PlayerInfo extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.PlayerInfo} instance.
	 */
	static de.haumacher.wizard.msg.PlayerInfo create() {
		return new de.haumacher.wizard.msg.impl.PlayerInfo_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.PlayerInfo} type in JSON format. */
	String PLAYER_INFO__TYPE = "PlayerInfo";

	/** @see #getBid() */
	String BID__PROP = "bid";

	/** @see #getTricks() */
	String TRICKS__PROP = "tricks";

	/** @see #getPoints() */
	String POINTS__PROP = "points";

	/**
	 * The player's bid.
	 */
	int getBid();

	/**
	 * @see #getBid()
	 */
	de.haumacher.wizard.msg.PlayerInfo setBid(int value);

	/**
	 * The number of won tricks.
	 */
	int getTricks();

	/**
	 * @see #getTricks()
	 */
	de.haumacher.wizard.msg.PlayerInfo setTricks(int value);

	/**
	 * The current points of the player.
	 */
	int getPoints();

	/**
	 * @see #getPoints()
	 */
	de.haumacher.wizard.msg.PlayerInfo setPoints(int value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.PlayerInfo readPlayerInfo(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.PlayerInfo_Impl result = new de.haumacher.wizard.msg.impl.PlayerInfo_Impl();
		result.readContent(in);
		return result;
	}

}
