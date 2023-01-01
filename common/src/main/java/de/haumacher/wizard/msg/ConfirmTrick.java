package de.haumacher.wizard.msg;

/**
 * Command in response to {@link FinishTurn} that must be received from all players, before the next turn starts
 */
public interface ConfirmTrick extends GameCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.ConfirmTrick} instance.
	 */
	static de.haumacher.wizard.msg.ConfirmTrick create() {
		return new de.haumacher.wizard.msg.impl.ConfirmTrick_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.ConfirmTrick} type in JSON format. */
	String CONFIRM_TRICK__TYPE = "ConfirmTrick";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.ConfirmTrick readConfirmTrick(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.ConfirmTrick_Impl result = new de.haumacher.wizard.msg.impl.ConfirmTrick_Impl();
		result.readContent(in);
		return result;
	}

}
