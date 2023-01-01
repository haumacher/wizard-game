package de.haumacher.wizard.msg;

/**
 * Starts a round by assigning cards to players.
 */
public class StartRound extends GameMsg {

	/**
	 * Creates a {@link StartRound} instance.
	 */
	public static StartRound create() {
		return new de.haumacher.wizard.msg.StartRound();
	}

	/** Identifier for the {@link StartRound} type in JSON format. */
	public static final String START_ROUND__TYPE = "StartRound";

	/** @see #getRound() */
	private static final String ROUND__PROP = "round";

	/** @see #getMaxRound() */
	private static final String MAX_ROUND__PROP = "maxRound";

	/** @see #getStartPlayer() */
	private static final String START_PLAYER__PROP = "startPlayer";

	/** @see #getPlayers() */
	private static final String PLAYERS__PROP = "players";

	/** @see #getCards() */
	private static final String CARDS__PROP = "cards";

	/** @see #getTrumpCard() */
	private static final String TRUMP_CARD__PROP = "trumpCard";

	private int _round = 0;

	private int _maxRound = 0;

	private String _startPlayer = "";

	private final java.util.List<Player> _players = new java.util.ArrayList<>();

	private final java.util.List<Card> _cards = new java.util.ArrayList<>();

	private Card _trumpCard = null;

	/**
	 * Creates a {@link StartRound} instance.
	 *
	 * @see StartRound#create()
	 */
	protected StartRound() {
		super();
	}

	/**
	 * The number of the round. 1 is the first round.
	 */
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

	/**
	 * The number of rounds being played in this game (depends on the number of players).
	 */
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

	/**
	 * The ID of the player that must make the first bid and leads the first card.
	 */
	public final String getStartPlayer() {
		return _startPlayer;
	}

	/**
	 * @see #getStartPlayer()
	 */
	public StartRound setStartPlayer(String value) {
		internalSetStartPlayer(value);
		return this;
	}

	/** Internal setter for {@link #getStartPlayer()} without chain call utility. */
	protected final void internalSetStartPlayer(String value) {
		_startPlayer = value;
	}

	/**
	 * The players and their order in this round.
	 */
	public final java.util.List<Player> getPlayers() {
		return _players;
	}

	/**
	 * @see #getPlayers()
	 */
	public StartRound setPlayers(java.util.List<? extends Player> value) {
		internalSetPlayers(value);
		return this;
	}

	/** Internal setter for {@link #getPlayers()} without chain call utility. */
	protected final void internalSetPlayers(java.util.List<? extends Player> value) {
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

	/**
	 * The cards given to the player that receives this message.
	 */
	public final java.util.List<Card> getCards() {
		return _cards;
	}

	/**
	 * @see #getCards()
	 */
	public StartRound setCards(java.util.List<? extends Card> value) {
		internalSetCards(value);
		return this;
	}

	/** Internal setter for {@link #getCards()} without chain call utility. */
	protected final void internalSetCards(java.util.List<? extends Card> value) {
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

	/**
	 * The given trump card. In the last round, there is no trump card.
	 */
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
		de.haumacher.wizard.msg.StartRound result = new de.haumacher.wizard.msg.StartRound();
		result.readContent(in);
		return result;
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
		for (Player x : getPlayers()) {
			x.writeTo(out);
		}
		out.endArray();
		out.name(CARDS__PROP);
		out.beginArray();
		for (Card x : getCards()) {
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
	public <R,A,E extends Throwable> R visit(GameMsg.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
