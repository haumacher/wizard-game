package de.haumacher.wizard.msg;

public class SelectTrump extends GameCmd {

	/**
	 * Creates a {@link SelectTrump} instance.
	 */
	public static SelectTrump create() {
		return new SelectTrump();
	}

	/** Identifier for the {@link SelectTrump} type in JSON format. */
	public static final String SELECT_TRUMP__TYPE = "SelectTrump";

	/** @see #getTrumpColor() */
	private static final String TRUMP_COLOR = "trumpColor";

	private Color _trumpColor = de.haumacher.wizard.msg.Color.YELLOW;

	/**
	 * Creates a {@link SelectTrump} instance.
	 *
	 * @see #create()
	 */
	protected SelectTrump() {
		super();
	}

	public final Color getTrumpColor() {
		return _trumpColor;
	}

	/**
	 * @see #getTrumpColor()
	 */
	public SelectTrump setTrumpColor(Color value) {
		internalSetTrumpColor(value);
		return this;
	}
	/** Internal setter for {@link #getTrumpColor()} without chain call utility. */
	protected final void internalSetTrumpColor(Color value) {
		if (value == null) throw new IllegalArgumentException("Property 'trumpColor' cannot be null.");
		_trumpColor = value;
	}


	@Override
	public String jsonType() {
		return SELECT_TRUMP__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static SelectTrump readSelectTrump(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		SelectTrump result = new SelectTrump();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(TRUMP_COLOR);
		getTrumpColor().writeTo(out);
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case TRUMP_COLOR: setTrumpColor(de.haumacher.wizard.msg.Color.readColor(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(GameCmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
