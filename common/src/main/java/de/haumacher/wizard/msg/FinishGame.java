package de.haumacher.wizard.msg;

/**
 * Message sent after the last round of a game.
 */
public class FinishGame extends GameMsg {

	/**
	 * Creates a {@link FinishGame} instance.
	 */
	public static FinishGame create() {
		return new FinishGame();
	}

	/** Identifier for the {@link FinishGame} type in JSON format. */
	public static final String FINISH_GAME__TYPE = "FinishGame";

	/** @see #getScores() */
	private static final String SCORES = "scores";

	private final java.util.List<PlayerScore> _scores = new java.util.ArrayList<>();

	/**
	 * Creates a {@link FinishGame} instance.
	 *
	 * @see #create()
	 */
	protected FinishGame() {
		super();
	}

	/**
	 * The total score for each player. The list is ordered by the points the players have won.
	 */
	public final java.util.List<PlayerScore> getScores() {
		return _scores;
	}

	/**
	 * @see #getScores()
	 */
	public FinishGame setScores(java.util.List<PlayerScore> value) {
		internalSetScores(value);
		return this;
	}
	/** Internal setter for {@link #getScores()} without chain call utility. */
	protected final void internalSetScores(java.util.List<PlayerScore> value) {
		if (value == null) throw new IllegalArgumentException("Property 'scores' cannot be null.");
		_scores.clear();
		_scores.addAll(value);
	}


	/**
	 * Adds a value to the {@link #getScores()} list.
	 */
	public FinishGame addScore(PlayerScore value) {
		internalAddScore(value);
		return this;
	}

	/** Implementation of {@link #addScore(PlayerScore)} without chain call utility. */
	protected final void internalAddScore(PlayerScore value) {
		_scores.add(value);
	}

	/**
	 * Removes a value from the {@link #getScores()} list.
	 */
	public final void removeScore(PlayerScore value) {
		_scores.remove(value);
	}

	@Override
	public String jsonType() {
		return FINISH_GAME__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static FinishGame readFinishGame(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		FinishGame result = new FinishGame();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(SCORES);
		out.beginArray();
		for (PlayerScore x : getScores()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case SCORES: {
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
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
