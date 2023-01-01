package de.haumacher.wizard.msg.impl;

/**
 * A player of a {@link Game}.
 */
public class Player_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.Player {

	private String _id = "";

	private String _name = "";

	/**
	 * Creates a {@link Player_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.Player#create()
	 */
	public Player_Impl() {
		super();
	}

	@Override
	public final String getId() {
		return _id;
	}

	@Override
	public de.haumacher.wizard.msg.Player setId(String value) {
		internalSetId(value);
		return this;
	}

	/** Internal setter for {@link #getId()} without chain call utility. */
	protected final void internalSetId(String value) {
		_id = value;
	}

	@Override
	public final String getName() {
		return _name;
	}

	@Override
	public de.haumacher.wizard.msg.Player setName(String value) {
		internalSetName(value);
		return this;
	}

	/** Internal setter for {@link #getName()} without chain call utility. */
	protected final void internalSetName(String value) {
		_name = value;
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
