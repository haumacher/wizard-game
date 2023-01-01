package de.haumacher.wizard.msg;

/**
 * Puts a card on the table.
 */
public class Lead extends GameCmd {

	/**
	 * Creates a {@link Lead} instance.
	 */
	public static Lead create() {
		return new de.haumacher.wizard.msg.Lead();
	}

	/** Identifier for the {@link Lead} type in JSON format. */
	public static final String LEAD__TYPE = "Lead";

	/** @see #getCard() */
	private static final String CARD__PROP = "card";

	private Card _card = null;

	/**
	 * Creates a {@link Lead} instance.
	 *
	 * @see Lead#create()
	 */
	protected Lead() {
		super();
	}

	/**
	 * The card that is led.
	 */
	public final Card getCard() {
		return _card;
	}

	/**
	 * @see #getCard()
	 */
	public Lead setCard(Card value) {
		internalSetCard(value);
		return this;
	}

	/** Internal setter for {@link #getCard()} without chain call utility. */
	protected final void internalSetCard(Card value) {
		_card = value;
	}

	/**
	 * Checks, whether {@link #getCard()} has a value.
	 */
	public final boolean hasCard() {
		return _card != null;
	}

	@Override
	public String jsonType() {
		return LEAD__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Lead readLead(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Lead result = new de.haumacher.wizard.msg.Lead();
		result.readContent(in);
		return result;
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
	public <R,A,E extends Throwable> R visit(GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
