/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.util.HashMap;
import java.util.Map;

import de.haumacher.wizard.ClientHandler;
import de.haumacher.wizard.msg.Bid;
import de.haumacher.wizard.msg.Player;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class ProphecyPane extends Controller {
	
	@FXML
	Text sumField;
	
	@FXML
	GridPane prophecyTable;

	@FXML
	Text infoField;

	private Map<String, Integer> _playerRows = new HashMap<>();

	private ClientHandler _client;

	public void setPlayers(ClientHandler client, Map<String, Player> players) {
		_client = client;
		
		// Clear all but the first row.
		prophecyTable.getChildren().removeIf(c -> intValue(GridPane.getRowIndex(c)) != 0);
		
		int row = 1;
		for (Player player : players.values()) {
			_playerRows.put(player.getId(), Integer.valueOf(row));
			
			Text nameField = new Text(player.getName());
			prophecyTable.add(nameField, 0, row++);
			
			while (prophecyTable.getRowConstraints().size() < row) {
				prophecyTable.getRowConstraints().add(new RowConstraints(30, 30, 30));
			}
		}
	}

	private static int intValue(Integer value) {
		return value == null ? 0 : value.intValue();
	}

	public void requestBid(String playerId, int round, int expected) {
		setExpected(expected);
		
		int row = removeBid(playerId);
		ChoiceBox<Integer> choices = new ChoiceBox<Integer>();
		for (int n = 0; n <= round ; n++) {
			choices.getItems().add(Integer.valueOf(n));
		}
		choices.setOnAction(e -> {
			Integer value = choices.getValue();
			if (value != null) {
				_client.sendCommand(Bid.create().setCnt(value.intValue()));
			}
		});
		prophecyTable.add(choices, 1, row);
	}

	public void setBid(String playerId, int cnt, int expected) {
		setExpected(expected);
		
		int row = removeBid(playerId);
		prophecyTable.add(new Text(Integer.toString(cnt)), 1, row);
	}

	private void setExpected(int expected) {
		sumField.setText(Integer.toString(expected));
	}
	
	private int removeBid(String playerId) {
		int row = row(playerId);
		removeBid(row);
		return row;
	}

	private void removeBid(int row) {
		prophecyTable.getChildren().removeIf(c -> intValue(GridPane.getRowIndex(c)) == row && intValue(GridPane.getColumnIndex(c)) == 1);
	}

	private int row(String playerId) {
		return _playerRows.get(playerId);
	}

}
