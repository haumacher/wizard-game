package de.haumacher.wizard.msg;

/**
 * A {@link Msg} targeting a running game and is only interesting for players of that game.
 */
public abstract class GameMsg extends Msg {

	/** Visitor interface for the {@link GameMsg} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link StartRound}.*/
		R visit(StartRound self, A arg) throws E;

		/** Visit case for {@link RequestTrumpSelection}.*/
		R visit(RequestTrumpSelection self, A arg) throws E;

		/** Visit case for {@link StartBids}.*/
		R visit(StartBids self, A arg) throws E;

		/** Visit case for {@link RequestBid}.*/
		R visit(RequestBid self, A arg) throws E;

		/** Visit case for {@link StartLead}.*/
		R visit(StartLead self, A arg) throws E;

		/** Visit case for {@link RequestLead}.*/
		R visit(RequestLead self, A arg) throws E;

		/** Visit case for {@link FinishTurn}.*/
		R visit(FinishTurn self, A arg) throws E;

		/** Visit case for {@link FinishRound}.*/
		R visit(FinishRound self, A arg) throws E;

		/** Visit case for {@link FinishGame}.*/
		R visit(FinishGame self, A arg) throws E;

		/** Visit case for {@link Announce}.*/
		R visit(Announce self, A arg) throws E;

	}

	/**
	 * Creates a {@link GameMsg} instance.
	 */
	protected GameMsg() {
		super();
	}

	/** Reads a new instance from the given reader. */
	public static GameMsg readGameMsg(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		GameMsg result;
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

	@Override
	public final <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return visit((GameMsg.Visitor<R,A,E>) v, arg);
	}

}
