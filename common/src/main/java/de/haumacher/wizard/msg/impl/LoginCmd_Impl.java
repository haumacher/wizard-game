package de.haumacher.wizard.msg.impl;

/**
 * Base class for commands for account management.
 */
public abstract class LoginCmd_Impl extends de.haumacher.wizard.msg.impl.Cmd_Impl implements de.haumacher.wizard.msg.LoginCmd {

	/**
	 * Creates a {@link LoginCmd_Impl} instance.
	 */
	public LoginCmd_Impl() {
		super();
	}

	@Override
	public final <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Cmd.Visitor<R,A,E> v, A arg) throws E {
		return visit((de.haumacher.wizard.msg.LoginCmd.Visitor<R,A,E>) v, arg);
	}

}
