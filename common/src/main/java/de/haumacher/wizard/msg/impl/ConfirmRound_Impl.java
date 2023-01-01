package de.haumacher.wizard.msg.impl;

/**
 * Command in response to {@link FinishRound} that must be received from all players, before the next round starts
 */
public class ConfirmRound_Impl extends de.haumacher.wizard.msg.impl.GameCmd_Impl implements de.haumacher.wizard.msg.ConfirmRound {

	/**
	 * Creates a {@link ConfirmRound_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.ConfirmRound#create()
	 */
	public ConfirmRound_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return CONFIRM_ROUND__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
