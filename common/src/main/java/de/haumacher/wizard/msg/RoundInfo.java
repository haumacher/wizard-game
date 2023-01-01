package de.haumacher.wizard.msg;

/**
 * Current player status at the end of a round.
 */
public interface RoundInfo extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.RoundInfo} instance.
	 */
	static de.haumacher.wizard.msg.RoundInfo create() {
		return new de.haumacher.wizard.msg.impl.RoundInfo_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.RoundInfo} type in JSON format. */
	String ROUND_INFO__TYPE = "RoundInfo";

	/** @see #getPoints() */
	String POINTS__PROP = "points";

	/** @see #getTotal() */
	String TOTAL__PROP = "total";

	/**
	 * The points won by the player during the last round. The number may be negative.
	 */
	int getPoints();

	/**
	 * @see #getPoints()
	 */
	de.haumacher.wizard.msg.RoundInfo setPoints(int value);

	/**
	 * The total amount of points the player won so far. The number may be negative.
	 */
	int getTotal();

	/**
	 * @see #getTotal()
	 */
	de.haumacher.wizard.msg.RoundInfo setTotal(int value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.RoundInfo readRoundInfo(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.RoundInfo_Impl result = new de.haumacher.wizard.msg.impl.RoundInfo_Impl();
		result.readContent(in);
		return result;
	}

}
