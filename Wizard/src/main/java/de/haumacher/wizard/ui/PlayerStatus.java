/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.msg.Player;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Controller of player status view.
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class PlayerStatus extends Controller {
	
	@FXML
	Rectangle activityView;
	
	@FXML
	Text nameField;

	@FXML
	Text pointsField;

	private Player _player;

	private int _bid;

	private int _tricks;

	private int _score;
	
	@Override
	public void initialize() {
		super.initialize();
		
		pointsField.setText("");
	}
	
	public Player getPlayer() {
		return _player;
	}
	
	public void setPlayer(Player player) {
		_player = player;
		nameField.setText(_player.getName());
	}
	
	public void setScore(int score) {
		_score = score;
		updateScore();
	}

	public void addScore(int delta) {
		_score += delta;
		updateScore();
	}

	private void updateScore() {
		pointsField.setText("(" + _score + ")");
	}
	
	public void setBid(int bid) {
		_bid = bid;
		_tricks = 0;
		updateTricks();
	}
	
	public void setTricks(int tricks) {
		_tricks = tricks;
		updateTricks();
	}

	private void updateTricks() {
		nameField.setText(_player.getName() + " " + _tricks + " von " + _bid);
	}

	public void clearTricks() {
		_bid = 0;
		_tricks = 0;
		nameField.setText(_player.getName());
	}

	public void incTricks() {
		_tricks++;
		updateTricks();
	}

}
