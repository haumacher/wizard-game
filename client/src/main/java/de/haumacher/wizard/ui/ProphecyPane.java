/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.util.Map;

import de.haumacher.wizard.WizardConnection;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Player;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class ProphecyPane extends Controller {
	
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
			infoField.setText("Mache Deine Vorhersage! " + remainingTricks + " Stiche verbleiben.");
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
			infoField.setText(player.getName() + " macht eine Vorhersage...");
		}
	}

}
