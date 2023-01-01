package de.haumacher.wizard.msg;

/**
 * A card of the Wizard game.
 */
public interface Card extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Card} instance.
	 */
	static de.haumacher.wizard.msg.Card create() {
		return new de.haumacher.wizard.msg.impl.Card_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Card} type in JSON format. */
	String CARD__TYPE = "Card";

	/** @see #getSuit() */
	String SUIT__PROP = "suit";

	/** @see #getValue() */
	String VALUE__PROP = "value";

	/**
	 * The card suit, <code>null</code> for wizards and fools.
	 */
	de.haumacher.wizard.msg.Suit getSuit();

	/**
	 * @see #getSuit()
	 */
	de.haumacher.wizard.msg.Card setSuit(de.haumacher.wizard.msg.Suit value);

	/**
	 * Checks, whether {@link #getSuit()} has a value.
	 */
	boolean hasSuit();

	/**
	 * The value of the card.
	 */
	de.haumacher.wizard.msg.Value getValue();

	/**
	 * @see #getValue()
	 */
	de.haumacher.wizard.msg.Card setValue(de.haumacher.wizard.msg.Value value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Card readCard(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Card_Impl result = new de.haumacher.wizard.msg.impl.Card_Impl();
		result.readContent(in);
		return result;
	}

}
