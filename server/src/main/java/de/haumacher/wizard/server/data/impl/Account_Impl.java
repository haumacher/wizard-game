package de.haumacher.wizard.server.data.impl;

public class Account_Impl extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.wizard.server.data.Account {

	private String _uid = "";

	private String _nick = "";

	private String _password = "";

	/**
	 * Creates a {@link Account_Impl} instance.
	 *
	 * @see de.haumacher.wizard.server.data.Account#create()
	 */
	public Account_Impl() {
		super();
	}

	@Override
	public final String getUid() {
		return _uid;
	}

	@Override
	public de.haumacher.wizard.server.data.Account setUid(String value) {
		internalSetUid(value);
		return this;
	}

	/** Internal setter for {@link #getUid()} without chain call utility. */
	protected final void internalSetUid(String value) {
		_listener.beforeSet(this, UID__PROP, value);
		_uid = value;
	}

	@Override
	public final String getNick() {
		return _nick;
	}

	@Override
	public de.haumacher.wizard.server.data.Account setNick(String value) {
		internalSetNick(value);
		return this;
	}

	/** Internal setter for {@link #getNick()} without chain call utility. */
	protected final void internalSetNick(String value) {
		_listener.beforeSet(this, NICK__PROP, value);
		_nick = value;
	}

	@Override
	public final String getPassword() {
		return _password;
	}

	@Override
	public de.haumacher.wizard.server.data.Account setPassword(String value) {
		internalSetPassword(value);
		return this;
	}

	/** Internal setter for {@link #getPassword()} without chain call utility. */
	protected final void internalSetPassword(String value) {
		_listener.beforeSet(this, PASSWORD__PROP, value);
		_password = value;
	}

	protected de.haumacher.msgbuf.observer.Listener _listener = de.haumacher.msgbuf.observer.Listener.NONE;

	@Override
	public de.haumacher.wizard.server.data.Account registerListener(de.haumacher.msgbuf.observer.Listener l) {
		internalRegisterListener(l);
		return this;
	}

