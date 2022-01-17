package de.haumacher.wizard.msg;

/**
 * A card of the Wizard game.
 */
public class Card extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link Card} instance.
	 */
	public static Card create() {
		return new Card();
	}

	/** Identifier for the {@link Card} type in JSON format. */
	public static final String CARD__TYPE = "Card";

	/** @see #getSuit() */
	private static final String SUIT = "suit";

	/** @see #getValue() */
	private static final String VALUE = "value";

	private Suit _suit = null;

	private Value _value = de.haumacher.wizard.msg.Value.N;

	/**
	 * Creates a {@link Card} instance.
	 *
	 * @see #create()
	 */
	protected Card() {
		super();
	}

	/**
	 * The card suit, <code>null</code> for wizards and fools.
	 */
	public final Suit getSuit() {
		return _suit;
	}

	/**
	 * @see #getSuit()
	 */
	public Card setSuit(Suit value) {
		internalSetSuit(value);
		return this;
	}
	/** Internal setter for {@link #getSuit()} without chain call utility. */
	protected final void internalSetSuit(Suit value) {
		_suit = value;
	}


	/**
	 * Checks, whether {@link #getSuit()} has a value.
	 */
	public final boolean hasSuit() {
		return _suit != null;
	}

	/**
	 * The value of the card.
	 */
	public final Value getValue() {
		return _value;
	}

	/**
	 * @see #getValue()
	 */
	public Card setValue(Value value) {
		internalSetValue(value);
		return this;
	}
	/** Internal setter for {@link #getValue()} without chain call utility. */
	protected final void internalSetValue(Value value) {
		if (value == null) throw new IllegalArgumentException("Property 'value' cannot be null.");
		_value = value;
	}


	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return CARD__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Card readCard(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Card result = new Card();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasSuit()) {
			out.name(SUIT);
			getSuit().writeTo(out);
		}
		out.name(VALUE);
		getValue().writeTo(out);
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case SUIT: setSuit(de.haumacher.wizard.msg.Suit.readSuit(in)); break;
			case VALUE: setValue(de.haumacher.wizard.msg.Value.readValue(in)); break;
			default: super.readField(in, field);
		}
	}

}
