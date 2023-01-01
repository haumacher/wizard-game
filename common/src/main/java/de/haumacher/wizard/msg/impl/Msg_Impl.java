package de.haumacher.wizard.msg.impl;

/**
 * A message sent from the game server to active clients.
 */
public abstract class Msg_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.msg.Msg {

	/**
	 * Creates a {@link Msg_Impl} instance.
	 */
	public Msg_Impl() {
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
