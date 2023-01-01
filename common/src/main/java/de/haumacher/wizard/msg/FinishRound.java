package de.haumacher.wizard.msg;

/**
 * Message sent after each round that announces the points received in this round.
 */
public interface FinishRound extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.FinishRound} instance.
	 */
	static de.haumacher.wizard.msg.FinishRound create() {
		return new de.haumacher.wizard.msg.impl.FinishRound_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.FinishRound} type in JSON format. */
	String FINISH_ROUND__TYPE = "FinishRound";

	/** @see #getInfo() */
	String INFO__PROP = "info";

	/**
	 * For each player ID the number of points this player wins. The number may be negative.
	 */
	java.util.Map<String, de.haumacher.wizard.msg.RoundInfo> getInfo();

	/**
	 * @see #getInfo()
	 */
	de.haumacher.wizard.msg.FinishRound setInfo(java.util.Map<String, de.haumacher.wizard.msg.RoundInfo> value);

	/**
	 * Adds a key value pair to the {@link #getInfo()} map.
	 */
	de.haumacher.wizard.msg.FinishRound putInfo(String key, de.haumacher.wizard.msg.RoundInfo value);

	/**
	 * Removes a key from the {@link #getInfo()} map.
	 */
	void removeInfo(String key);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.FinishRound readFinishRound(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.FinishRound_Impl result = new de.haumacher.wizard.msg.impl.FinishRound_Impl();
		result.readContent(in);
		return result;
	}

}
