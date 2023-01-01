package de.haumacher.wizard.msg.impl;

/**
 * Acknowledges the e-mail verification.
 */
public class VerifyEmailSuccess_Impl extends de.haumacher.wizard.msg.impl.ResultMsg_Impl implements de.haumacher.wizard.msg.VerifyEmailSuccess {

	/**
	 * Creates a {@link VerifyEmailSuccess_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.VerifyEmailSuccess#create()
	 */
	public VerifyEmailSuccess_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return VERIFY_EMAIL_SUCCESS__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
