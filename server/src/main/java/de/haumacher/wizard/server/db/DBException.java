/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.db;

/**
 * TODO
 */
public class DBException extends Exception {

	/** 
	 * Creates a {@link DBException}.
	 */
	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 
	 * Creates a {@link DBException}.
	 */
	public DBException(String message) {
		super(message);
	}
}
