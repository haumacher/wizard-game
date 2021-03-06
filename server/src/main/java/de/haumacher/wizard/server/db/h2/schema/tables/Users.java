/*
 * This file is generated by jOOQ.
 */
package de.haumacher.wizard.server.db.h2.schema.tables;


import de.haumacher.wizard.server.db.h2.schema.Indexes;
import de.haumacher.wizard.server.db.h2.schema.Keys;
import de.haumacher.wizard.server.db.h2.schema.Public;
import de.haumacher.wizard.server.db.h2.schema.tables.records.UsersRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Users extends TableImpl<UsersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>PUBLIC.USERS</code>
     */
    public static final Users USERS = new Users();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UsersRecord> getRecordType() {
        return UsersRecord.class;
    }

    /**
     * The column <code>PUBLIC.USERS.UID</code>.
     */
    public final TableField<UsersRecord, String> UID = createField(DSL.name("UID"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.USERS.NICKNAME</code>.
     */
    public final TableField<UsersRecord, String> NICKNAME = createField(DSL.name("NICKNAME"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.USERS.EMAIL</code>.
     */
    public final TableField<UsersRecord, byte[]> EMAIL = createField(DSL.name("EMAIL"), SQLDataType.VARBINARY(128).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.USERS.LANGUAGE</code>.
     */
    public final TableField<UsersRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(5).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.USERS.CREATED</code>.
     */
    public final TableField<UsersRecord, Long> CREATED = createField(DSL.name("CREATED"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.USERS.VERIFIED</code>.
     */
    public final TableField<UsersRecord, Boolean> VERIFIED = createField(DSL.name("VERIFIED"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.USERS.LAST_LOGIN</code>.
     */
    public final TableField<UsersRecord, Long> LAST_LOGIN = createField(DSL.name("LAST_LOGIN"), SQLDataType.BIGINT.nullable(false), this, "");

    private Users(Name alias, Table<UsersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Users(Name alias, Table<UsersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>PUBLIC.USERS</code> table reference
     */
    public Users(String alias) {
        this(DSL.name(alias), USERS);
    }

    /**
     * Create an aliased <code>PUBLIC.USERS</code> table reference
     */
    public Users(Name alias) {
        this(alias, USERS);
    }

    /**
     * Create a <code>PUBLIC.USERS</code> table reference
     */
    public Users() {
        this(DSL.name("USERS"), null);
    }

    public <O extends Record> Users(Table<O> child, ForeignKey<O, UsersRecord> key) {
        super(child, key, USERS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.USERS_EMAIL_INDEX, Indexes.USERS_NICKNAME_INDEX);
    }

    @Override
    public UniqueKey<UsersRecord> getPrimaryKey() {
        return Keys.USERS_PK;
    }

    @Override
    public Users as(String alias) {
        return new Users(DSL.name(alias), this);
    }

    @Override
    public Users as(Name alias) {
        return new Users(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Users rename(String name) {
        return new Users(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Users rename(Name name) {
        return new Users(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, String, byte[], String, Long, Boolean, Long> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
