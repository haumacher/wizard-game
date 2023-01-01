package de.haumacher.wizard.msg.impl;

/**
 * Command that requests creating a new game on the server.
 * <p>
 * On success, a {@link GameCreated} response message is sent to all clients not currently participating in a game.
 * </p>
 */
public class CreateGame_Impl extends de.haumacher.wizard.msg.impl.Cmd_Impl implements de.haumacher.wizard.msg.CreateGame {

	/**
	 * Creates a {@link CreateGame_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.CreateGame#create()
	 */
	public CreateGame_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return CREATE_GAME__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
