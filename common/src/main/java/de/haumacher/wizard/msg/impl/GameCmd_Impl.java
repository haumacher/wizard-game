package de.haumacher.wizard.msg.impl;

/**
 * A {@link Cmd} targeting a running game.
 */
public abstract class GameCmd_Impl extends de.haumacher.wizard.msg.impl.Cmd_Impl implements de.haumacher.wizard.msg.GameCmd {

	/**
	 * Creates a {@link GameCmd_Impl} instance.
	 */
	public GameCmd_Impl() {
		super();
	}

	@Override
	public final <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Cmd.Visitor<R,A,E> v, A arg) throws E {
		return visit((de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E>) v, arg);
	}

}
