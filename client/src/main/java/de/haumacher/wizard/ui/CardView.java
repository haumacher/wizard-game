/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.controller.Controller;
import de.haumacher.wizard.controller.GenericController;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.Value;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class CardView extends GenericController {
	
	@FXML
	Text valueView;
	
	private Card _card;

	public void setValue(String value) {
		valueView.setText(value);
	}

	/** 
	 * TODO
	 *
	 * @param card
	 */
	public void setCard(Card card) {
		_card = card;
		setValue(text(card.getValue()));
	}
	
	/**
	 * TODO
	 */
	public Card getCard() {
		return _card;
	}
	
	public static Node createCard(Card card) {
		CardView controller = loadCardView(card);
		controller.setCard(card);
		return controller.getView();
	}
	
	private String text(Value value) {
		switch (value) {
			case N: 
			case Z:
				return value.name();
			default:
				return value.name().substring(2);
		}
	}

	private static CardView loadCardView(Card card) {
		// Note: For selecting the trump suit, even a wizard with a suit must be displayed.
		if (card.getSuit() != null) {
			switch (card.getSuit()) {
			case HEART:
				return Controller.load(CardView.class, "card-heart.fxml");
			case SPADE:
				return Controller.load(CardView.class, "card-pik.fxml");
			case CLUB:
				return Controller.load(CardView.class, "card-cross.fxml");
			case DIAMOND:
				return Controller.load(CardView.class, "card-caro.fxml");
			}
		}
		switch (card.getValue()) {
			case N:
				return Controller.load(CardView.class, "card-none.fxml");
			case Z:
				return Controller.load(CardView.class, "card-wizard.fxml");
			default:
		}
		
		throw new IllegalArgumentException("Unsupported card: " + card);
	}
}
