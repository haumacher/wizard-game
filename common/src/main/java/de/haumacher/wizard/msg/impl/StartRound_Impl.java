package de.haumacher.wizard.msg.impl;

/**
 * Starts a round by assigning cards to players.
 */
public class StartRound_Impl extends de.haumacher.wizard.msg.impl.GameMsg_Impl implements de.haumacher.wizard.msg.StartRound {

	private int _round = 0;

	private int _maxRound = 0;

	private String _startPlayer = "";

	private final java.util.List<de.haumacher.wizard.msg.Player> _players = new java.util.ArrayList<>();

	private final java.util.List<de.haumacher.wizard.msg.Card> _cards = new java.util.ArrayList<>();

	private de.haumacher.wizard.msg.Card _trumpCard = null;

	/**
	 * Creates a {@link StartRound_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.StartRound#create()
	 */
	public StartRound_Impl() {
		super();
	}

	@Override
	public final int getRound() {
		return _round;
	}

	@Override
	public de.haumacher.wizard.msg.StartRound setRound(int value) {
		internalSetRound(value);
		return this;
	}

	/** Internal setter for {@link #getRound()} without chain call utility. */
	protected final void internalSetRound(int value) {
		_round = value;
	}

	@Override
	public final int getMaxRound() {
		return _maxRound;
	}

	@Override
	public de.haumacher.wizard.msg.StartRound setMaxRound(int value) {
		internalSetMaxRound(value);
		return this;
	}

	/** Internal setter for {@link #getMaxRound()} without chain call utility. */
	protected final void internalSetMaxRound(int value) {
		_maxRound = value;
	}

	@Override
	public final String getStartPlayer() {
		return _startPlayer;
	}

	@Override
	public de.haumacher.wizard.msg.StartRound setStartPlayer(String value) {
		internalSetStartPlayer(value);
		return this;
	}

	/** Internal setter for {@link #getStartPlayer()} without chain call utility. */
	protected final void internalSetStartPlayer(String value) {
		_startPlayer = value;
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.Player> getPlayers() {
		return _players;
	}

	@Override
	public de.haumacher.wizard.msg.StartRound setPlayers(java.util.List<? extends de.haumacher.wizard.msg.Player> value) {
		internalSetPlayers(value);
		return this;
	}

	/** Internal setter for {@link #getPlayers()} without chain call utility. */
	protected final void internalSetPlayers(java.util.List<? extends de.haumacher.wizard.msg.Player> value) {
		if (value == null) throw new IllegalArgumentException("Property 'players' cannot be null.");
		_players.clear();
		_players.addAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.StartRound addPlayer(de.haumacher.wizard.msg.Player value) {
		internalAddPlayer(value);
		return this;
	}

	/** Implementation of {@link #addPlayer(de.haumacher.wizard.msg.Player)} without chain call utility. */
	protected final void internalAddPlayer(de.haumacher.wizard.msg.Player value) {
		_players.add(value);
	}

	@Override
	public final void removePlayer(de.haumacher.wizard.msg.Player value) {
		_players.remove(value);
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.Card> getCards() {
		return _cards;
	}

	@Override
	public de.haumacher.wizard.msg.StartRound setCards(java.util.List<? extends de.haumacher.wizard.msg.Card> value) {
		internalSetCards(value);
		return this;
	}

	/** Internal setter for {@link #getCards()} without chain call utility. */
	protected final void internalSetCards(java.util.List<? extends de.haumacher.wizard.msg.Card> value) {
		if (value == null) throw new IllegalArgumentException("Property 'cards' cannot be null.");
		_cards.clear();
		_cards.addAll(value);
	}

	@Override
	public de.haumacher.wizard.msg.StartRound addCard(de.haumacher.wizard.msg.Card value) {
		internalAddCard(value);
		return this;
	}

	/** Implementation of {@link #addCard(de.haumacher.wizard.msg.Card)} without chain call utility. */
	protected final void internalAddCard(de.haumacher.wizard.msg.Card value) {
		_cards.add(value);
	}

	@Override
	public final void removeCard(de.haumacher.wizard.msg.Card value) {
		_cards.remove(value);
	}

	@Override
	public final de.haumacher.wizard.msg.Card getTrumpCard() {
		return _trumpCard;
	}

	@Override
	public de.haumacher.wizard.msg.StartRound setTrumpCard(de.haumacher.wizard.msg.Card value) {
		internalSetTrumpCard(value);
		return this;
	}

	/** Internal setter for {@link #getTrumpCard()} without chain call utility. */
	protected final void internalSetTrumpCard(de.haumacher.wizard.msg.Card value) {
		_trumpCard = value;
	}

	@Override
	public final boolean hasTrumpCard() {
		return _trumpCard != null;
	}

	@Override
	public String jsonType() {
		return START_ROUND__TYPE;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(ROUND__PROP);
		out.value(getRound());
		out.name(MAX_ROUND__PROP);
		out.value(getMaxRound());
		out.name(START_PLAYER__PROP);
		out.value(getStartPlayer());
		out.name(PLAYERS__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.Player x : getPlayers()) {
			x.writeTo(out);
		}
		out.endArray();
		out.name(CARDS__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.Card x : getCards()) {
			x.writeTo(out);
		}
		out.endArray();
		if (hasTrumpCard()) {
			out.name(TRUMP_CARD__PROP);
			getTrumpCard().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case ROUND__PROP: setRound(in.nextInt()); break;
			case MAX_ROUND__PROP: setMaxRound(in.nextInt()); break;
			case START_PLAYER__PROP: setStartPlayer(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case PLAYERS__PROP: {
				in.beginArray();
				while (in.hasNext()) {
					addPlayer(de.haumacher.wizard.msg.Player.readPlayer(in));
				}
				in.endArray();
			}
			break;
			case CARDS__PROP: {
				in.beginArray();
				while (in.hasNext()) {
					addCard(de.haumacher.wizard.msg.Card.readCard(in));
				}
				in.endArray();
			}
			break;
			case TRUMP_CARD__PROP: setTrumpCard(de.haumacher.wizard.msg.Card.readCard(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
