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

	/** @see #getColor() */
	private static final String COLOR = "color";

	/** @see #getValue() */
	private static final String VALUE = "value";

	private Color _color = null;

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
	 * The card color, <code>null</code> for wizards and fools.
	 */
	public final Color getColor() {
		return _color;
	}

	/**
	 * @see #getColor()
	 */
	public Card setColor(Color value) {
		internalSetColor(value);
		return this;
	}
	/** Internal setter for {@link #getColor()} without chain call utility. */
	protected final void internalSetColor(Color value) {
		_color = value;
	}


	/**
	 * Checks, whether {@link #getColor()} has a value.
	 */
	public final boolean hasColor() {
		return _color != null;
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
		if (hasColor()) {
			out.name(COLOR);
			getColor().writeTo(out);
		}
		out.name(VALUE);
		getValue().writeTo(out);
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case COLOR: setColor(de.haumacher.wizard.msg.Color.readColor(in)); break;
			case VALUE: setValue(de.haumacher.wizard.msg.Value.readValue(in)); break;
			default: super.readField(in, field);
		}
	}

}
