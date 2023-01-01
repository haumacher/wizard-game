package de.haumacher.wizard.msg.impl;

/**
 * A {@link Msg} targeting a running game and is only interesting for players of that game.
 */
public abstract class GameMsg_Impl extends de.haumacher.wizard.msg.impl.Msg_Impl implements de.haumacher.wizard.msg.GameMsg {

	/**
	 * Creates a {@link GameMsg_Impl} instance.
	 */
	public GameMsg_Impl() {
		super();
	}

	@Override
	public final <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Msg.Visitor<R,A,E> v, A arg) throws E {
		return visit((de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E>) v, arg);
	}

}
