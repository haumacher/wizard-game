package de.haumacher.wizard.msg;

/**
 * A message that is sent in direct result to a {@link Cmd}.
 */
public abstract class ResultMsg extends Msg {

	/** Visitor interface for the {@link ResultMsg} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link Error}.*/
		R visit(Error self, A arg) throws E;

		/** Visit case for {@link HelloResult}.*/
		R visit(HelloResult self, A arg) throws E;

		/** Visit case for {@link CreateAccountResult}.*/
		R visit(CreateAccountResult self, A arg) throws E;

		/** Visit case for {@link AddEmailSuccess}.*/
		R visit(AddEmailSuccess self, A arg) throws E;

		/** Visit case for {@link VerifyEmailSuccess}.*/
		R visit(VerifyEmailSuccess self, A arg) throws E;

		/** Visit case for {@link Welcome}.*/
		R visit(Welcome self, A arg) throws E;

		/** Visit case for {@link LoginFailed}.*/
		R visit(LoginFailed self, A arg) throws E;

		/** Visit case for {@link ListGamesResult}.*/
		R visit(ListGamesResult self, A arg) throws E;

	}

	/**
	 * Creates a {@link ResultMsg} instance.
	 */
	protected ResultMsg() {
		super();
	}

	@Override
	public abstract String jsonType();

	/** Reads a new instance from the given reader. */
	public static ResultMsg readResultMsg(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		ResultMsg result;
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


	@Override
	public final <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return visit((Visitor<R,A,E>) v, arg);
	}

}
