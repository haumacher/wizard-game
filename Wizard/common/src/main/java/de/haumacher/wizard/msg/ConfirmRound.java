package de.haumacher.wizard.msg;

/**
 * Command in response to {@link FinishRound} that must be received from all players, before the next round starts
 */
public class ConfirmRound extends GameCmd {

	/**
	 * Creates a {@link ConfirmRound} instance.
	 */
	public static ConfirmRound create() {
		return new ConfirmRound();
	}

	/** Identifier for the {@link ConfirmRound} type in JSON format. */
	public static final String CONFIRM_ROUND__TYPE = "ConfirmRound";

	/**
	 * Creates a {@link ConfirmRound} instance.
	 *
	 * @see #create()
	 */
	protected ConfirmRound() {
		super();
	}

	@Override
	public String jsonType() {
		return CONFIRM_ROUND__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static ConfirmRound readConfirmRound(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		ConfirmRound result = new ConfirmRound();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
