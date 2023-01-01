package de.haumacher.wizard.msg;

/**
 * A player of a {@link Game}.
 */
public interface Player extends de.haumacher.msgbuf.data.DataObject {

	/**
	 * Creates a {@link de.haumacher.wizard.msg.Player} instance.
	 */
	static de.haumacher.wizard.msg.Player create() {
		return new de.haumacher.wizard.msg.impl.Player_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.msg.Player} type in JSON format. */
	String PLAYER__TYPE = "Player";

	/** @see #getId() */
	String ID__PROP = "id";

	/** @see #getName() */
	String NAME__PROP = "name";

	/**
	 * A technical ID of the player used to reference this player in messages.
	 */
	String getId();

	/**
	 * @see #getId()
	 */
	de.haumacher.wizard.msg.Player setId(String value);

	/**
	 * A nick name for the player to display to other users.
	 */
	String getName();

	/**
	 * @see #getName()
	 */
	de.haumacher.wizard.msg.Player setName(String value);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.msg.Player readPlayer(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.msg.impl.Player_Impl result = new de.haumacher.wizard.msg.impl.Player_Impl();
		result.readContent(in);
		return result;
	}

}
