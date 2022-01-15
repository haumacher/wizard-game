package de.haumacher.wizard.msg;

/**
 * The color of a card.
 */
public enum Color implements de.haumacher.msgbuf.data.ProtocolEnum {

	YELLOW("yellow"),

	RED("red"),

	GREEN("green"),

	BLUE("blue"),

	;

	private final String _protocolName;

	private Color(String protocolName) {
		_protocolName = protocolName;
	}

	/**
	 * The protocol name of a {@link Color} constant.
	 *
	 * @see #valueOfProtocol(String)
	 */
	@Override
	public String protocolName() {
		return _protocolName;
	}

	/** Looks up a {@link Color} constant by it's protocol name. */
	public static Color valueOfProtocol(String protocolName) {
		if (protocolName == null) { return null; }
		switch (protocolName) {
			case "yellow": return YELLOW;
			case "red": return RED;
			case "green": return GREEN;
			case "blue": return BLUE;
		}
		return YELLOW;
	}

	/** Writes this instance to the given output. */
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		out.value(protocolName());
	}

	/** Reads a new instance from the given reader. */
	public static Color readColor(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		return valueOfProtocol(in.nextString());
	}

	/** Writes this instance to the given binary output. */
	public final void writeTo(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		switch (this) {
			case YELLOW: out.value(1); break;
			case RED: out.value(2); break;
			case GREEN: out.value(3); break;
			case BLUE: out.value(4); break;
			default: out.value(0);
		}
	}

	/** Reads a new instance from the given binary reader. */
	public static Color readColor(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		switch (in.nextInt()) {
			case 1: return YELLOW;
			case 2: return RED;
			case 3: return GREEN;
			case 4: return BLUE;
			default: return YELLOW;
		}
	}
}
