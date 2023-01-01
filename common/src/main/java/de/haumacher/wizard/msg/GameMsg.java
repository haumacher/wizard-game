package de.haumacher.wizard.msg;

/**
 * A {@link Msg} targeting a running game and is only interesting for players of that game.
 */
public interface GameMsg extends Msg {

	/** Visitor interface for the {@link de.haumacher.wizard.msg.GameMsg} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link de.haumacher.wizard.msg.StartRound}.*/
		R visit(de.haumacher.wizard.msg.StartRound self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.RequestTrumpSelection}.*/
		R visit(de.haumacher.wizard.msg.RequestTrumpSelection self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.StartBids}.*/
		R visit(de.haumacher.wizard.msg.StartBids self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.RequestBid}.*/
		R visit(de.haumacher.wizard.msg.RequestBid self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.StartLead}.*/
		R visit(de.haumacher.wizard.msg.StartLead self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.RequestLead}.*/
		R visit(de.haumacher.wizard.msg.RequestLead self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.FinishTurn}.*/
		R visit(de.haumacher.wizard.msg.FinishTurn self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.FinishRound}.*/
		R visit(de.haumacher.wizard.msg.FinishRound self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.FinishGame}.*/
		R visit(de.haumacher.wizard.msg.FinishGame self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.Announce}.*/
		R visit(de.haumacher.wizard.msg.Announce self, A arg) throws E;

	}

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.GameMsg readGameMsg(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.GameMsg result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case StartRound.START_ROUND__TYPE: result = de.haumacher.wizard.msg.StartRound.readStartRound(in); break;
			case RequestTrumpSelection.REQUEST_TRUMP_SELECTION__TYPE: result = de.haumacher.wizard.msg.RequestTrumpSelection.readRequestTrumpSelection(in); break;
			case StartBids.START_BIDS__TYPE: result = de.haumacher.wizard.msg.StartBids.readStartBids(in); break;
			case RequestBid.REQUEST_BID__TYPE: result = de.haumacher.wizard.msg.RequestBid.readRequestBid(in); break;
			case StartLead.START_LEAD__TYPE: result = de.haumacher.wizard.msg.StartLead.readStartLead(in); break;
			case RequestLead.REQUEST_LEAD__TYPE: result = de.haumacher.wizard.msg.RequestLead.readRequestLead(in); break;
			case FinishTurn.FINISH_TURN__TYPE: result = de.haumacher.wizard.msg.FinishTurn.readFinishTurn(in); break;
			case FinishRound.FINISH_ROUND__TYPE: result = de.haumacher.wizard.msg.FinishRound.readFinishRound(in); break;
			case FinishGame.FINISH_GAME__TYPE: result = de.haumacher.wizard.msg.FinishGame.readFinishGame(in); break;
			case Announce.ANNOUNCE__TYPE: result = de.haumacher.wizard.msg.Announce.readAnnounce(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;

}
