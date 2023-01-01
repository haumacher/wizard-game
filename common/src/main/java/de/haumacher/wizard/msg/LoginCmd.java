package de.haumacher.wizard.msg;

/**
 * Base class for commands for account management.
 */
public abstract class LoginCmd extends Cmd {

	/** Visitor interface for the {@link LoginCmd} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link Hello}.*/
		R visit(Hello self, A arg) throws E;

		/** Visit case for {@link CreateAccount}.*/
		R visit(CreateAccount self, A arg) throws E;

		/** Visit case for {@link AddEmail}.*/
		R visit(AddEmail self, A arg) throws E;

		/** Visit case for {@link VerifyEmail}.*/
		R visit(VerifyEmail self, A arg) throws E;

		/** Visit case for {@link Login}.*/
		R visit(Login self, A arg) throws E;

		/** Visit case for {@link Reconnect}.*/
		R visit(Reconnect self, A arg) throws E;

	}

	/**
	 * Creates a {@link LoginCmd} instance.
	 */
	protected LoginCmd() {
		super();
	}

	/** The type identifier for this concrete subtype. */
	public abstract String jsonType();

	/** Reads a new instance from the given reader. */
	public static LoginCmd readLoginCmd(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		LoginCmd result;
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

	@Override
	public final <R,A,E extends Throwable> R visit(Cmd.Visitor<R,A,E> v, A arg) throws E {
		return visit((LoginCmd.Visitor<R,A,E>) v, arg);
	}

}
