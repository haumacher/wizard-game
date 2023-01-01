package de.haumacher.wizard.msg;

/**
 * Message announcing that all bids are placed.
 */
public interface StartLead extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.StartLead} instance.
	 */
	static de.haumacher.wizard.msg.StartLead create() {
		return new de.haumacher.wizard.msg.impl.StartLead_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.StartLead} type in JSON format. */
	String START_LEAD__TYPE = "StartLead";

	/** @see #getState() */
	String STATE__PROP = "state";

	/** @see #getCurrentTrick() */
	String CURRENT_TRICK__PROP = "currentTrick";

	/**
	 * Score information of all players by their IDs.
	 */
	java.util.Map<String, de.haumacher.wizard.msg.PlayerInfo> getState();

	/**
	 * @see #getState()
	 */
	de.haumacher.wizard.msg.StartLead setState(java.util.Map<String, de.haumacher.wizard.msg.PlayerInfo> value);

	/**
	 * Adds a key value pair to the {@link #getState()} map.
	 */
	de.haumacher.wizard.msg.StartLead putState(String key, de.haumacher.wizard.msg.PlayerInfo value);

	/**
	 * Removes a key from the {@link #getState()} map.
	 */
	void removeState(String key);

	/**
	 * The cards played so far. Only relevant when reconnecting to a game.
	 */
	java.util.List<de.haumacher.wizard.msg.PlayedCard> getCurrentTrick();

	/**
	 * @see #getCurrentTrick()
	 */
	de.haumacher.wizard.msg.StartLead setCurrentTrick(java.util.List<? extends de.haumacher.wizard.msg.PlayedCard> value);

	/**
	 * Adds a value to the {@link #getCurrentTrick()} list.
	 */
	de.haumacher.wizard.msg.StartLead addCurrentTrick(de.haumacher.wizard.msg.PlayedCard value);

	/**
	 * Removes a value from the {@link #getCurrentTrick()} list.
	 */
	void removeCurrentTrick(de.haumacher.wizard.msg.PlayedCard value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.StartLead readStartLead(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.StartLead_Impl result = new de.haumacher.wizard.msg.impl.StartLead_Impl();
		result.readContent(in);
		return result;
	}

}
