package de.haumacher.wizard.msg;

/**
 * Command that requests creating a new game on the server.
 * <p>
 * On success, a {@link GameCreated} response message is sent to all clients not currently participating in a game.
 * </p>
 */
public interface CreateGame extends Cmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.CreateGame} instance.
	 */
	static de.haumacher.wizard.msg.CreateGame create() {
		return new de.haumacher.wizard.msg.impl.CreateGame_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.CreateGame} type in JSON format. */
	String CREATE_GAME__TYPE = "CreateGame";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.CreateGame readCreateGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.CreateGame_Impl result = new de.haumacher.wizard.msg.impl.CreateGame_Impl();
		result.readContent(in);
		return result;
	}

}