	protected final void internalRegisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.register(_listener, l);
	}

	@Override
	public de.haumacher.wizard.server.data.Account unregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		internalUnregisterListener(l);
		return this;
	}

	protected final void internalUnregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.unregister(_listener, l);
	}

	@Override
	public String jsonType() {
		return ACCOUNT__TYPE;
	}

	private static java.util.List<String> PROPERTIES = java.util.Collections.unmodifiableList(
		java.util.Arrays.asList(
			UID__PROP, 
			NICK__PROP, 
			PASSWORD__PROP));

	@Override
	public java.util.List<String> properties() {
		return PROPERTIES;
	}

	@Override
	public Object get(String field) {
		switch (field) {
			case UID__PROP: return getUid();
			case NICK__PROP: return getNick();
			case PASSWORD__PROP: return getPassword();
			default: return de.haumacher.wizard.server.data.Account.super.get(field);
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
			case UID__PROP: internalSetUid((String) value); break;
			case NICK__PROP: internalSetNick((String) value); break;
			case PASSWORD__PROP: internalSetPassword((String) value); break;
		}
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		writeContent(out);
	}

	@Override
	protected void writeFields(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		super.writeFields(out);
		out.name(UID__PROP);
		out.value(getUid());
		out.name(NICK__PROP);
		out.value(getNick());
		out.name(PASSWORD__PROP);
		out.value(getPassword());
	}

	@Override
	protected void readField(de.haumacher.msgbuf.json.JsonReader in, String field) throws java.io.IOException {
		switch (field) {
			case UID__PROP: setUid(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case NICK__PROP: setNick(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			case PASSWORD__PROP: setPassword(de.haumacher.msgbuf.json.JsonUtil.nextStringOptional(in)); break;
			default: super.readField(in, field);
		}
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		out.beginObject();
		writeFields(out);
		out.endObject();
	}

	/**
	 * Serializes all fields of this instance to the given binary output.
	 *
	 * @param out
	 *        The binary output to write to.
	 * @throws java.io.IOException If writing fails.
	 */
	protected void writeFields(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		out.name(UID__ID);
		out.value(getUid());
		out.name(NICK__ID);
		out.value(getNick());
		out.name(PASSWORD__ID);
		out.value(getPassword());
	}

	/** Helper for creating an object of type {@link de.haumacher.wizard.server.data.Account} from a polymorphic composition. */
	public static de.haumacher.wizard.server.data.Account readAccount_Content(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		de.haumacher.wizard.server.data.impl.Account_Impl result = new Account_Impl();
		result.readContent(in);
		return result;
	}

	/** Helper for reading all fields of this instance. */
	protected final void readContent(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		while (in.hasNext()) {
			int field = in.nextName();
			readField(in, field);
		}
	}

	/** Consumes the value for the field with the given ID and assigns its value. */
	protected void readField(de.haumacher.msgbuf.binary.DataReader in, int field) throws java.io.IOException {
		switch (field) {
			case UID__ID: setUid(in.nextString()); break;
			case NICK__ID: setNick(in.nextString()); break;
			case PASSWORD__ID: setPassword(in.nextString()); break;
			default: in.skipValue(); 
		}
	}

	/** XML element name representing a {@link de.haumacher.wizard.server.data.Account} type. */
	public static final String ACCOUNT__XML_ELEMENT = "account";

	/** XML attribute or element name of a {@link #getUid} property. */
	private static final String UID__XML_ATTR = "uid";

	/** XML attribute or element name of a {@link #getNick} property. */
	private static final String NICK__XML_ATTR = "nick";

	/** XML attribute or element name of a {@link #getPassword} property. */
	private static final String PASSWORD__XML_ATTR = "password";

	@Override
	public String getXmlTagName() {
		return ACCOUNT__XML_ELEMENT;
	}

	@Override
	public final void writeContent(javax.xml.stream.XMLStreamWriter out) throws javax.xml.stream.XMLStreamException {
		writeAttributes(out);
		writeElements(out);
	}

	/** Serializes all fields that are written as XML attributes. */
	protected void writeAttributes(javax.xml.stream.XMLStreamWriter out) throws javax.xml.stream.XMLStreamException {
		out.writeAttribute(UID__XML_ATTR, getUid());
		out.writeAttribute(NICK__XML_ATTR, getNick());
		out.writeAttribute(PASSWORD__XML_ATTR, getPassword());
	}

	/** Serializes all fields that are written as XML elements. */
	protected void writeElements(javax.xml.stream.XMLStreamWriter out) throws javax.xml.stream.XMLStreamException {
		// No element fields.
	}

	/** Creates a new {@link de.haumacher.wizard.server.data.Account} and reads properties from the content (attributes and inner tags) of the currently open element in the given {@link javax.xml.stream.XMLStreamReader}. */
	public static Account_Impl readAccount_XmlContent(javax.xml.stream.XMLStreamReader in) throws javax.xml.stream.XMLStreamException {
		Account_Impl result = new Account_Impl();
		result.readContentXml(in);
		return result;
	}

	/** Reads properties from the content (attributes and inner tags) of the currently open element in the given {@link javax.xml.stream.XMLStreamReader}. */
	protected final void readContentXml(javax.xml.stream.XMLStreamReader in) throws javax.xml.stream.XMLStreamException {
		for (int n = 0, cnt = in.getAttributeCount(); n < cnt; n++) {
			String name = in.getAttributeLocalName(n);
			String value = in.getAttributeValue(n);

			readFieldXmlAttribute(name, value);
		}
		while (true) {
			int event = in.nextTag();
			if (event == javax.xml.stream.XMLStreamConstants.END_ELEMENT) {
				break;
			}
			assert event == javax.xml.stream.XMLStreamConstants.START_ELEMENT;

			String localName = in.getLocalName();
			readFieldXmlElement(in, localName);
		}
	}

	/** Parses the given attribute value and assigns it to the field with the given name. */
	protected void readFieldXmlAttribute(String name, String value) {
		switch (name) {
			case UID__XML_ATTR: {
				setUid(value);
				break;
			}
			case NICK__XML_ATTR: {
				setNick(value);
				break;
			}
			case PASSWORD__XML_ATTR: {
				setPassword(value);
				break;
			}
			default: {
				// Skip unknown attribute.
			}
		}
	}

	/** Reads the element under the cursor and assigns its contents to the field with the given name. */
	protected void readFieldXmlElement(javax.xml.stream.XMLStreamReader in, String localName) throws javax.xml.stream.XMLStreamException {
		switch (localName) {
			case UID__XML_ATTR: {
				setUid(in.getElementText());
				break;
			}
			case NICK__XML_ATTR: {
				setNick(in.getElementText());
				break;
			}
			case PASSWORD__XML_ATTR: {
				setPassword(in.getElementText());
				break;
			}
			default: {
				internalSkipUntilMatchingEndElement(in);
			}
		}
	}

	protected static final void internalSkipUntilMatchingEndElement(javax.xml.stream.XMLStreamReader in) throws javax.xml.stream.XMLStreamException {
		int level = 0;
		while (true) {
			switch (in.next()) {
				case javax.xml.stream.XMLStreamConstants.START_ELEMENT: level++; break;
				case javax.xml.stream.XMLStreamConstants.END_ELEMENT: if (level == 0) { return; } else { level--; break; }
			}
		}
	}

}
