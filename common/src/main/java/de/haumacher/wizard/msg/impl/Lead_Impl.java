package de.haumacher.wizard.msg.impl;

/**
 * Puts a card on the table.
 */
public class Lead_Impl extends de.haumacher.wizard.msg.impl.GameCmd_Impl implements de.haumacher.wizard.msg.Lead {

	private de.haumacher.wizard.msg.Card _card = null;

	/**
	 * Creates a {@link Lead_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Lead#create()
	 */
	public Lead_Impl() {
		super();
	}

	@Override
	public final de.haumacher.wizard.msg.Card getCard() {
		return _card;
	}

	@Override
	public de.haumacher.wizard.msg.Lead setCard(de.haumacher.wizard.msg.Card value) {
		internalSetCard(value);
		return this;
	}

	/** Internal setter for {@link #getCard()} without chain call utility. */
	protected final void internalSetCard(de.haumacher.wizard.msg.Card value) {
		_card = value;
	}

	@Override
	public final boolean hasCard() {
		return _card != null;
	}

	@Override
	public String jsonType() {
		return LEAD__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasCard()) {
			out.name(CARD__PROP);
			getCard().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case CARD__PROP: setCard(de.haumacher.wizard.msg.Card.readCard(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
