package de.haumacher.wizard.msg;

/**
 * Command in response to {@link FinishTurn} that must be received from all players, before the next turn starts
 */
public class ConfirmTrick extends GameCmd {

	/**
	 * Creates a {@link ConfirmTrick} instance.
	 */
	public static ConfirmTrick create() {
		return new ConfirmTrick();
	}

	/** Identifier for the {@link ConfirmTrick} type in JSON format. */
	public static final String CONFIRM_TRICK__TYPE = "ConfirmTrick";

	/**
	 * Creates a {@link ConfirmTrick} instance.
	 *
	 * @see #create()
	 */
	protected ConfirmTrick() {
		super();
	}

	@Override
	public String jsonType() {
		return CONFIRM_TRICK__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static ConfirmTrick readConfirmTrick(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		ConfirmTrick result = new ConfirmTrick();
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
