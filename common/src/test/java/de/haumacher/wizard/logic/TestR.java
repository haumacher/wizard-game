/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import junit.framework.TestCase;

/**
 * Test for resources {@link R}.
 */
public class TestR extends TestCase {

	public void testLoad() {
		assertNotNull(R.connectionAccepted.format());
	}
	
}
