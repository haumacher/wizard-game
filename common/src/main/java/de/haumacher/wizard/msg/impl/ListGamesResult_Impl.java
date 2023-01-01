package de.haumacher.wizard.msg.impl;

/**
 * Provides a listing of games waiting for players.
 */
public class ListGamesResult_Impl extends de.haumacher.wizard.msg.impl.ResultMsg_Impl implements de.haumacher.wizard.msg.ListGamesResult {

	private final java.util.List<de.haumacher.wizard.msg.Game> _games = new java.util.ArrayList<>();

	/**
	 * Creates a {@link ListGamesResult_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.ListGamesResult#create()
	 */
	public ListGamesResult_Impl() {
		super();
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.Game> getGames() {
		return _games;
	}

	@Override
	public de.haumacher.wizard.msg.ListGamesResult setGames(java.util.List<? extends de.haumacher.wizard.msg.Game> value) {
		internalSetGames(value);
		return this;
	}

	/** Internal setter for {@link #getGames()} without chain call utility. */
	protected final void internalSetGames(java.util.List<? extends de.haumacher.wizard.msg.Game> value) {
		if (value == null) throw new IllegalArgumentException("Property 'games' cannot be null.");
		_games.clear();
		_games.addAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.ListGamesResult addGame(de.haumacher.wizard.msg.Game value) {
		internalAddGame(value);
		return this;
	}

	/** Implementation of {@link #addGame(de.haumacher.wizard.msg.Game)} without chain call utility. */
	protected final void internalAddGame(de.haumacher.wizard.msg.Game value) {
		_games.add(value);
	}

	@Override
	public final void removeGame(de.haumacher.wizard.msg.Game value) {
		_games.remove(value);
	}

	@Override
	public String jsonType() {
		return LIST_GAMES_RESULT__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(GAMES__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.Game x : getGames()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case GAMES__PROP: {
				in.beginArray();
				while (in.hasNext()) {
					addGame(de.haumacher.wizard.msg.Game.readGame(in));
				}
				in.endArray();
			}
			break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.ResultMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
