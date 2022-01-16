/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.ClientHandler;
import de.haumacher.wizard.msg.Card;
import de.haumacher.wizard.msg.Color;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.SelectTrump;
import de.haumacher.wizard.msg.Value;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class TrumpSelection extends Controller {
	
	@FXML
	TilePane selectPane;
	
	@FXML
	Text info;

	private ClientHandler _handler;

	private Player _player;

	private boolean _allowSelect;

	public void init(ClientHandler handler, Player player, boolean allowSelect) {
		_handler = handler;
		_player = player;
		_allowSelect = allowSelect;
		
		if (_allowSelect) {
			info.setText("Du darfst die Trumpffarbe wählen!");
		} else {
			info.setText(player.getName() + " wählt die Trumpffarbe...");
		}
		
		for (Color color : Color.values()) {
			Node cardView = CardView.createCard(Card.create().setColor(color).setValue(Value.Z));
			selectPane.getChildren().add(cardView);

			if (allowSelect) {
				cardView.setOnMouseClicked(e -> {
					_handler.sendCommand(SelectTrump.create().setTrumpColor(color));
				});
			}
		}
	}

}
