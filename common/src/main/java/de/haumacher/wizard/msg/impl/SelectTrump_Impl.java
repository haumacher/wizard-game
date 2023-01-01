package de.haumacher.wizard.msg.impl;

/**
 * {@link GameCmd} to select the trump suit in response to {@link RequestTrumpSelection}.
 */
public class SelectTrump_Impl extends de.haumacher.wizard.msg.impl.GameCmd_Impl implements de.haumacher.wizard.msg.SelectTrump {

	private de.haumacher.wizard.msg.Suit _trumpSuit = de.haumacher.wizard.msg.Suit.DIAMOND;

	/**
	 * Creates a {@link SelectTrump_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.SelectTrump#create()
	 */
	public SelectTrump_Impl() {
		super();
	}

	@Override
	public final de.haumacher.wizard.msg.Suit getTrumpSuit() {
		return _trumpSuit;
	}

	@Override
	public de.haumacher.wizard.msg.SelectTrump setTrumpSuit(de.haumacher.wizard.msg.Suit value) {
		internalSetTrumpSuit(value);
		return this;
	}

	/** Internal setter for {@link #getTrumpSuit()} without chain call utility. */
	protected final void internalSetTrumpSuit(de.haumacher.wizard.msg.Suit value) {
		if (value == null) throw new IllegalArgumentException("Property 'trumpSuit' cannot be null.");
		_trumpSuit = value;
	}

	@Override
	public String jsonType() {
		return SELECT_TRUMP__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(TRUMP_SUIT__PROP);
		getTrumpSuit().writeTo(out);
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case TRUMP_SUIT__PROP: setTrumpSuit(de.haumacher.wizard.msg.Suit.readSuit(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
