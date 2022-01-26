/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Ri"ghts Reserved.
 */
package de.haumacher.wizard.resources;

import de.haumacher.wizard.R;
import junit.framework.TestCase;

/**
 * Test for {@link R}.
 */
public class TestResources extends TestCase {

	public void testResource() {
		assertEquals("Message", TestR.msg);
		assertEquals("Message(A)", TestR.msg1.format("A"));
		assertEquals("Message(A, B)", TestR.msg2.format("A", "B"));
		assertEquals("Message(A, B, C)", TestR.msg3.format("A", "B", "C"));
		assertEquals("Message(A, B, C, D)", TestR.msg4.format("A", "B", "C", "D"));
		assertEquals("Message(A, B, C, D, E)", TestR.msg5.format("A", "B", "C", "D", "E"));
		assertEquals("Message(A, B, C, D, E, F)", TestR.msgX.format("A", "B", "C", "D", "E", "F"));
	}
	
}
