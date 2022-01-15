package de.haumacher.wizard.msg;

public abstract class Cmd extends de.haumacher.msgbuf.data.AbstractDataObject {

	/** Visitor interface for the {@link Cmd} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> extends GameCmd.Visitor<R,A,E> {

		/** Visit case for {@link Login}.*/
		R visit(Login self, A arg) throws E;

		/** Visit case for {@link CreateGame}.*/
		R visit(CreateGame self, A arg) throws E;

		/** Visit case for {@link StartGame}.*/
		R visit(StartGame self, A arg) throws E;

		/** Visit case for {@link Join}.*/
		R visit(Join self, A arg) throws E;

		/** Visit case for {@link ListGames}.*/
		R visit(ListGames self, A arg) throws E;

		/** Visit case for {@link LeaveGame}.*/
		R visit(LeaveGame self, A arg) throws E;

	}

	/**
	 * Creates a {@link Cmd} instance.
	 */
	protected Cmd() {
		super();
	}

	/** The type identifier for this concrete subtype. */
	public abstract String jsonType();

	/** Reads a new instance from the given reader. */
	public static Cmd readCmd(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Cmd result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case Login.LOGIN__TYPE: result = de.haumacher.wizard.msg.Login.readLogin(in); break;
			case CreateGame.CREATE_GAME__TYPE: result = de.haumacher.wizard.msg.CreateGame.readCreateGame(in); break;
			case StartGame.START_GAME__TYPE: result = de.haumacher.wizard.msg.StartGame.readStartGame(in); break;
			case Join.JOIN__TYPE: result = de.haumacher.wizard.msg.Join.readJoin(in); break;
			case ListGames.LIST_GAMES__TYPE: result = de.haumacher.wizard.msg.ListGames.readListGames(in); break;
			case LeaveGame.LEAVE_GAME__TYPE: result = de.haumacher.wizard.msg.LeaveGame.readLeaveGame(in); break;
			case ConfirmTrick.CONFIRM_TRICK__TYPE: result = de.haumacher.wizard.msg.ConfirmTrick.readConfirmTrick(in); break;
			case ConfirmRound.CONFIRM_ROUND__TYPE: result = de.haumacher.wizard.msg.ConfirmRound.readConfirmRound(in); break;
			case SelectTrump.SELECT_TRUMP__TYPE: result = de.haumacher.wizard.msg.SelectTrump.readSelectTrump(in); break;
			case Bid.BID__TYPE: result = de.haumacher.wizard.msg.Bid.readBid(in); break;
			case Put.PUT__TYPE: result = de.haumacher.wizard.msg.Put.readPut(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		out.beginArray();
		out.value(jsonType());
		writeContent(out);
		out.endArray();
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;


}
