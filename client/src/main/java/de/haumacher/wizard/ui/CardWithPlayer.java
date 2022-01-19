/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.WizardApp;
import de.haumacher.wizard.msg.Card;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Display of a card with an added player name who played the card.
 */
public class CardWithPlayer extends Controller {
	
	@FXML 
	Pane card;
	
	@FXML
	Text name;
	
	
	public void initialize(Node cardView, String playerName) {
		card.getChildren().setAll(cardView);
		name.setText(playerName);
	}


	public static Node createCard(Card cardData, String playerName) {
		Node view = WizardApp.load(CardWithPlayer.class, "CardWithPlayer.fxml");
		CardWithPlayer result = (CardWithPlayer) view.getUserData();
		result.initialize(CardView.createCard(cardData), playerName);
		return view;
	}

}
