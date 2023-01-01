package de.haumacher.wizard.msg;

/**
 * Message announcing a player's bid.
 */
public interface Bid extends GameCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Bid} instance.
	 */
	static de.haumacher.wizard.msg.Bid create() {
		return new de.haumacher.wizard.msg.impl.Bid_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Bid} type in JSON format. */
	String BID__TYPE = "Bid";

	/** @see #getCnt() */
	String CNT__PROP = "cnt";

	/** @see #getExpected() */
	String EXPECTED__PROP = "expected";

	/**
	 * The bid of the current player
	 */
	int getCnt();

	/**
	 * @see #getCnt()
	 */
	de.haumacher.wizard.msg.Bid setCnt(int value);

	/**
	 * The sum of tricks expected by all players so far.
	 *
	 * <p>
	 * The value is only relevant when the message is announced to all players. It is not required to set this value when sending the command to the server.
	 * </p>
	 */
	int getExpected();

	/**
	 * @see #getExpected()
	 */
	de.haumacher.wizard.msg.Bid setExpected(int value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Bid readBid(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Bid_Impl result = new de.haumacher.wizard.msg.impl.Bid_Impl();
		result.readContent(in);
		return result;
	}

}
