package de.haumacher.wizard.msg;

/**
 * Message sent at the end of each turn announcing the winner.
 */
public interface FinishTurn extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.FinishTurn} instance.
	 */
	static de.haumacher.wizard.msg.FinishTurn create() {
		return new de.haumacher.wizard.msg.impl.FinishTurn_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.FinishTurn} type in JSON format. */
	String FINISH_TURN__TYPE = "FinishTurn";

	/** @see #getTrick() */
	String TRICK__PROP = "trick";

	/** @see #getWinner() */
	String WINNER__PROP = "winner";

	/**
	 * The cards that have been played in this turn.
	 */
	java.util.List<de.haumacher.wizard.msg.Card> getTrick();

	/**
	 * @see #getTrick()
	 */
	de.haumacher.wizard.msg.FinishTurn setTrick(java.util.List<? extends de.haumacher.wizard.msg.Card> value);

	/**
	 * Adds a value to the {@link #getTrick()} list.
	 */
	de.haumacher.wizard.msg.FinishTurn addTrick(de.haumacher.wizard.msg.Card value);

	/**
	 * Removes a value from the {@link #getTrick()} list.
	 */
	void removeTrick(de.haumacher.wizard.msg.Card value);

	/**
	 * The winner of this turn.
	 */
	de.haumacher.wizard.msg.Player getWinner();

	/**
	 * @see #getWinner()
	 */
	de.haumacher.wizard.msg.FinishTurn setWinner(de.haumacher.wizard.msg.Player value);

	/**
	 * Checks, whether {@link #getWinner()} has a value.
	 */
	boolean hasWinner();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.FinishTurn readFinishTurn(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.FinishTurn_Impl result = new de.haumacher.wizard.msg.impl.FinishTurn_Impl();
		result.readContent(in);
		return result;
	}

}
