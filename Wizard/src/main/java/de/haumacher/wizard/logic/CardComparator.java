/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import java.util.Comparator;

import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.Value;

/**
 * {@link Comparator} of {@link Card}s
 */
public class CardComparator implements Comparator<Card> {
	
	/**
	 * Singleton {@link CardComparator} instance.
	 */
	public static final CardComparator INSTANCE = new CardComparator();

	private CardComparator() {
		// Singleton constructor.
	}

	@Override
	public int compare(Card c1, Card c2) {
		return c1.getValue() == c2.getValue() && c1.getColor() == c2.getColor() ? 0 : 
			c1.getValue() == Value.Z ? -1 :
				c2.getValue() == Value.Z ? 1 :
					c1.getValue() == Value.N ? 1 :
						c2.getValue() == Value.N ? -1 :
							c1.getColor() != c2.getColor() ? 
								-Integer.compare(c1.getColor().ordinal(), c2.getColor().ordinal()) :
								-Integer.compare(c1.getValue().ordinal(), c2.getValue().ordinal());
		}

}
