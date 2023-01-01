package de.haumacher.wizard.msg;

/**
 * The state of a player as part of {@link PlayerState}.
 */
public interface RoundState extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.RoundState} instance.
	 */
	static de.haumacher.wizard.msg.RoundState create() {
		return new de.haumacher.wizard.msg.impl.RoundState_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.RoundState} type in JSON format. */
	String ROUND_STATE__TYPE = "RoundState";

	/** @see #getBidCnt() */
	String BID_CNT__PROP = "bidCnt";

	/** @see #getWinCnt() */
	String WIN_CNT__PROP = "winCnt";

	/** @see #getCards() */
	String CARDS__PROP = "cards";

	/**
	 * The player's bid.
	 */
	int getBidCnt();

	/**
	 * @see #getBidCnt()
	 */
	de.haumacher.wizard.msg.RoundState setBidCnt(int value);

	/**
	 * The number of tricks won.
	 */
	int getWinCnt();

	/**
	 * @see #getWinCnt()
	 */
	de.haumacher.wizard.msg.RoundState setWinCnt(int value);

	/**
	 * The cards that the player has currently in his hand.
	 */
	java.util.List<de.haumacher.wizard.msg.Card> getCards();

	/**
	 * @see #getCards()
	 */
	de.haumacher.wizard.msg.RoundState setCards(java.util.List<? extends de.haumacher.wizard.msg.Card> value);

	/**
	 * Adds a value to the {@link #getCards()} list.
	 */
	de.haumacher.wizard.msg.RoundState addCard(de.haumacher.wizard.msg.Card value);

	/**
	 * Removes a value from the {@link #getCards()} list.
	 */
	void removeCard(de.haumacher.wizard.msg.Card value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.RoundState readRoundState(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.RoundState_Impl result = new de.haumacher.wizard.msg.impl.RoundState_Impl();
		result.readContent(in);
		return result;
	}

}
