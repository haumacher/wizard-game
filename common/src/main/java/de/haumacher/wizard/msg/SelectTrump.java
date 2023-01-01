package de.haumacher.wizard.msg;

/**
 * {@link GameCmd} to select the trump suit in response to {@link RequestTrumpSelection}.
 */
public interface SelectTrump extends GameCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.SelectTrump} instance.
	 */
	static de.haumacher.wizard.msg.SelectTrump create() {
		return new de.haumacher.wizard.msg.impl.SelectTrump_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.SelectTrump} type in JSON format. */
	String SELECT_TRUMP__TYPE = "SelectTrump";

	/** @see #getTrumpSuit() */
	String TRUMP_SUIT__PROP = "trumpSuit";

	/**
	 * The trump suit selected.
	 */
	de.haumacher.wizard.msg.Suit getTrumpSuit();

	/**
	 * @see #getTrumpSuit()
	 */
	de.haumacher.wizard.msg.SelectTrump setTrumpSuit(de.haumacher.wizard.msg.Suit value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.SelectTrump readSelectTrump(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.SelectTrump_Impl result = new de.haumacher.wizard.msg.impl.SelectTrump_Impl();
		result.readContent(in);
		return result;
	}

}
