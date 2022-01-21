/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.WizardServer;
import de.haumacher.wizard.msg.Card;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

/**
 * {@link Controller} for the pane that shows the current trick.
 */
public class CurrentTrick extends Controller {
	
	@FXML
	TilePane table;
	
	@FXML
	Text info;
	
	@FXML
	Button next;

	private WizardServer _server;

	public void init(WizardServer server) {
		_server = server;

		table.getChildren().clear();
		info.setText(null);
		
		next.setVisible(false);
	}

	public void setInfo(String message) {
		info.setText(message);
		next.setVisible(false);
	}

	public void addCard(Card card, String playerName) {
		table.getChildren().add(CardWithPlayer.createCard(card, playerName));
	}

	public void clear() {
		table.getChildren().clear();
		info.setText(null);
		next.setVisible(false);
	}

	public void confirm(String message, EventHandler<ActionEvent> continuation) {
		info.setText(message);
		next.setOnAction(continuation);
		next.setVisible(true);
	}

}
