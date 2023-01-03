package de.haumacher.wizard.msg;

/**
 * Command in response to {@link FinishGame} that must be received from all players, before the game is dropped.
 */
public interface ConfirmGame extends GameCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.ConfirmGame} instance.
	 */
	static de.haumacher.wizard.msg.ConfirmGame create() {
		return new de.haumacher.wizard.msg.impl.ConfirmGame_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.ConfirmGame} type in JSON format. */
	String CONFIRM_GAME__TYPE = "ConfirmGame";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.ConfirmGame readConfirmGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.ConfirmGame_Impl result = new de.haumacher.wizard.msg.impl.ConfirmGame_Impl();
		result.readContent(in);
		return result;
	}

}
