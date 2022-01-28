/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Ri"ghts Reserved.
 */
package test.de.haumacher.wizard.resources;

import de.haumacher.wizard.resources.StaticResources;
import junit.framework.TestCase;

/**
 * Test for {@link StaticResources}.
 */
public class TestResources extends TestCase {

	public void testResource() {
		assertEquals("Message", TestR.msg);
		assertEquals("Message()", TestR.msg0.format());
		assertEquals("Message(A)", TestR.msg1.format("A"));
		assertEquals("Message(A, B)", TestR.msg2.format("A", "B"));
		assertEquals("Message(A, B, C)", TestR.msg3.format("A", "B", "C"));
		assertEquals("Message(A, B, C, D)", TestR.msg4.format("A", "B", "C", "D"));
		assertEquals("Message(A, B, C, D, E)", TestR.msg5.format("A", "B", "C", "D", "E"));
		assertEquals("Message(A, B, C, D, E, F)", TestR.msgX.format("A", "B", "C", "D", "E", "F"));

		assertEquals("msg0", TestR.msg0.key());
		assertEquals("msg1", TestR.msg1.key());
		assertEquals("msg2", TestR.msg2.key());
		assertEquals("msg3", TestR.msg3.key());
		assertEquals("msg4", TestR.msg4.key());
		assertEquals("msg5", TestR.msg5.key());
		assertEquals("msgX", TestR.msgX.key());
		
		assertEquals("Message()", TestR.msg0.pattern());
		assertEquals("Message({0})", TestR.msg1.pattern());
		assertEquals("Message({0}, {1})", TestR.msg2.pattern());
		assertEquals("Message({0}, {1}, {2})", TestR.msg3.pattern());
		assertEquals("Message({0}, {1}, {2}, {3})", TestR.msg4.pattern());
		assertEquals("Message({0}, {1}, {2}, {3}, {4})", TestR.msg5.pattern());
		assertEquals("Message({0}, {1}, {2}, {3}, {4}, {5})", TestR.msgX.pattern());
	}
	
}
