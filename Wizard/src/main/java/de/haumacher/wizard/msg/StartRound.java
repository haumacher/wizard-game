package de.haumacher.wizard.msg;

public class StartRound extends Msg {

	/**
	 * Creates a {@link StartRound} instance.
	 */
	public static StartRound create() {
		return new StartRound();
	}

	/** Identifier for the {@link StartRound} type in JSON format. */
	public static final String START_ROUND__TYPE = "StartRound";

	/** @see #getRound() */
	private static final String ROUND = "round";

	/** @see #getMaxRound() */
	private static final String MAX_ROUND = "maxRound";

	/** @see #getPlayers() */
	private static final String PLAYERS = "players";

	/** @see #getCards() */
	private static final String CARDS = "cards";

	/** @see #getTrumpCard() */
	private static final String TRUMP_CARD = "trumpCard";

	private int _round = 0;

	private int _maxRound = 0;

	private final java.util.List<Player> _players = new java.util.ArrayList<>();

	private final java.util.List<Card> _cards = new java.util.ArrayList<>();

	private Card _trumpCard = null;

	/**
	 * Creates a {@link StartRound} instance.
	 *
	 * @see #create()
	 */
	protected StartRound() {
		super();
	}

	public final int getRound() {
		return _round;
	}

	/**
	 * @see #getRound()
	 */
	public StartRound setRound(int value) {
		internalSetRound(value);
		return this;
	}
	/** Internal setter for {@link #getRound()} without chain call utility. */
	protected final void internalSetRound(int value) {
		_round = value;
	}


	public final int getMaxRound() {
		return _maxRound;
	}

	/**
	 * @see #getMaxRound()
	 */
	public StartRound setMaxRound(int value) {
		internalSetMaxRound(value);
		return this;
	}
	/** Internal setter for {@link #getMaxRound()} without chain call utility. */
	protected final void internalSetMaxRound(int value) {
		_maxRound = value;
	}


	public final java.util.List<Player> getPlayers() {
		return _players;
	}

	/**
	 * @see #getPlayers()
	 */
	public StartRound setPlayers(java.util.List<Player> value) {
		internalSetPlayers(value);
		return this;
	}
	/** Internal setter for {@link #getPlayers()} without chain call utility. */
	protected final void internalSetPlayers(java.util.List<Player> value) {
		if (value == null) throw new IllegalArgumentException("Property 'players' cannot be null.");
		_players.clear();
		_players.addAll(value);
	}


	/**
	 * Adds a value to the {@link #getPlayers()} list.
	 */
	public StartRound addPlayer(Player value) {
		internalAddPlayer(value);
		return this;
	}

	/** Implementation of {@link #addPlayer(Player)} without chain call utility. */
	protected final void internalAddPlayer(Player value) {
		_players.add(value);
	}

	/**
	 * Removes a value from the {@link #getPlayers()} list.
	 */
	public final void removePlayer(Player value) {
		_players.remove(value);
	}

	public final java.util.List<Card> getCards() {
		return _cards;
	}

	/**
	 * @see #getCards()
	 */
	public StartRound setCards(java.util.List<Card> value) {
		internalSetCards(value);
		return this;
	}
	/** Internal setter for {@link #getCards()} without chain call utility. */
	protected final void internalSetCards(java.util.List<Card> value) {
		if (value == null) throw new IllegalArgumentException("Property 'cards' cannot be null.");
		_cards.clear();
		_cards.addAll(value);
	}


	/**
	 * Adds a value to the {@link #getCards()} list.
	 */
	public StartRound addCard(Card value) {
		internalAddCard(value);
		return this;
	}

	/** Implementation of {@link #addCard(Card)} without chain call utility. */
	protected final void internalAddCard(Card value) {
		_cards.add(value);
	}

	/**
	 * Removes a value from the {@link #getCards()} list.
	 */
	public final void removeCard(Card value) {
		_cards.remove(value);
	}

	public final Card getTrumpCard() {
		return _trumpCard;
	}

	/**
	 * @see #getTrumpCard()
	 */
	public StartRound setTrumpCard(Card value) {
		internalSetTrumpCard(value);
		return this;
	}
	/** Internal setter for {@link #getTrumpCard()} without chain call utility. */
	protected final void internalSetTrumpCard(Card value) {
		_trumpCard = value;
	}


	/**
	 * Checks, whether {@link #getTrumpCard()} has a value.
	 */
	public final boolean hasTrumpCard() {
		return _trumpCard != null;
	}

	@Override
	public String jsonType() {
		return START_ROUND__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static StartRound readStartRound(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		StartRound result = new StartRound();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(ROUND);
		out.value(getRound());
		out.name(MAX_ROUND);
		out.value(getMaxRound());
		out.name(PLAYERS);
		out.beginArray();
		for (Player x : getPlayers()) {
			x.writeTo(out);
		}
		out.endArray();
		out.name(CARDS);
		out.beginArray();
		for (Card x : getCards()) {
			x.writeTo(out);
		}
		out.endArray();
		if (hasTrumpCard()) {
			out.name(TRUMP_CARD);
			getTrumpCard().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case ROUND: setRound(in.nextInt()); break;
			case MAX_ROUND: setMaxRound(in.nextInt()); break;
			case PLAYERS: {
				in.beginArray();
				while (in.hasNext()) {
					addPlayer(de.haumacher.wizard.msg.Player.readPlayer(in));
				}
				in.endArray();
			}
			break;
			case CARDS: {
				in.beginArray();
				while (in.hasNext()) {
					addCard(de.haumacher.wizard.msg.Card.readCard(in));
				}
				in.endArray();
			}
			break;
			case TRUMP_CARD: setTrumpCard(de.haumacher.wizard.msg.Card.readCard(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public <R,A,E extends Throwable> R visit(Msg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
