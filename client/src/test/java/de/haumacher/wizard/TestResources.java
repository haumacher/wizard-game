/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import junit.framework.TestCase;

/**
 * Test for {@link R}.
 */
public class TestResources extends TestCase {

	public void testResource() {
		assertEquals("Runde 3 von 4", R.round_round_maxRound.format(3, 4));
	}
	
}
