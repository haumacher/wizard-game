package de.haumacher.wizard.msg;

public abstract class Result extends Msg {

	/** Visitor interface for the {@link Result} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link Error}.*/
		R visit(Error self, A arg) throws E;

		/** Visit case for {@link ListGamesResult}.*/
		R visit(ListGamesResult self, A arg) throws E;

	}

	/**
	 * Creates a {@link Result} instance.
	 */
	protected Result() {
		super();
	}

	@Override
	public abstract String jsonType();

	/** Reads a new instance from the given reader. */
	public static Result readResult(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Result result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case Error.ERROR__TYPE: result = de.haumacher.wizard.msg.Error.readError(in); break;
			case ListGamesResult.LIST_GAMES_RESULT__TYPE: result = de.haumacher.wizard.msg.ListGamesResult.readListGamesResult(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;


	@Override
	public final <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return visit((Visitor<R,A,E>) v, arg);
	}

}
