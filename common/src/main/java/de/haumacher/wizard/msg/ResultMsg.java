package de.haumacher.wizard.msg;

/**
 * A message that is sent in direct result to a {@link Cmd}.
 */
public interface ResultMsg extends Msg {

	/** Visitor interface for the {@link de.haumacher.wizard.msg.ResultMsg} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link de.haumacher.wizard.msg.Error}.*/
		R visit(de.haumacher.wizard.msg.Error self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.HelloResult}.*/
		R visit(de.haumacher.wizard.msg.HelloResult self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.CreateAccountResult}.*/
		R visit(de.haumacher.wizard.msg.CreateAccountResult self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.AddEmailSuccess}.*/
		R visit(de.haumacher.wizard.msg.AddEmailSuccess self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.VerifyEmailSuccess}.*/
		R visit(de.haumacher.wizard.msg.VerifyEmailSuccess self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.Welcome}.*/
		R visit(de.haumacher.wizard.msg.Welcome self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.LoginFailed}.*/
		R visit(de.haumacher.wizard.msg.LoginFailed self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.ListGamesResult}.*/
		R visit(de.haumacher.wizard.msg.ListGamesResult self, A arg) throws E;

	}

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.ResultMsg readResultMsg(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.ResultMsg result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case Error.ERROR__TYPE: result = de.haumacher.wizard.msg.Error.readError(in); break;
			case HelloResult.HELLO_RESULT__TYPE: result = de.haumacher.wizard.msg.HelloResult.readHelloResult(in); break;
			case CreateAccountResult.CREATE_ACCOUNT_RESULT__TYPE: result = de.haumacher.wizard.msg.CreateAccountResult.readCreateAccountResult(in); break;
			case AddEmailSuccess.ADD_EMAIL_SUCCESS__TYPE: result = de.haumacher.wizard.msg.AddEmailSuccess.readAddEmailSuccess(in); break;
			case VerifyEmailSuccess.VERIFY_EMAIL_SUCCESS__TYPE: result = de.haumacher.wizard.msg.VerifyEmailSuccess.readVerifyEmailSuccess(in); break;
			case Welcome.WELCOME__TYPE: result = de.haumacher.wizard.msg.Welcome.readWelcome(in); break;
			case LoginFailed.LOGIN_FAILED__TYPE: result = de.haumacher.wizard.msg.LoginFailed.readLoginFailed(in); break;
			case ListGamesResult.LIST_GAMES_RESULT__TYPE: result = de.haumacher.wizard.msg.ListGamesResult.readListGamesResult(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;

}
