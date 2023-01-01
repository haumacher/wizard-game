package de.haumacher.wizard.msg;

/**
 * Message that starts the bid phase.
 */
public interface StartBids extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.StartBids} instance.
	 */
	static de.haumacher.wizard.msg.StartBids create() {
		return new de.haumacher.wizard.msg.impl.StartBids_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.StartBids} type in JSON format. */
	String START_BIDS__TYPE = "StartBids";

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.StartBids readStartBids(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.StartBids_Impl result = new de.haumacher.wizard.msg.impl.StartBids_Impl();
		result.readContent(in);
		return result;
	}

}
