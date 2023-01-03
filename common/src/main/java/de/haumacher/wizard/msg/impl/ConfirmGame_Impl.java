package de.haumacher.wizard.msg.impl;

/**
 * Command in response to {@link FinishGame} that must be received from all players, before the game is dropped.
 */
public class ConfirmGame_Impl extends de.haumacher.wizard.msg.impl.GameCmd_Impl implements de.haumacher.wizard.msg.ConfirmGame {

	/**
	 * Creates a {@link ConfirmGame_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.ConfirmGame#create()
	 */
	public ConfirmGame_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return CONFIRM_GAME__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
