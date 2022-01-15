package de.haumacher.wizard.msg;

public class StartBids extends Msg {

	/**
	 * Creates a {@link StartBids} instance.
	 */
	public static StartBids create() {
		return new StartBids();
	}

	/** Identifier for the {@link StartBids} type in JSON format. */
	public static final String START_BIDS__TYPE = "StartBids";

	/**
	 * Creates a {@link StartBids} instance.
	 *
	 * @see #create()
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
		StartBids result = new StartBids();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
