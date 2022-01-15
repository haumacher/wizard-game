package de.haumacher.wizard.msg;

public class Player extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link Player} instance.
	 */
	public static Player create() {
		return new Player();
	}

	/** Identifier for the {@link Player} type in JSON format. */
	public static final String PLAYER__TYPE = "Player";

	/** @see #getId() */
	private static final String ID = "id";

	/** @see #getName() */
	private static final String NAME = "name";

	private String _id = "";

	private String _name = "";

	/**
	 * Creates a {@link Player} instance.
	 *
	 * @see #create()
	 */
	protected Player() {
		super();
	}

	public final String getId() {
		return _id;
	}

	/**
	 * @see #getId()
	 */
	public Player setId(String value) {
		internalSetId(value);
		return this;
	}
	/** Internal setter for {@link #getId()} without chain call utility. */
	protected final void internalSetId(String value) {
		_id = value;
	}


	public final String getName() {
		return _name;
	}

	/**
	 * @see #getName()
	 */
	public Player setName(String value) {
		internalSetName(value);
		return this;
	}
	/** Internal setter for {@link #getName()} without chain call utility. */
	protected final void internalSetName(String value) {
		_name = value;
	}


	/** The type identifier for this concrete subtype. */
	public String jsonType() {
		return PLAYER__TYPE;
	}

	/** Reads a new instance from the given reader. */
	public static Player readPlayer(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		Player result = new Player();
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
		out.name(ID);
		out.value(getId());
		out.name(NAME);
		out.value(getName());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case ID: setId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case NAME: setName(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

}
