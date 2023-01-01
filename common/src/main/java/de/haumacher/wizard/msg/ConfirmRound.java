package de.haumacher.wizard.msg;

/**
 * Command in response to {@link FinishRound} that must be received from all players, before the next round starts
 */
public interface ConfirmRound extends GameCmd {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.ConfirmRound} instance.
	 */
	static de.haumacher.wizard.msg.ConfirmRound create() {
		return new de.haumacher.wizard.msg.impl.ConfirmRound_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.ConfirmRound} type in JSON format. */
	String CONFIRM_ROUND__TYPE = "ConfirmRound";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.ConfirmRound readConfirmRound(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.ConfirmRound_Impl result = new de.haumacher.wizard.msg.impl.ConfirmRound_Impl();
		result.readContent(in);
		return result;
	}

}
