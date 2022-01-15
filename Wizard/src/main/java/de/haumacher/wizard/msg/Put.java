package de.haumacher.wizard.msg;

public class Put extends GameCmd {

	/**
	 * Creates a {@link Put} instance.
	 */
	public static Put create() {
		return new Put();
	}

	/** Identifier for the {@link Put} type in JSON format. */
	public static final String PUT__TYPE = "Put";

	/** @see #getCard() */
	private static final String CARD = "card";

	private Card _card = null;

	/**
	 * Creates a {@link Put} instance.
	 *
	 * @see #create()
	 */
	protected Put() {
		super();
	}

	public final Card getCard() {
		return _card;
	}

	/**
	 * @see #getCard()
	 */
	public Put setCard(Card value) {
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
		return PUT__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Put readPut(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Put result = new Put();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasCard()) {
			out.name(CARD);
			getCard().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case CARD: setCard(de.haumacher.wizard.msg.Card.readCard(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
