package de.haumacher.wizard.msg;

public class ListGames extends Cmd {

	/**
	 * Creates a {@link ListGames} instance.
	 */
	public static ListGames create() {
		return new ListGames();
	}

	/** Identifier for the {@link ListGames} type in JSON format. */
	public static final String LIST_GAMES__TYPE = "ListGames";

	/**
	 * Creates a {@link ListGames} instance.
	 *
	 * @see #create()
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
		ListGames result = new ListGames();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
