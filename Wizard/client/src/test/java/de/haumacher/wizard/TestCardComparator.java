/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.haumacher.wizard.logic.CardComparator;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.Color;
import de.haumacher.wizard.msg.Value;
import junit.framework.TestCase;

/**
 * Test case for {@link CardComparator}.
 */
public class TestCardComparator extends TestCase {
	
	public void testCompare() {
		List<Card> cards = Arrays.asList(
			Card.create().setValue(Value.Z),
			Card.create().setValue(Value.C_12).setColor(Color.RED),
			Card.create().setValue(Value.Z),
			Card.create().setValue(Value.C_13).setColor(Color.YELLOW));
			
		Collections.sort(cards, CardComparator.INSTANCE);
		
		assertEquals(Arrays.asList(Value.Z, Value.Z, Value.C_12, Value.C_13), cards.stream().map(c -> c.getValue()).collect(Collectors.toList()));
	}

}
