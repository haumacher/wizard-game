/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package test.de.haumacher.wizard.resources;

import de.haumacher.wizard.resources.StaticResources;

/**
 * Test fixture for {@link TestResources}.
 */
public class TestR extends StaticResources {

	public static String msg;
	public static M1 msg1;
	public static M2 msg2;
	public static M3 msg3;
	public static M4 msg4;
	public static M5 msg5;
	public static MX msgX;
	
	static {
		load(TestR.class);
	}
}
