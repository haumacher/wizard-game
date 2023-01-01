package de.haumacher.wizard.msg.impl;

/**
 * Message sent after the last round of a game.
 */
public class FinishGame_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.FinishGame {

	private final java.util.List<de.haumacher.wizard.msg.PlayerScore> _scores = new java.util.ArrayList<>();

	/**
	 * Creates a {@link FinishGame_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.FinishGame#create()
	 */
	public FinishGame_Impl() {
		super();
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.PlayerScore> getScores() {
		return _scores;
	}

	@Override
	public de.haumacher.wizard.msg.FinishGame setScores(java.util.List<? extends de.haumacher.wizard.msg.PlayerScore> value) {
		internalSetScores(value);
		return this;
	}

	/** Internal setter for {@link #getScores()} without chain call utility. */
	protected final void internalSetScores(java.util.List<? extends de.haumacher.wizard.msg.PlayerScore> value) {
		if (value == null) throw new IllegalArgumentException("Property 'scores' cannot be null.");
		_scores.clear();
		_scores.addAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.FinishGame addScore(de.haumacher.wizard.msg.PlayerScore value) {
		internalAddScore(value);
		return this;
	}

	/** Implementation of {@link #addScore(de.haumacher.wizard.msg.PlayerScore)} without chain call utility. */
	protected final void internalAddScore(de.haumacher.wizard.msg.PlayerScore value) {
		_scores.add(value);
	}

	@Override
	public final void removeScore(de.haumacher.wizard.msg.PlayerScore value) {
		_scores.remove(value);
	}

	@Override
	public String jsonType() {
		return FINISH_GAME__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(SCORES__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.PlayerScore x : getScores()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case SCORES__PROP: {
				in.beginArray();
				while (in.hasNext()) {
					addScore(de.haumacher.wizard.msg.PlayerScore.readPlayerScore(in));
				}
				in.endArray();
			}
			break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
