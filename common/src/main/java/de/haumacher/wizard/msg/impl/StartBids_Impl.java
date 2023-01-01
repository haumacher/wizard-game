package de.haumacher.wizard.msg.impl;

/**
 * Message that starts the bid phase.
 */
public class StartBids_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.StartBids {

	/**
	 * Creates a {@link StartBids_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.StartBids#create()
	 */
	public StartBids_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return START_BIDS__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
