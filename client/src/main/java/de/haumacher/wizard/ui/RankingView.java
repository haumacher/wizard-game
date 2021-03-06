/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.R;
import de.haumacher.wizard.controller.GenericController;
import de.haumacher.wizard.io.WizardConnection;
import de.haumacher.wizard.msg.FinishGame;
import de.haumacher.wizard.msg.PlayerScore;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * View showing the final points for players after the game ends.
 */
public class RankingView extends GenericController {

	@FXML
	GridPane rankingTable;

	@FXML
	Text info;
	
	@FXML
	Button okButton;

	public void show(WizardConnection server, FinishGame msg, EventHandler<ActionEvent> onOk) {
		int lastPoints = Integer.MAX_VALUE;
		int rank = 0;
		int row = 1;
		for (PlayerScore playerScore : msg.getScores()) {
			int currentPoints = playerScore.getPoints();
			if (currentPoints < lastPoints) {
				rank++;
				lastPoints = currentPoints;
			}
			rankingTable.add(new Text(Integer.toString(rank)), 0, row);
			rankingTable.add(new Text(R.ranking_player_points.format(playerScore.getPlayer().getName(), Integer.toString(currentPoints))), 1, row);
			row++;
		}
		okButton.setOnAction(onOk);
	}
	
}
