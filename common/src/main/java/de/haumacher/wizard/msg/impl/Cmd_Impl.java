package de.haumacher.wizard.msg.impl;

/**
 * A message that is sent from a client to the game server to trigger some action.
 */
public abstract class Cmd_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.Cmd {

	/**
	 * Creates a {@link Cmd_Impl} instance.
	 */
	public Cmd_Impl() {
		super();
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		out.beginArray();
		out.value(jsonType());
		writeContent(out);
		out.endArray();
	}

}
