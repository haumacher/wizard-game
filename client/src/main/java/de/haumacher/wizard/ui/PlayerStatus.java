/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import de.haumacher.wizard.msg.Player;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Controller of player status view.
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class PlayerStatus extends Controller {
	
	/**
	 * {@link Rectangle} showing by color, if this player is currently in command.
	 */
	@FXML
	Rectangle activityView;
	
	/**
	 * Display of the player name.
	 */
	@FXML
	Text nameField;

	/**
	 * Points of this player collected so far.
	 */
	@FXML
	Text pointsField;
	
	/**
	 * Box showing {@link Rectangle}s for bids (gray), green colored ones are tricks already won if they match the bid
	 * and red if they exceed the bid.
	 */
	@FXML
	HBox bidAndTrickPane;

	private Player _player;

	private int _bid;

	private int _tricks;

	private int _score;
	
	@Override
	public void initialize() {
		super.initialize();
		
		clearPoints();
		clearTricks();
		setActive(false);
	}

	public void setActive(boolean value) {
		activityView.setFill(value ? Color.YELLOW : Color.WHITE);
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
	
	private void clearPoints() {
		pointsField.setText("");
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
		bidAndTrickPane.getChildren().clear();
		for (int n = 0; n < _bid; n++) {
			addRect(n, rect(n < _tricks ? Color.GREEN : Color.LIGHTGRAY));
		}
		for (int n = _bid; n < _tricks; n++) {
			addRect(n, rect(Color.RED));
		}
	}

	private void addRect(int n, Rectangle rect) {
		bidAndTrickPane.getChildren().add(rect);
		if (n > 0) {
			// Overlap borders.
			HBox.setMargin(rect, new Insets(0, 0, 0, -2));
		}
	}

	private Rectangle rect(Paint fill) {
		Rectangle rect = new Rectangle(10, 20, fill);
		rect.setStroke(Color.BLACK);
		rect.setStrokeWidth(2);
		rect.setArcWidth(0);
		rect.setArcHeight(0);
		return rect;
	}

	public void clearTricks() {
		_bid = 0;
		_tricks = 0;
		bidAndTrickPane.getChildren().clear();
	}

	public void incTricks() {
		_tricks++;
		updateTricks();
	}

}
