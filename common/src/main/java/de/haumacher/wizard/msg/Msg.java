package de.haumacher.wizard.msg;

/**
 * A message sent from the game server to active clients.
 */
public interface Msg extends de.haumacher.msgbuf.data.DataObject {

	/** Visitor interface for the {@link de.haumacher.wizard.msg.Msg} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> extends de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E>, de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> {

		/** Visit case for {@link de.haumacher.wizard.msg.GameCreated}.*/
		R visit(de.haumacher.wizard.msg.GameCreated self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.GameDeleted}.*/
		R visit(de.haumacher.wizard.msg.GameDeleted self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.GameStarted}.*/
		R visit(de.haumacher.wizard.msg.GameStarted self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.JoinAnnounce}.*/
		R visit(de.haumacher.wizard.msg.JoinAnnounce self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.LeaveAnnounce}.*/
		R visit(de.haumacher.wizard.msg.LeaveAnnounce self, A arg) throws E;

	}

	/** The type identifier for this concrete subtype. */
	String jsonType();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Msg readMsg(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Msg result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case GameCreated.GAME_CREATED__TYPE: result = de.haumacher.wizard.msg.GameCreated.readGameCreated(in); break;
			case GameDeleted.GAME_DELETED__TYPE: result = de.haumacher.wizard.msg.GameDeleted.readGameDeleted(in); break;
			case GameStarted.GAME_STARTED__TYPE: result = de.haumacher.wizard.msg.GameStarted.readGameStarted(in); break;
			case JoinAnnounce.JOIN_ANNOUNCE__TYPE: result = de.haumacher.wizard.msg.JoinAnnounce.readJoinAnnounce(in); break;
			case LeaveAnnounce.LEAVE_ANNOUNCE__TYPE: result = de.haumacher.wizard.msg.LeaveAnnounce.readLeaveAnnounce(in); break;
			case Error.ERROR__TYPE: result = de.haumacher.wizard.msg.Error.readError(in); break;
			case HelloResult.HELLO_RESULT__TYPE: result = de.haumacher.wizard.msg.HelloResult.readHelloResult(in); break;
			case CreateAccountResult.CREATE_ACCOUNT_RESULT__TYPE: result = de.haumacher.wizard.msg.CreateAccountResult.readCreateAccountResult(in); break;
			case AddEmailSuccess.ADD_EMAIL_SUCCESS__TYPE: result = de.haumacher.wizard.msg.AddEmailSuccess.readAddEmailSuccess(in); break;
			case VerifyEmailSuccess.VERIFY_EMAIL_SUCCESS__TYPE: result = de.haumacher.wizard.msg.VerifyEmailSuccess.readVerifyEmailSuccess(in); break;
			case Welcome.WELCOME__TYPE: result = de.haumacher.wizard.msg.Welcome.readWelcome(in); break;
			case LoginFailed.LOGIN_FAILED__TYPE: result = de.haumacher.wizard.msg.LoginFailed.readLoginFailed(in); break;
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

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;

}
