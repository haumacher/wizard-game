package de.haumacher.wizard.msg;

/**
 * Starts a round by assigning cards to players.
 */
public interface StartRound extends GameMsg {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.StartRound} instance.
	 */
	static de.haumacher.wizard.msg.StartRound create() {
		return new de.haumacher.wizard.msg.impl.StartRound_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.StartRound} type in JSON format. */
	String START_ROUND__TYPE = "StartRound";

	/** @see #getRound() */
	String ROUND__PROP = "round";

	/** @see #getMaxRound() */
	String MAX_ROUND__PROP = "maxRound";

	/** @see #getStartPlayer() */
	String START_PLAYER__PROP = "startPlayer";

	/** @see #getPlayers() */
	String PLAYERS__PROP = "players";

	/** @see #getCards() */
	String CARDS__PROP = "cards";

	/** @see #getTrumpCard() */
	String TRUMP_CARD__PROP = "trumpCard";

	/**
	 * The number of the round. 1 is the first round.
	 */
	int getRound();

	/**
	 * @see #getRound()
	 */
	de.haumacher.wizard.msg.StartRound setRound(int value);

	/**
	 * The number of rounds being played in this game (depends on the number of players).
	 */
	int getMaxRound();

	/**
	 * @see #getMaxRound()
	 */
	de.haumacher.wizard.msg.StartRound setMaxRound(int value);

	/**
	 * The ID of the player that must make the first bid and leads the first card.
	 */
	String getStartPlayer();

	/**
	 * @see #getStartPlayer()
	 */
	de.haumacher.wizard.msg.StartRound setStartPlayer(String value);

	/**
	 * The players and their order in this round.
	 */
	java.util.List<de.haumacher.wizard.msg.Player> getPlayers();

	/**
	 * @see #getPlayers()
	 */
	de.haumacher.wizard.msg.StartRound setPlayers(java.util.List<? extends de.haumacher.wizard.msg.Player> value);

	/**
	 * Adds a value to the {@link #getPlayers()} list.
	 */
	de.haumacher.wizard.msg.StartRound addPlayer(de.haumacher.wizard.msg.Player value);

	/**
	 * Removes a value from the {@link #getPlayers()} list.
	 */
	void removePlayer(de.haumacher.wizard.msg.Player value);

	/**
	 * The cards given to the player that receives this message.
	 */
	java.util.List<de.haumacher.wizard.msg.Card> getCards();

	/**
	 * @see #getCards()
	 */
	de.haumacher.wizard.msg.StartRound setCards(java.util.List<? extends de.haumacher.wizard.msg.Card> value);

	/**
	 * Adds a value to the {@link #getCards()} list.
	 */
	de.haumacher.wizard.msg.StartRound addCard(de.haumacher.wizard.msg.Card value);

	/**
	 * Removes a value from the {@link #getCards()} list.
	 */
	void removeCard(de.haumacher.wizard.msg.Card value);

	/**
	 * The given trump card. In the last round, there is no trump card.
	 */
	de.haumacher.wizard.msg.Card getTrumpCard();

	/**
	 * @see #getTrumpCard()
	 */
	de.haumacher.wizard.msg.StartRound setTrumpCard(de.haumacher.wizard.msg.Card value);

	/**
	 * Checks, whether {@link #getTrumpCard()} has a value.
	 */
	boolean hasTrumpCard();

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.StartRound readStartRound(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.StartRound_Impl result = new de.haumacher.wizard.msg.impl.StartRound_Impl();
		result.readContent(in);
		return result;
	}

}
