package de.haumacher.wizard.msg;

/**
 * Possible card values.
 */
public enum Value implements de.haumacher.msgbuf.data.ProtocolEnum {

	/**
	 * A jester.
	 */
	N("N"),

	/**
	 * The card with value 1.
	 */
	C_1("C1"),

	/**
	 * The card with value 2.
	 */
	C_2("C2"),

	/**
	 * The card with value 3.
	 */
	C_3("C3"),

	/**
	 * The card with value 4.
	 */
	C_4("C4"),

	/**
	 * The card with value 5.
	 */
	C_5("C5"),

	/**
	 * The card with value 6.
	 */
	C_6("C6"),

	/**
	 * The card with value 7.
	 */
	C_7("C7"),

	/**
	 * The card with value 8.
	 */
	C_8("C8"),

	/**
	 * The card with value 9.
	 */
	C_9("C9"),

	/**
	 * The card with value 10.
	 */
	C_10("C10"),

	/**
	 * The card with value 11.
	 */
	C_11("C11"),

	/**
	 * The card with value 12.
	 */
	C_12("C12"),

	/**
	 * The card with value 13.
	 */
	C_13("C13"),

	/**
	 * A wizard.
	 */
	Z("Z"),

	;

	private final String _protocolName;

	private Value(String protocolName) {
		_protocolName = protocolName;
	}

	/**
	 * The protocol name of a {@link Value} constant.
	 *
	 * @see #valueOfProtocol(String)
	 */
	@Override
	public String protocolName() {
		return _protocolName;
	}

	/** Looks up a {@link Value} constant by it's protocol name. */
	public static Value valueOfProtocol(String protocolName) {
		if (protocolName == null) { return null; }
		switch (protocolName) {
			case "N": return N;
			case "C1": return C_1;
			case "C2": return C_2;
			case "C3": return C_3;
			case "C4": return C_4;
			case "C5": return C_5;
			case "C6": return C_6;
			case "C7": return C_7;
			case "C8": return C_8;
			case "C9": return C_9;
			case "C10": return C_10;
			case "C11": return C_11;
			case "C12": return C_12;
			case "C13": return C_13;
			case "Z": return Z;
		}
		return N;
	}

	/** Writes this instance to the given output. */
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		out.value(protocolName());
	}

	/** Reads a new instance from the given reader. */
	public static Value readValue(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		return valueOfProtocol(in.nextString());
	}

	/** Writes this instance to the given binary output. */
	public final void writeTo(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		switch (this) {
			case N: out.value(1); break;
			case C_1: out.value(2); break;
			case C_2: out.value(3); break;
			case C_3: out.value(4); break;
			case C_4: out.value(5); break;
			case C_5: out.value(6); break;
			case C_6: out.value(7); break;
			case C_7: out.value(8); break;
			case C_8: out.value(9); break;
			case C_9: out.value(10); break;
			case C_10: out.value(11); break;
			case C_11: out.value(12); break;
			case C_12: out.value(13); break;
			case C_13: out.value(14); break;
			case Z: out.value(15); break;
			default: out.value(0);
		}
	}

	/** Reads a new instance from the given binary reader. */
	public static Value readValue(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		switch (in.nextInt()) {
			case 1: return N;
			case 2: return C_1;
			case 3: return C_2;
			case 4: return C_3;
			case 5: return C_4;
			case 6: return C_5;
			case 7: return C_6;
			case 8: return C_7;
			case 9: return C_8;
			case 10: return C_9;
			case 11: return C_10;
			case 12: return C_11;
			case 13: return C_12;
			case 14: return C_13;
			case 15: return Z;
			default: return N;
		}
	}
}
