package de.haumacher.wizard.msg;

/**
 * The suit of a card.
 */
public enum Suit implements de.haumacher.msgbuf.data.ProtocolEnum {

	DIAMOND("diamond"),

	HEART("heart"),

	SPADE("spade"),

	CLUB("club"),

	;

	private final String _protocolName;

	private Suit(String protocolName) {
		_protocolName = protocolName;
	}

	/**
	 * The protocol name of a {@link Suit} constant.
	 *
	 * @see #valueOfProtocol(String)
	 */
	@Override
	public String protocolName() {
		return _protocolName;
	}

	/** Looks up a {@link Suit} constant by it's protocol name. */
	public static Suit valueOfProtocol(String protocolName) {
		if (protocolName == null) { return null; }
		switch (protocolName) {
			case "diamond": return DIAMOND;
			case "heart": return HEART;
			case "spade": return SPADE;
			case "club": return CLUB;
		}
		return DIAMOND;
	}

	/** Writes this instance to the given output. */
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		out.value(protocolName());
	}

	/** Reads a new instance from the given reader. */
	public static Suit readSuit(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		return valueOfProtocol(in.nextString());
	}

	/** Writes this instance to the given binary output. */
	public final void writeTo(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		switch (this) {
			case DIAMOND: out.value(1); break;
			case HEART: out.value(2); break;
			case SPADE: out.value(3); break;
			case CLUB: out.value(4); break;
			default: out.value(0);
		}
	}

	/** Reads a new instance from the given binary reader. */
	public static Suit readSuit(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		switch (in.nextInt()) {
			case 1: return DIAMOND;
			case 2: return HEART;
			case 3: return SPADE;
			case 4: return CLUB;
			default: return DIAMOND;
		}
	}
}
