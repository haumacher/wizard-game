package de.haumacher.wizard.msg;

/**
 * A {@link Cmd} targeting a running game.
 */
public interface GameCmd extends Cmd {

	/** Visitor interface for the {@link de.haumacher.wizard.msg.GameCmd} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link de.haumacher.wizard.msg.SelectTrump}.*/
		R visit(de.haumacher.wizard.msg.SelectTrump self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.Bid}.*/
		R visit(de.haumacher.wizard.msg.Bid self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.Lead}.*/
		R visit(de.haumacher.wizard.msg.Lead self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.ConfirmTrick}.*/
		R visit(de.haumacher.wizard.msg.ConfirmTrick self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.ConfirmRound}.*/
		R visit(de.haumacher.wizard.msg.ConfirmRound self, A arg) throws E;

	}

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.GameCmd readGameCmd(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.GameCmd result;
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

}
