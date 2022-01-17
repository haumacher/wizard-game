package de.haumacher.wizard.msg;

/**
 * The state of a player as part of {@link PlayerState}.
 */
public class RoundState extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link RoundState} instance.
	 */
	public static RoundState create() {
		return new RoundState();
	}

	/** Identifier for the {@link RoundState} type in JSON format. */
	public static final String ROUND_STATE__TYPE = "RoundState";

	/** @see #getBidCnt() */
	private static final String BID_CNT = "bidCnt";

	/** @see #getWinCnt() */
	private static final String WIN_CNT = "winCnt";

	/** @see #getCards() */
	private static final String CARDS = "cards";

	private int _bidCnt = 0;

	private int _winCnt = 0;

	private final java.util.List<Card> _cards = new java.util.ArrayList<>();

	/**
	 * Creates a {@link RoundState} instance.
	 *
	 * @see #create()
	 */
	protected RoundState() {
		super();
	}

	/**
	 * The player's bid.
	 */
	public final int getBidCnt() {
		return _bidCnt;
	}

	/**
	 * @see #getBidCnt()
	 */
	public RoundState setBidCnt(int value) {
		internalSetBidCnt(value);
		return this;
	}
	/** Internal setter for {@link #getBidCnt()} without chain call utility. */
	protected final void internalSetBidCnt(int value) {
		_bidCnt = value;
	}


	/**
	 * The number of tricks won.
	 */
	public final int getWinCnt() {
		return _winCnt;
	}

	/**
	 * @see #getWinCnt()
	 */
	public RoundState setWinCnt(int value) {
		internalSetWinCnt(value);
		return this;
	}
	/** Internal setter for {@link #getWinCnt()} without chain call utility. */
	protected final void internalSetWinCnt(int value) {
		_winCnt = value;
	}


	/**
	 * The cards that the player has currently in his hand.
	 */
	public final java.util.List<Card> getCards() {
		return _cards;
	}

	/**
	 * @see #getCards()
	 */
	public RoundState setCards(java.util.List<Card> value) {
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
	public RoundState addCard(Card value) {
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

	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return ROUND_STATE__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static RoundState readRoundState(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		RoundState result = new RoundState();
		in.beginObject();
		result.readFields(in);
		in.endObject();
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(BID_CNT);
		out.value(getBidCnt());
		out.name(WIN_CNT);
		out.value(getWinCnt());
		out.name(CARDS);
		out.beginArray();
		for (Card x : getCards()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case BID_CNT: setBidCnt(in.nextInt()); break;
			case WIN_CNT: setWinCnt(in.nextInt()); break;
			case CARDS: {
				in.beginArray();
				while (in.hasNext()) {
					addCard(de.haumacher.wizard.msg.Card.readCard(in));
				}
				in.endArray();
			}
			break;
			default: super.readField(in, field);
		}
	}

}
