package de.haumacher.wizard.msg;

/**
 * Command that requests creating a new game on the server.
 * <p>
 * On success, a {@link GameCreated} response message is sent to all clients not currently participating in a game.
 * </p>
 */
public class CreateGame extends Cmd {

	/**
	 * Creates a {@link CreateGame} instance.
	 */
	public static CreateGame create() {
		return new de.haumacher.wizard.msg.CreateGame();
	}

	/** Identifier for the {@link CreateGame} type in JSON format. */
	public static final String CREATE_GAME__TYPE = "CreateGame";

	/**
	 * Creates a {@link CreateGame} instance.
	 *
	 * @see CreateGame#create()
	 */
	protected CreateGame() {
		super();
	}

	@Override
	public String jsonType() {
		return CREATE_GAME__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static CreateGame readCreateGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.CreateGame result = new de.haumacher.wizard.msg.CreateGame();
		result.readContent(in);
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
