package de.haumacher.wizard.msg.impl;

/**
 * Command in response to {@link FinishTurn} that must be received from all players, before the next turn starts
 */
public class ConfirmTrick_Impl extends de.haumacher.wizard.msg.impl.GameCmd_Impl implements de.haumacher.wizard.msg.ConfirmTrick {

	/**
	 * Creates a {@link ConfirmTrick_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.ConfirmTrick#create()
	 */
	public ConfirmTrick_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return CONFIRM_TRICK__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
