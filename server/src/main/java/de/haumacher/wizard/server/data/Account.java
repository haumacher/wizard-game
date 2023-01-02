package de.haumacher.wizard.server.data;

public interface Account extends de.haumacher.msgbuf.data.DataObject, de.haumacher.msgbuf.binary.BinaryDataObject, de.haumacher.msgbuf.observer.Observable, de.haumacher.msgbuf.xml.XmlSerializable {

	/**
	 * Creates a {@link de.haumacher.wizard.server.data.Account} instance.
	 */
	static de.haumacher.wizard.server.data.Account create() {
		return new de.haumacher.wizard.server.data.impl.Account_Impl();
	}

	/** Identifier for the {@link de.haumacher.wizard.server.data.Account} type in JSON format. */
	String ACCOUNT__TYPE = "Account";

	/** @see #getUid() */
	String UID__PROP = "uid";

	/** @see #getNick() */
	String NICK__PROP = "nick";

	/** @see #getPassword() */
	String PASSWORD__PROP = "password";

	/** Identifier for the property {@link #getUid()} in binary format. */
	static final int UID__ID = 1;

	/** Identifier for the property {@link #getNick()} in binary format. */
	static final int NICK__ID = 2;

	/** Identifier for the property {@link #getPassword()} in binary format. */
	static final int PASSWORD__ID = 3;

	String getUid();

	/**
	 * @see #getUid()
	 */
	de.haumacher.wizard.server.data.Account setUid(String value);

	String getNick();

	/**
	 * @see #getNick()
	 */
	de.haumacher.wizard.server.data.Account setNick(String value);

	String getPassword();

	/**
	 * @see #getPassword()
	 */
	de.haumacher.wizard.server.data.Account setPassword(String value);

	@Override
	public de.haumacher.wizard.server.data.Account registerListener(de.haumacher.msgbuf.observer.Listener l);

	@Override
	public de.haumacher.wizard.server.data.Account unregisterListener(de.haumacher.msgbuf.observer.Listener l);

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.server.data.Account readAccount(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		de.haumacher.wizard.server.data.impl.Account_Impl result = new de.haumacher.wizard.server.data.impl.Account_Impl();
		result.readContent(in);
		return result;
	}

	/** Reads a new instance from the given reader. */
	static de.haumacher.wizard.server.data.Account readAccount(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		de.haumacher.wizard.server.data.Account result = de.haumacher.wizard.server.data.impl.Account_Impl.readAccount_Content(in);
		in.endObject();
		return result;
	}

	/** Creates a new {@link Account} and reads properties from the content (attributes and inner tags) of the currently open element in the given {@link javax.xml.stream.XMLStreamReader}. */
	public static Account readAccount(javax.xml.stream.XMLStreamReader in) throws javax.xml.stream.XMLStreamException {
		in.nextTag();
		return de.haumacher.wizard.server.data.impl.Account_Impl.readAccount_XmlContent(in);
	}

}
