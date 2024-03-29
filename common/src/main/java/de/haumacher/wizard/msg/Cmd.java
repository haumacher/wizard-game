package de.haumacher.wizard.msg;

/**
 * A message that is sent from a client to the game server to trigger some action.
 */
public interface Cmd extends de.haumacher.msgbuf.data.DataObject {

	/** Visitor interface for the {@link de.haumacher.wizard.msg.Cmd} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> extends de.haumacher.wizard.msg.LoginCmd.Visitor<R,A,E>, de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E> {

		/** Visit case for {@link de.haumacher.wizard.msg.ListGames}.*/
		R visit(de.haumacher.wizard.msg.ListGames self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.JoinGame}.*/
		R visit(de.haumacher.wizard.msg.JoinGame self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.CreateGame}.*/
		R visit(de.haumacher.wizard.msg.CreateGame self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.StartGame}.*/
		R visit(de.haumacher.wizard.msg.StartGame self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.LeaveGame}.*/
		R visit(de.haumacher.wizard.msg.LeaveGame self, A arg) throws E;

	}

	/** The type identifier for this concrete subtype. */
	String jsonType();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Cmd readCmd(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Cmd result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case ListGames.LIST_GAMES__TYPE: result = de.haumacher.wizard.msg.ListGames.readListGames(in); break;
			case JoinGame.JOIN_GAME__TYPE: result = de.haumacher.wizard.msg.JoinGame.readJoinGame(in); break;
			case CreateGame.CREATE_GAME__TYPE: result = de.haumacher.wizard.msg.CreateGame.readCreateGame(in); break;
			case StartGame.START_GAME__TYPE: result = de.haumacher.wizard.msg.StartGame.readStartGame(in); break;
			case LeaveGame.LEAVE_GAME__TYPE: result = de.haumacher.wizard.msg.LeaveGame.readLeaveGame(in); break;
			case Hello.HELLO__TYPE: result = de.haumacher.wizard.msg.Hello.readHello(in); break;
			case CreateAccount.CREATE_ACCOUNT__TYPE: result = de.haumacher.wizard.msg.CreateAccount.readCreateAccount(in); break;
			case AddEmail.ADD_EMAIL__TYPE: result = de.haumacher.wizard.msg.AddEmail.readAddEmail(in); break;
			case VerifyEmail.VERIFY_EMAIL__TYPE: result = de.haumacher.wizard.msg.VerifyEmail.readVerifyEmail(in); break;
			case Login.LOGIN__TYPE: result = de.haumacher.wizard.msg.Login.readLogin(in); break;
			case Reconnect.RECONNECT__TYPE: result = de.haumacher.wizard.msg.Reconnect.readReconnect(in); break;
			case SelectTrump.SELECT_TRUMP__TYPE: result = de.haumacher.wizard.msg.SelectTrump.readSelectTrump(in); break;
			case Bid.BID__TYPE: result = de.haumacher.wizard.msg.Bid.readBid(in); break;
			case Lead.LEAD__TYPE: result = de.haumacher.wizard.msg.Lead.readLead(in); break;
			case ConfirmTrick.CONFIRM_TRICK__TYPE: result = de.haumacher.wizard.msg.ConfirmTrick.readConfirmTrick(in); break;
			case ConfirmRound.CONFIRM_ROUND__TYPE: result = de.haumacher.wizard.msg.ConfirmRound.readConfirmRound(in); break;
			case ConfirmGame.CONFIRM_GAME__TYPE: result = de.haumacher.wizard.msg.ConfirmGame.readConfirmGame(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;

}
