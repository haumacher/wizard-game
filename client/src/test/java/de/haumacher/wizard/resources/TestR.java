/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.resources;

/**
 * Test fixture for {@link TestResources}.
 */
public class TestR extends StaticResources {

	public static String msg;
	public static R1 msg1;
	public static R2 msg2;
	public static R3 msg3;
	public static R4 msg4;
	public static R5 msg5;
	public static RX msgX;
	
	static {
		load(TestR.class);
	}
}
