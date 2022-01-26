/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.util.Map;

import de.haumacher.wizard.R;
import de.haumacher.wizard.controller.GenericController;
import de.haumacher.wizard.io.WizardConnection;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Player;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

/**
 * View that allows a player to make his prophecy.
 */
public class ProphecyPane extends GenericController {
	
	@FXML
	ChoiceBox<Integer> prophecySelector;

	@FXML
	Text infoField;

	private WizardConnection _server;

	public void setPlayers(WizardConnection server, Map<String, Player> players) {
		_server = server;
		
		prophecySelector.setVisible(false);
	}

	private static int intValue(Integer value) {
		return value == null ? 0 : value.intValue();
	}

	public void requestBid(Player player, boolean itsMe, int round, int remainingTricks) {
		if (itsMe) {
			infoField.setText(R.makeYourProphecy_remaining.format(remainingTricks));
			prophecySelector.getItems().clear();
			for (int n = 0; n <= round; n++) {
				prophecySelector.getItems().add(Integer.valueOf(n));
			}
			prophecySelector.setOnAction(e -> {
				Integer value = prophecySelector.getValue();
				if (value != null) {
					_server.sendCommand(Bid.create().setCnt(value.intValue()));
				}
			});
			prophecySelector.setVisible(true);
		} else {
			prophecySelector.setVisible(false);
			infoField.setText(R.makesProphecy_player.format(player.getName()));
		}
	}

}
