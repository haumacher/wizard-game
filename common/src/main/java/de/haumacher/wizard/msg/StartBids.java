package de.haumacher.wizard.msg;

/**
 * Message that starts the bid phase.
 */
public class StartBids extends GameMsg {

	/**
	 * Creates a {@link StartBids} instance.
	 */
	public static StartBids create() {
		return new de.haumacher.wizard.msg.StartBids();
	}

	/** Identifier for the {@link StartBids} type in JSON format. */
	public static final String START_BIDS__TYPE = "StartBids";

	/**
	 * Creates a {@link StartBids} instance.
	 *
	 * @see StartBids#create()
	 */
	protected StartBids() {
		super();
	}

	@Override
	public String jsonType() {
		return START_BIDS__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static StartBids readStartBids(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.StartBids result = new de.haumacher.wizard.msg.StartBids();
		result.readContent(in);
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
