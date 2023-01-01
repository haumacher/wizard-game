package de.haumacher.wizard.msg;

/**
 * Requests a bid.
 */
public interface RequestBid extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.RequestBid} instance.
	 */
	static de.haumacher.wizard.msg.RequestBid create() {
		return new de.haumacher.wizard.msg.impl.RequestBid_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.RequestBid} type in JSON format. */
	String REQUEST_BID__TYPE = "RequestBid";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/** @see #getExpected() */
	String EXPECTED__PROP = "expected";

	/** @see #getRound() */
	String ROUND__PROP = "round";

	/**
	 * The player that is expected to place a bid.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.RequestBid setPlayerId(String value);

	/**
	 * The number of tricks expected by other players so far.
	 */
	int getExpected();

	/**
	 * @see #getExpected()
	 */
	de.haumacher.wizard.msg.RequestBid setExpected(int value);

	/**
	 * The round number and maximum number of tricks possible in that round.
	 */
	int getRound();

	/**
	 * @see #getRound()
	 */
	de.haumacher.wizard.msg.RequestBid setRound(int value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.RequestBid readRequestBid(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.RequestBid_Impl result = new de.haumacher.wizard.msg.impl.RequestBid_Impl();
		result.readContent(in);
		return result;
	}

}
