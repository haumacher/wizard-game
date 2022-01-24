/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.controller.GenericController;
import de.haumacher.wizard.io.WizardConnection;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.Suit;
import de.haumacher.wizard.msg.Value;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

/**
 * View that is displayed if a wizard was chosen as "trump" card to allow a player to select the trump color. 
 */
public class TrumpSelection extends GenericController {
	
	@FXML
	TilePane selectPane;
	
	@FXML
	Text info;

	public void init(WizardConnection server, Player player, boolean allowSelect) {
		if (allowSelect) {
			info.setText("Du darfst die Trumpffarbe wählen!");
			
			for (Suit suit : Suit.values()) {
				Node cardView = CardView.createCard(Card.create().setSuit(suit).setValue(Value.Z));
				cardView.setOnMouseClicked(e -> {
					server.sendCommand(SelectTrump.create().setTrumpSuit(suit));
				});
				
				selectPane.getChildren().add(cardView);
			}
		} else {
			info.setText(player.getName() + " wählt die Trumpffarbe...");
			
			selectPane.getChildren().clear();
		}
	}

}
