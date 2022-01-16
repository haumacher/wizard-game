/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.ClientHandler;
import de.haumacher.wizard.msg.FinishGame;
import de.haumacher.wizard.msg.PlayerScore;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class RankingView extends Controller {

	@FXML
	GridPane rankingTable;

	@FXML
	Text info;
	
	@FXML
	Button okButton;

	public void show(ClientHandler handler, FinishGame msg, EventHandler<ActionEvent> onOk) {
		int lastScore = Integer.MIN_VALUE;
		int rank = 0;
		int row = 1;
		for (PlayerScore playerScore : msg.getScores()) {
			int currentScore = playerScore.getScore();
			if (currentScore > lastScore) {
				rank++;
				lastScore = currentScore;
			}
			rankingTable.add(new Text(Integer.toString(rank)), 0, row);
			rankingTable.add(new Text(playerScore.getPlayer().getName() + " mit " + Integer.toString(currentScore) + " Punkten"), 1, row);
			row++;
		}
		okButton.setOnAction(onOk);
	}
	
}
