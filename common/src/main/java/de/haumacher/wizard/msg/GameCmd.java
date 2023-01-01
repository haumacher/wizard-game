package de.haumacher.wizard.msg;

/**
 * A {@link Cmd} targeting a running game.
 */
public abstract class GameCmd extends Cmd {

	/** Visitor interface for the {@link GameCmd} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link SelectTrump}.*/
		R visit(SelectTrump self, A arg) throws E;

		/** Visit case for {@link Bid}.*/
		R visit(Bid self, A arg) throws E;

		/** Visit case for {@link Lead}.*/
		R visit(Lead self, A arg) throws E;

		/** Visit case for {@link ConfirmTrick}.*/
		R visit(ConfirmTrick self, A arg) throws E;

		/** Visit case for {@link ConfirmRound}.*/
		R visit(ConfirmRound self, A arg) throws E;

	}

	/**
	 * Creates a {@link GameCmd} instance.
	 */
	protected GameCmd() {
		super();
	}

	/** Reads a new instance from the given reader. */
	public static GameCmd readGameCmd(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		GameCmd result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case SelectTrump.SELECT_TRUMP__TYPE: result = de.haumacher.wizard.msg.SelectTrump.readSelectTrump(in); break;
			case Bid.BID__TYPE: result = de.haumacher.wizard.msg.Bid.readBid(in); break;
			case Lead.LEAD__TYPE: result = de.haumacher.wizard.msg.Lead.readLead(in); break;
			case ConfirmTrick.CONFIRM_TRICK__TYPE: result = de.haumacher.wizard.msg.ConfirmTrick.readConfirmTrick(in); break;
			case ConfirmRound.CONFIRM_ROUND__TYPE: result = de.haumacher.wizard.msg.ConfirmRound.readConfirmRound(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;

	@Override
	public final <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return visit((GameCmd.Visitor<R,A,E>) v, arg);
	}

}
