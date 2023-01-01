package de.haumacher.wizard.msg;

/**
 * Base class for commands for account management.
 */
public interface LoginCmd extends Cmd {

	/** Visitor interface for the {@link de.haumacher.wizard.msg.LoginCmd} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link de.haumacher.wizard.msg.Hello}.*/
		R visit(de.haumacher.wizard.msg.Hello self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.CreateAccount}.*/
		R visit(de.haumacher.wizard.msg.CreateAccount self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.AddEmail}.*/
		R visit(de.haumacher.wizard.msg.AddEmail self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.VerifyEmail}.*/
		R visit(de.haumacher.wizard.msg.VerifyEmail self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.Login}.*/
		R visit(de.haumacher.wizard.msg.Login self, A arg) throws E;

		/** Visit case for {@link de.haumacher.wizard.msg.Reconnect}.*/
		R visit(de.haumacher.wizard.msg.Reconnect self, A arg) throws E;

	}

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.LoginCmd readLoginCmd(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.LoginCmd result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case Hello.HELLO__TYPE: result = de.haumacher.wizard.msg.Hello.readHello(in); break;
			case CreateAccount.CREATE_ACCOUNT__TYPE: result = de.haumacher.wizard.msg.CreateAccount.readCreateAccount(in); break;
			case AddEmail.ADD_EMAIL__TYPE: result = de.haumacher.wizard.msg.AddEmail.readAddEmail(in); break;
			case VerifyEmail.VERIFY_EMAIL__TYPE: result = de.haumacher.wizard.msg.VerifyEmail.readVerifyEmail(in); break;
			case Login.LOGIN__TYPE: result = de.haumacher.wizard.msg.Login.readLogin(in); break;
			case Reconnect.RECONNECT__TYPE: result = de.haumacher.wizard.msg.Reconnect.readReconnect(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;

}
