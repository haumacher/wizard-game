package de.haumacher.wizard.msg.impl;

/**
 * A card of the Wizard game.
 */
public class Card_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.Card {

	private de.haumacher.wizard.msg.Suit _suit = null;

	private de.haumacher.wizard.msg.Value _value = de.haumacher.wizard.msg.Value.N;

	/**
	 * Creates a {@link Card_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Card#create()
	 */
	public Card_Impl() {
		super();
	}

	@Override
	public final de.haumacher.wizard.msg.Suit getSuit() {
		return _suit;
	}

	@Override
	public de.haumacher.wizard.msg.Card setSuit(de.haumacher.wizard.msg.Suit value) {
		internalSetSuit(value);
		return this;
	}

	/** Internal setter for {@link #getSuit()} without chain call utility. */
	protected final void internalSetSuit(de.haumacher.wizard.msg.Suit value) {
		_suit = value;
	}

	@Override
	public final boolean hasSuit() {
		return _suit != null;
	}

	@Override
	public final de.haumacher.wizard.msg.Value getValue() {
		return _value;
	}

	@Override
	public de.haumacher.wizard.msg.Card setValue(de.haumacher.wizard.msg.Value value) {
		internalSetValue(value);
		return this;
	}

	/** Internal setter for {@link #getValue()} without chain call utility. */
	protected final void internalSetValue(de.haumacher.wizard.msg.Value value) {
		if (value == null) throw new IllegalArgumentException("Property 'value' cannot be null.");
		_value = value;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		if (hasSuit()) {
			out.name(SUIT__PROP);
			getSuit().writeTo(out);
		}
		out.name(VALUE__PROP);
		getValue().writeTo(out);
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case SUIT__PROP: setSuit(de.haumacher.wizard.msg.Suit.readSuit(in)); break;
			case VALUE__PROP: setValue(de.haumacher.wizard.msg.Value.readValue(in)); break;
			default: super.readField(in, field);
		}
	}

}
