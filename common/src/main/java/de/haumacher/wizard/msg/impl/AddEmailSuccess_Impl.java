package de.haumacher.wizard.msg.impl;

/**
 * Acknowledges adding the e-mail.
 */
public class AddEmailSuccess_Impl extends de.haumacher.wizard.msg.impl.ResultMsg_Impl implements de.haumacher.wizard.msg.AddEmailSuccess {

	/**
	 * Creates a {@link AddEmailSuccess_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.AddEmailSuccess#create()
	 */
	public AddEmailSuccess_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return ADD_EMAIL_SUCCESS__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
