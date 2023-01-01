package de.haumacher.wizard.msg.impl;

/**
 * Message sent at the end of each turn announcing the winner.
 */
public class FinishTurn_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.FinishTurn {

	private final java.util.List<de.haumacher.wizard.msg.Card> _trick = new java.util.ArrayList<>();

	private de.haumacher.wizard.msg.Player _winner = null;

	/**
	 * Creates a {@link FinishTurn_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.FinishTurn#create()
	 */
	public FinishTurn_Impl() {
		super();
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.Card> getTrick() {
		return _trick;
	}

	@Override
	public de.haumacher.wizard.msg.FinishTurn setTrick(java.util.List<? extends de.haumacher.wizard.msg.Card> value) {
		internalSetTrick(value);
		return this;
	}

	/** Internal setter for {@link #getTrick()} without chain call utility. */
	protected final void internalSetTrick(java.util.List<? extends de.haumacher.wizard.msg.Card> value) {
		if (value == null) throw new IllegalArgumentException("Property 'trick' cannot be null.");
		_trick.clear();
		_trick.addAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.FinishTurn addTrick(de.haumacher.wizard.msg.Card value) {
		internalAddTrick(value);
		return this;
	}

	/** Implementation of {@link #addTrick(de.haumacher.wizard.msg.Card)} without chain call utility. */
	protected final void internalAddTrick(de.haumacher.wizard.msg.Card value) {
		_trick.add(value);
	}

	@Override
	public final void removeTrick(de.haumacher.wizard.msg.Card value) {
		_trick.remove(value);
	}

	@Override
	public final de.haumacher.wizard.msg.Player getWinner() {
		return _winner;
	}

	@Override
	public de.haumacher.wizard.msg.FinishTurn setWinner(de.haumacher.wizard.msg.Player value) {
		internalSetWinner(value);
		return this;
	}

	/** Internal setter for {@link #getWinner()} without chain call utility. */
	protected final void internalSetWinner(de.haumacher.wizard.msg.Player value) {
		_winner = value;
	}

	@Override
	public final boolean hasWinner() {
		return _winner != null;
	}

	@Override
	public String jsonType() {
		return FINISH_TURN__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(TRICK__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.Card x : getTrick()) {
			x.writeTo(out);
		}
		out.endArray();
		if (hasWinner()) {
			out.name(WINNER__PROP);
			getWinner().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case TRICK__PROP: {
				in.beginArray();
				while (in.hasNext()) {
					addTrick(de.haumacher.wizard.msg.Card.readCard(in));
				}
				in.endArray();
			}
			break;
			case WINNER__PROP: setWinner(de.haumacher.wizard.msg.Player.readPlayer(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
