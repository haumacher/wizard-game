/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.WizardApp;
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
public class CardView extends Controller {
	
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
		Node view = loadCardView(card);
		CardView controller = (CardView) view.getUserData();
		controller.setCard(card);
		return view;
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

	private static Node loadCardView(Card card) {
		// Note: For selecting the trump color, even a wizard with a color must be displayed.
		if (card.getColor() != null) {
			switch (card.getColor()) {
			case RED:
				return WizardApp.load(CardView.class, "card-heart.fxml");
			case GREEN:
				return WizardApp.load(CardView.class, "card-pik.fxml");
			case BLUE:
				return WizardApp.load(CardView.class, "card-cross.fxml");
			case YELLOW:
				return WizardApp.load(CardView.class, "card-caro.fxml");
			}
		}
		switch (card.getValue()) {
			case N:
				return WizardApp.load(CardView.class, "card-none.fxml");
			case Z:
				return WizardApp.load(CardView.class, "card-wizard.fxml");
			default:
		}
		
		throw new IllegalArgumentException("Unsupported card: " + card);
	}
}
