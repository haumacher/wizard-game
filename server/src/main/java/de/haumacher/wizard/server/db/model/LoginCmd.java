package de.haumacher.wizard.server.db.model;

/**
 * Base class for commands for account management.
 */
public abstract class LoginCmd extends de.haumacher.msgbuf.data.AbstractDataObject implements de.haumacher.msgbuf.binary.BinaryDataObject, de.haumacher.msgbuf.observer.Observable {

	/** Type codes for the {@link LoginCmd} hierarchy. */
	public enum TypeKind {

		/** Type literal for {@link CreateAccount}. */
		CREATE_ACCOUNT,

		/** Type literal for {@link AddEmail}. */
		ADD_EMAIL,

		/** Type literal for {@link VerifyEmail}. */
		VERIFY_EMAIL,
		;

	}

	/** Visitor interface for the {@link LoginCmd} hierarchy.*/
	public interface Visitor<R,A,E extends Throwable> {

		/** Visit case for {@link CreateAccount}.*/
		R visit(CreateAccount self, A arg) throws E;

		/** Visit case for {@link AddEmail}.*/
		R visit(AddEmail self, A arg) throws E;

		/** Visit case for {@link VerifyEmail}.*/
		R visit(VerifyEmail self, A arg) throws E;

	}

	/**
	 * Creates a {@link LoginCmd} instance.
	 */
	protected LoginCmd() {
		super();
	}

	/** The type code of this instance. */
	public abstract TypeKind kind();

	protected de.haumacher.msgbuf.observer.Listener _listener = de.haumacher.msgbuf.observer.Listener.NONE;

	@Override
	public LoginCmd registerListener(de.haumacher.msgbuf.observer.Listener l) {
		internalRegisterListener(l);
		return this;
	}

	protected final void internalRegisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.register(_listener, l);
	}

	@Override
	public LoginCmd unregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		internalUnregisterListener(l);
		return this;
	}

	protected final void internalUnregisterListener(de.haumacher.msgbuf.observer.Listener l) {
		_listener = de.haumacher.msgbuf.observer.Listener.unregister(_listener, l);
	}

	/** Reads a new instance from the given reader. */
	public static LoginCmd readLoginCmd(de.haumacher.msgbuf.json.JsonReader in) throws java.io.IOException {
		LoginCmd result;
		in.beginArray();
		String type = in.nextString();
		switch (type) {
			case CreateAccount.CREATE_ACCOUNT__TYPE: result = de.haumacher.wizard.server.db.model.CreateAccount.readCreateAccount(in); break;
			case AddEmail.ADD_EMAIL__TYPE: result = de.haumacher.wizard.server.db.model.AddEmail.readAddEmail(in); break;
			case VerifyEmail.VERIFY_EMAIL__TYPE: result = de.haumacher.wizard.server.db.model.VerifyEmail.readVerifyEmail(in); break;
			default: in.skipValue(); result = null; break;
		}
		in.endArray();
		return result;
	}

	@Override
	public final void writeTo(de.haumacher.msgbuf.json.JsonWriter out) throws java.io.IOException {
		out.beginArray();
		out.value(jsonType());
		writeContent(out);
		out.endArray();
	}

	/** The binary identifier for this concrete type in the polymorphic {@link LoginCmd} hierarchy. */
	public abstract int typeId();

	@Override
	public final void writeTo(de.haumacher.msgbuf.binary.DataWriter out) throws java.io.IOException {
		out.beginObject();
		out.name(0);
		out.value(typeId());
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
		// No fields to write, hook for subclasses.
	}

	/** Reads a new instance from the given reader. */
	public static LoginCmd readLoginCmd(de.haumacher.msgbuf.binary.DataReader in) throws java.io.IOException {
		in.beginObject();
		LoginCmd result;
		int typeField = in.nextName();
		assert typeField == 0;
		int type = in.nextInt();
		switch (type) {
			case CreateAccount.CREATE_ACCOUNT__TYPE_ID: result = de.haumacher.wizard.server.db.model.CreateAccount.create(); break;
			case AddEmail.ADD_EMAIL__TYPE_ID: result = de.haumacher.wizard.server.db.model.AddEmail.create(); break;
			case VerifyEmail.VERIFY_EMAIL__TYPE_ID: result = de.haumacher.wizard.server.db.model.VerifyEmail.create(); break;
			default: while (in.hasNext()) {in.skipValue(); } in.endObject(); return null;
		}
		while (in.hasNext()) {
			int field = in.nextName();
			result.readField(in, field);
		}
		in.endObject();
		return result;
	}

	/** Consumes the value for the field with the given ID and assigns its value. */
	protected void readField(de.haumacher.msgbuf.binary.DataReader in, int field) throws java.io.IOException {
		switch (field) {
			default: in.skipValue(); 
		}
	}

	/** Accepts the given visitor. */
	public abstract <R,A,E extends Throwable> R visit(Visitor<R,A,E> v, A arg) throws E;


}
