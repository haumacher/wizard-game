package de.haumacher.wizard.msg;

/**
 * Requests a player to select the trump suit in case the trump card was a wizard.
 */
public interface RequestTrumpSelection extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.RequestTrumpSelection} instance.
	 */
	static de.haumacher.wizard.msg.RequestTrumpSelection create() {
		return new de.haumacher.wizard.msg.impl.RequestTrumpSelection_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.RequestTrumpSelection} type in JSON format. */
	String REQUEST_TRUMP_SELECTION__TYPE = "RequestTrumpSelection";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/**
	 * The ID of the player that is expected to select the trump suit.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.RequestTrumpSelection setPlayerId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.RequestTrumpSelection readRequestTrumpSelection(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.RequestTrumpSelection_Impl result = new de.haumacher.wizard.msg.impl.RequestTrumpSelection_Impl();
		result.readContent(in);
		return result;
	}

}
