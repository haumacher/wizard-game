package de.haumacher.wizard.msg;

/**
 * {@link GameCmd} to select the trump suit in response to {@link RequestTrumpSelection}.
 */
public class SelectTrump extends GameCmd {

	/**
	 * Creates a {@link SelectTrump} instance.
	 */
	public static SelectTrump create() {
		return new de.haumacher.wizard.msg.SelectTrump();
	}

	/** Identifier for the {@link SelectTrump} type in JSON format. */
	public static final String SELECT_TRUMP__TYPE = "SelectTrump";

	/** @see #getTrumpSuit() */
	private static final String TRUMP_SUIT__PROP = "trumpSuit";

	private Suit _trumpSuit = de.haumacher.wizard.msg.Suit.DIAMOND;

	/**
	 * Creates a {@link SelectTrump} instance.
	 *
	 * @see SelectTrump#create()
	 */
	protected SelectTrump() {
		super();
	}

	/**
	 * The trump suit selected.
	 */
	public final Suit getTrumpSuit() {
		return _trumpSuit;
	}

	/**
	 * @see #getTrumpSuit()
	 */
	public SelectTrump setTrumpSuit(Suit value) {
		internalSetTrumpSuit(value);
		return this;
	}

	/** Internal setter for {@link #getTrumpSuit()} without chain call utility. */
	protected final void internalSetTrumpSuit(Suit value) {
		if (value == null) throw new IllegalArgumentException("Property 'trumpSuit' cannot be null.");
		_trumpSuit = value;
	}

	@Override
	public String jsonType() {
		return SELECT_TRUMP__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static SelectTrump readSelectTrump(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.SelectTrump result = new de.haumacher.wizard.msg.SelectTrump();
		result.readContent(in);
		return result;
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
	public <R,A,E extends Throwable> R visit(GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
