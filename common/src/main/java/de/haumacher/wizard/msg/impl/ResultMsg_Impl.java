package de.haumacher.wizard.msg.impl;

/**
 * A message that is sent in direct result to a {@link Cmd}.
 */
public abstract class ResultMsg_Impl extends de.haumacher.wizard.msg.impl.Msg_Impl implements de.haumacher.wizard.msg.ResultMsg {

	/**
	 * Creates a {@link ResultMsg_Impl} instance.
	 */
	public ResultMsg_Impl() {
		super();
	}

	@Override
	public final <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Msg.Visitor<R,A,E> v, A arg) throws E {
		return visit((de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E>) v, arg);
	}

}
