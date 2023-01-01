package de.haumacher.wizard.msg;

/**
 * Message to forward a {@link GameCmd} sent by one player of a game to all other players.
 */
public interface Announce extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Announce} instance.
	 */
	static de.haumacher.wizard.msg.Announce create() {
		return new de.haumacher.wizard.msg.impl.Announce_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Announce} type in JSON format. */
	String ANNOUNCE__TYPE = "Announce";

	/** @see #getPlayerId() */
	String PLAYER_ID__PROP = "playerId";

	/** @see #getCmd() */
	String CMD__PROP = "cmd";

	/**
	 * The ID of the player that sent the command.
	 */
	String getPlayerId();

	/**
	 * @see #getPlayerId()
	 */
	de.haumacher.wizard.msg.Announce setPlayerId(String value);

	/**
	 * The command sent by the player with the ID given in {@link #getPlayerId()}.
	 */
	de.haumacher.wizard.msg.GameCmd getCmd();

	/**
	 * @see #getCmd()
	 */
	de.haumacher.wizard.msg.Announce setCmd(de.haumacher.wizard.msg.GameCmd value);

	/**
	 * Checks, whether {@link #getCmd()} has a value.
	 */
	boolean hasCmd();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Announce readAnnounce(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Announce_Impl result = new de.haumacher.wizard.msg.impl.Announce_Impl();
		result.readContent(in);
		return result;
	}

}
