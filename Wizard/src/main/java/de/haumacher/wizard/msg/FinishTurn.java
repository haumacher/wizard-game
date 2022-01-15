package de.haumacher.wizard.msg;

public class FinishTurn extends Msg {

	/**
	 * Creates a {@link FinishTurn} instance.
	 */
	public static FinishTurn create() {
		return new FinishTurn();
	}

	/** Identifier for the {@link FinishTurn} type in JSON format. */
	public static final String FINISH_TURN__TYPE = "FinishTurn";

	/** @see #getTrick() */
	private static final String TRICK = "trick";

	/** @see #getWinner() */
	private static final String WINNER = "winner";

	private final java.util.List<Card> _trick = new java.util.ArrayList<>();

	private Player _winner = null;

	/**
	 * Creates a {@link FinishTurn} instance.
	 *
	 * @see #create()
	 */
	protected FinishTurn() {
		super();
	}

	public final java.util.List<Card> getTrick() {
		return _trick;
	}

	/**
	 * @see #getTrick()
	 */
	public FinishTurn setTrick(java.util.List<Card> value) {
		internalSetTrick(value);
		return this;
	}
	/** Internal setter for {@link #getTrick()} without chain call utility. */
	protected final void internalSetTrick(java.util.List<Card> value) {
		if (value == null) throw new IllegalArgumentException("Property 'trick' cannot be null.");
		_trick.clear();
		_trick.addAll(value);
	}


	/**
	 * Adds a value to the {@link #getTrick()} list.
	 */
	public FinishTurn addTrick(Card value) {
		internalAddTrick(value);
		return this;
	}

	/** Implementation of {@link #addTrick(Card)} without chain call utility. */
	protected final void internalAddTrick(Card value) {
		_trick.add(value);
	}

	/**
	 * Removes a value from the {@link #getTrick()} list.
	 */
	public final void removeTrick(Card value) {
		_trick.remove(value);
	}

	public final Player getWinner() {
		return _winner;
	}

	/**
	 * @see #getWinner()
	 */
	public FinishTurn setWinner(Player value) {
		internalSetWinner(value);
		return this;
	}
	/** Internal setter for {@link #getWinner()} without chain call utility. */
	protected final void internalSetWinner(Player value) {
		_winner = value;
	}


	/**
	 * Checks, whether {@link #getWinner()} has a value.
	 */
	public final boolean hasWinner() {
		return _winner != null;
	}

	@Override
	public String jsonType() {
		return FINISH_TURN__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static FinishTurn readFinishTurn(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		FinishTurn result = new FinishTurn();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(TRICK);
		out.beginArray();
		for (Card x : getTrick()) {
			x.writeTo(out);
		}
		out.endArray();
		if (hasWinner()) {
			out.name(WINNER);
			getWinner().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case TRICK: {
				in.beginArray();
				while (in.hasNext()) {
					addTrick(de.haumacher.wizard.msg.Card.readCard(in));
				}
				in.endArray();
			}
			break;
			case WINNER: setWinner(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
