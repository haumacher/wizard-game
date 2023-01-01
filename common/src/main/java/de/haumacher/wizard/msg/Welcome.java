package de.haumacher.wizard.msg;

/**
 * Response message upon successful {@link Login}.
 */
public interface Welcome extends ResultMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Welcome} instance.
	 */
	static de.haumacher.wizard.msg.Welcome create() {
		return new de.haumacher.wizard.msg.impl.Welcome_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Welcome} type in JSON format. */
	String WELCOME__TYPE = "Welcome";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/**
	 * The ID of the player that logged in to the server.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.Welcome setPlayerId(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Welcome readWelcome(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Welcome_Impl result = new de.haumacher.wizard.msg.impl.Welcome_Impl();
		result.readContent(in);
		return result;
	}

}
