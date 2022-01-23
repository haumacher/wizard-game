package de.haumacher.wizard.msg;

/**
 * Info about a {@link Card} played.
 */
public class PlayedCard extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link PlayedCard} instance.
	 */
	public static PlayedCard create() {
		return new PlayedCard();
	}

	/** Identifier for the {@link PlayedCard} type in JSON format. */
	public static final String PLAYED_CARD__TYPE = "PlayedCard";

	/** @see #getPlayerId() */
	private static final String PLAYER_ID = "playerId";

	/** @see #getCard() */
	private static final String CARD = "card";

	private String _playerId = "";

	private Card _card = null;

	/**
	 * Creates a {@link PlayedCard} instance.
	 *
	 * @see #create()
	 */
	protected PlayedCard() {
		super();
	}

	/**
	 * ID of the player that played the {@link #getCard()}.
	 */
	public final String getPlayerId() {
		return _playerId;
	}

	/**
	 * @see #getPlayerId()
	 */
	public PlayedCard setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}
	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}


	/**
	 * The {@link Card} that lays on the table.
	 */
	public final Card getCard() {
		return _card;
	}

	/**
	 * @see #getCard()
	 */
	public PlayedCard setCard(Card value) {
		internalSetCard(value);
		return this;
	}
	/** Internal setter for {@link #getCard()} without chain call utility. */
	protected final void internalSetCard(Card value) {
		_card = value;
	}


	/**
	 * Checks, whether {@link #getCard()} has a value.
	 */
	public final boolean hasCard() {
		return _card != null;
	}

	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return PLAYED_CARD__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static PlayedCard readPlayedCard(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		PlayedCard result = new PlayedCard();
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
		out.name(PLAYER_ID);
		out.value(getPlayerId());
		if (hasCard()) {
			out.name(CARD);
			getCard().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case CARD: setCard(de.haumacher.wizard.msg.Card.readCard(in)); break;
			default: super.readField(in, field);
		}
	}

}
