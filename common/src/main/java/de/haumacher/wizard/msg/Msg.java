package de.haumacher.wizard.msg;

/**
 * A message sent from the game server to active clients.
 */
public abstract class Msg extends de.haumacher.msgbuf.data.AbstractDataObject {

	/** Visitor interface for the {@link Msg} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> extends ResultMsg.Visitor<R,A,E>, GameMsg.Visitor<R,A,E> {

		/** Visit case for {@link GameCreated}.*/
		R visit(GameCreated self, A arg) throws E;

		/** Visit case for {@link GameDeleted}.*/
		R visit(GameDeleted self, A arg) throws E;

		/** Visit case for {@link GameStarted}.*/
		R visit(GameStarted self, A arg) throws E;

		/** Visit case for {@link JoinAnnounce}.*/
		R visit(JoinAnnounce self, A arg) throws E;

		/** Visit case for {@link LeaveAnnounce}.*/
		R visit(LeaveAnnounce self, A arg) throws E;

	}

	/**
	 * Creates a {@link Msg} instance.
	 */
	protected Msg() {
		super();
	}

	/** The type identifier for this concrete subtype. */
	public abstract String jsonType();

	/** Reads a new instance from the given reader. */
	public static Msg readMsg(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Msg result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case GameCreated.GAME_CREATED__TYPE: result = de.haumacher.wizard.msg.GameCreated.readGameCreated(in); break;
			case GameDeleted.GAME_DELETED__TYPE: result = de.haumacher.wizard.msg.GameDeleted.readGameDeleted(in); break;
			case GameStarted.GAME_STARTED__TYPE: result = de.haumacher.wizard.msg.GameStarted.readGameStarted(in); break;
			case JoinAnnounce.JOIN_ANNOUNCE__TYPE: result = de.haumacher.wizard.msg.JoinAnnounce.readJoinAnnounce(in); break;
			case LeaveAnnounce.LEAVE_ANNOUNCE__TYPE: result = de.haumacher.wizard.msg.LeaveAnnounce.readLeaveAnnounce(in); break;
			case Error.ERROR__TYPE: result = de.haumacher.wizard.msg.Error.readError(in); break;
			case Welcome.WELCOME__TYPE: result = de.haumacher.wizard.msg.Welcome.readWelcome(in); break;
			case ListGamesResult.LIST_GAMES_RESULT__TYPE: result = de.haumacher.wizard.msg.ListGamesResult.readListGamesResult(in); break;
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
