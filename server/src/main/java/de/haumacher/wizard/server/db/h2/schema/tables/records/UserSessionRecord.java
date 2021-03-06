/*
 * This file is generated by jOOQ.
 */
package de.haumacher.wizard.server.db.h2.schema.tables.records;


import de.haumacher.wizard.server.db.h2.schema.tables.UserSession;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserSessionRecord extends UpdatableRecordImpl<UserSessionRecord> implements Record2<String, byte[]> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>PUBLIC.USER_SESSION.UID</code>.
     */
    public void setUid(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.USER_SESSION.UID</code>.
     */
    public String getUid() {
        return (String) get(0);
    }

    /**
     * Setter for <code>PUBLIC.USER_SESSION.HASH</code>.
     */
    public void setHash(byte[] value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.USER_SESSION.HASH</code>.
     */
    public byte[] getHash() {
        return (byte[]) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, byte[]> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, byte[]> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return UserSession.USER_SESSION.UID;
    }

    @Override
    public Field<byte[]> field2() {
        return UserSession.USER_SESSION.HASH;
    }

    @Override
    public String component1() {
        return getUid();
    }

    @Override
    public byte[] component2() {
        return getHash();
    }

    @Override
    public String value1() {
        return getUid();
    }

    @Override
    public byte[] value2() {
        return getHash();
    }

    @Override
    public UserSessionRecord value1(String value) {
        setUid(value);
        return this;
    }

    @Override
    public UserSessionRecord value2(byte[] value) {
        setHash(value);
        return this;
    }

    @Override
    public UserSessionRecord values(String value1, byte[] value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserSessionRecord
     */
    public UserSessionRecord() {
        super(UserSession.USER_SESSION);
    }

    /**
     * Create a detached, initialised UserSessionRecord
     */
    public UserSessionRecord(String uid, byte[] hash) {
        super(UserSession.USER_SESSION);

        setUid(uid);
        setHash(hash);
    }
}
