package de.haumacher.wizard.msg;

/**
 * Message sent to all players that announces the player that is about to put a card on the table.
 */
public interface RequestLead extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.RequestLead} instance.
	 */
	static de.haumacher.wizard.msg.RequestLead create() {
		return new de.haumacher.wizard.msg.impl.RequestLead_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.RequestLead} type in JSON format. */
	String REQUEST_LEAD__TYPE = "RequestLead";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/**
	 * The ID of the player that is expected to send a {@link Lead} message. All other playes are only informed about the player that is in command.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.RequestLead setPlayerId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.RequestLead readRequestLead(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.RequestLead_Impl result = new de.haumacher.wizard.msg.impl.RequestLead_Impl();
		result.readContent(in);
		return result;
	}

}
