package de.haumacher.wizard.msg;

/**
 * A player of a {@link Game}.
 */
public class Player extends de.haumacher.msgbuf.data.AbstractDataObject {

	/**
	 * Creates a {@link Player} instance.
	 */
	public static Player create() {
		return new de.haumacher.wizard.msg.Player();
	}

	/** Identifier for the {@link Player} type in JSON format. */
	public static final String PLAYER__TYPE = "Player";

	/** @see #getId() */
	private static final String ID__PROP = "id";

	/** @see #getName() */
	private static final String NAME__PROP = "name";

	private String _id = "";

	private String _name = "";

	/**
	 * Creates a {@link Player} instance.
	 *
	 * @see Player#create()
	 */
	protected Player() {
		super();
	}

	/**
	 * A technical ID of the player used to reference this player in messages.
	 */
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

	/**
	 * A nick name for the player to display to other users.
	 */
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

	/** Reads a new instance from the given reader. */
	public static Player readPlayer(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.Player result = new de.haumacher.wizard.msg.Player();
		result.readContent(in);
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(ID__PROP);
		out.value(getId());
		out.name(NAME__PROP);
		out.value(getName());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case ID__PROP: setId(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case NAME__PROP: setName(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

}
