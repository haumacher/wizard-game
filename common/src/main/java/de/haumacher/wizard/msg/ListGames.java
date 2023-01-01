package de.haumacher.wizard.msg;

/**
 * Requests a listing of games waiting for player.
 */
public class ListGames extends Cmd {

	/**
	 * Creates a {@link ListGames} instance.
	 */
	public static ListGames create() {
		return new de.haumacher.wizard.msg.ListGames();
	}

	/** Identifier for the {@link ListGames} type in JSON format. */
	public static final String LIST_GAMES__TYPE = "ListGames";

	/**
	 * Creates a {@link ListGames} instance.
	 *
	 * @see ListGames#create()
	 */
	protected ListGames() {
		super();
	}

	@Override
	public String jsonType() {
		return LIST_GAMES__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static ListGames readListGames(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.ListGames result = new de.haumacher.wizard.msg.ListGames();
		result.readContent(in);
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
