package de.haumacher.wizard.msg.impl;

/**
 * The state of a player as part of {@link PlayerState}.
 */
public class RoundState_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.RoundState {

	private int _bidCnt = 0;

	private int _winCnt = 0;

	private final java.util.List<de.haumacher.wizard.msg.Card> _cards = new java.util.ArrayList<>();

	/**
	 * Creates a {@link RoundState_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.RoundState#create()
	 */
	public RoundState_Impl() {
		super();
	}

	@Override
	public final int getBidCnt() {
		return _bidCnt;
	}

	@Override
	public de.haumacher.wizard.msg.RoundState setBidCnt(int value) {
		internalSetBidCnt(value);
		return this;
	}

	/** Internal setter for {@link #getBidCnt()} without chain call utility. */
	protected final void internalSetBidCnt(int value) {
		_bidCnt = value;
	}

	@Override
	public final int getWinCnt() {
		return _winCnt;
	}

	@Override
	public de.haumacher.wizard.msg.RoundState setWinCnt(int value) {
		internalSetWinCnt(value);
		return this;
	}

	/** Internal setter for {@link #getWinCnt()} without chain call utility. */
	protected final void internalSetWinCnt(int value) {
		_winCnt = value;
	}

	@Override
	public final java.util.List<de.haumacher.wizard.msg.Card> getCards() {
		return _cards;
	}

	@Override
	public de.haumacher.wizard.msg.RoundState setCards(java.util.List<? extends de.haumacher.wizard.msg.Card> value) {
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
	public de.haumacher.wizard.msg.RoundState addCard(de.haumacher.wizard.msg.Card value) {
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
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(BID_CNT__PROP);
		out.value(getBidCnt());
		out.name(WIN_CNT__PROP);
		out.value(getWinCnt());
		out.name(CARDS__PROP);
		out.beginArray();
		for (de.haumacher.wizard.msg.Card x : getCards()) {
			x.writeTo(out);
		}
		out.endArray();
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case BID_CNT__PROP: setBidCnt(in.nextInt()); break;
			case WIN_CNT__PROP: setWinCnt(in.nextInt()); break;
			case CARDS__PROP: {
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
