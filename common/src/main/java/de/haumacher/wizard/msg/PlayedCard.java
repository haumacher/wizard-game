package de.haumacher.wizard.msg;

/**
 * Info about a {@link Card} played.
 */
public interface PlayedCard extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.PlayedCard} instance.
	 */
	static de.haumacher.wizard.msg.PlayedCard create() {
		return new de.haumacher.wizard.msg.impl.PlayedCard_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.PlayedCard} type in JSON format. */
	String PLAYED_CARD__TYPE = "PlayedCard";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/** @see #getCard() */
	String CARD__PROP = "card";

	/**
	 * ID of the player that played the {@link #getCard()}.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.PlayedCard setPlayerId(String value);

	/**
	 * The {@link Card} that lays on the table.
	 */
	de.haumacher.wizard.msg.Card getCard();

	/**
	 * @see #getCard()
	 */
	de.haumacher.wizard.msg.PlayedCard setCard(de.haumacher.wizard.msg.Card value);

	/**
	 * Checks, whether {@link #getCard()} has a value.
	 */
	boolean hasCard();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.PlayedCard readPlayedCard(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.PlayedCard_Impl result = new de.haumacher.wizard.msg.impl.PlayedCard_Impl();
		result.readContent(in);
		return result;
	}

}
