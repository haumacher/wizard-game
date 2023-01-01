package de.haumacher.wizard.msg.impl;

/**
 * Info about a {@link Card} played.
 */
public class PlayedCard_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.PlayedCard {

	private String _playerId = "";

	private de.haumacher.wizard.msg.Card _card = null;

	/**
	 * Creates a {@link PlayedCard_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.PlayedCard#create()
	 */
	public PlayedCard_Impl() {
		super();
	}

	@Override
	public final String getPlayerId() {
		return _playerId;
	}

	@Override
	public de.haumacher.wizard.msg.PlayedCard setPlayerId(String value) {
		internalSetPlayerId(value);
		return this;
	}

	/** Internal setter for {@link #getPlayerId()} without chain call utility. */
	protected final void internalSetPlayerId(String value) {
		_playerId = value;
	}

	@Override
	public final de.haumacher.wizard.msg.Card getCard() {
		return _card;
	}

	@Override
	public de.haumacher.wizard.msg.PlayedCard setCard(de.haumacher.wizard.msg.Card value) {
		internalSetCard(value);
		return this;
	}

	/** Internal setter for {@link #getCard()} without chain call utility. */
	protected final void internalSetCard(de.haumacher.wizard.msg.Card value) {
		_card = value;
	}

	@Override
	public final boolean hasCard() {
		return _card != null;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(PLAYER_ID__PROP);
		out.value(getPlayerId());
		if (hasCard()) {
			out.name(CARD__PROP);
			getCard().writeTo(out);
		}
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case PLAYER_ID__PROP: setPlayerId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case CARD__PROP: setCard(de.haumacher.wizard.msg.Card.readCard(in)); break;
			default: super.readField(in, field);
		}
	}

}
