package de.haumacher.wizard.msg;

/**
 * Requests a listing of games waiting for player.
 */
public interface ListGames extends Cmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.ListGames} instance.
	 */
	static de.haumacher.wizard.msg.ListGames create() {
		return new de.haumacher.wizard.msg.impl.ListGames_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.ListGames} type in JSON format. */
	String LIST_GAMES__TYPE = "ListGames";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.ListGames readListGames(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.ListGames_Impl result = new de.haumacher.wizard.msg.impl.ListGames_Impl();
		result.readContent(in);
		return result;
	}

}
