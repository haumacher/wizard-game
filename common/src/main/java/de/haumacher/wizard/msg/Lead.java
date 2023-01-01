package de.haumacher.wizard.msg;

/**
 * Puts a card on the table.
 */
public interface Lead extends GameCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Lead} instance.
	 */
	static de.haumacher.wizard.msg.Lead create() {
		return new de.haumacher.wizard.msg.impl.Lead_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Lead} type in JSON format. */
	String LEAD__TYPE = "Lead";

	/** @see #getCard() */
	String CARD__PROP = "card";

	/**
	 * The card that is led.
	 */
	de.haumacher.wizard.msg.Card getCard();

	/**
	 * @see #getCard()
	 */
	de.haumacher.wizard.msg.Lead setCard(de.haumacher.wizard.msg.Card value);

	/**
	 * Checks, whether {@link #getCard()} has a value.
	 */
	boolean hasCard();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Lead readLead(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Lead_Impl result = new de.haumacher.wizard.msg.impl.Lead_Impl();
		result.readContent(in);
		return result;
	}

}
