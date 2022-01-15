/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.ClientHandler;
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

	private ClientHandler _handler;

	public void init(ClientHandler handler) {
		_handler = handler;

		table.getChildren().clear();
		info.setText(null);
		
		next.setVisible(false);
	}

	public void setInfo(String message) {
		info.setText(message);
		next.setVisible(false);
	}

	public void addCard(Card card) {
		table.getChildren().add(CardView.createCard(card));
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
