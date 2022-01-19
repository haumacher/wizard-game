/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.util.List;

import de.haumacher.wizard.ClientHandler;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.msg.StartGame;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

/**
 * Controller for the game lobby view listing joining players.
 */
public class GameLobby extends Controller implements ListChangeListener<Player>, EventHandler<ActionEvent>, Callback<ListView<Player>, ListCell<Player>> {
	
	@FXML
	ListView<Player> playerList;
	
	@FXML
	Button startGame;

	private ClientHandler _handler;

	private String _gameId;
	
	@Override
	public void initialize() {
		super.initialize();
		
		playerList.getItems().addListener(this);
		startGame.setOnAction(this);
		playerList.setEditable(false);
		playerList.setCellFactory(this);
		playerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	public void setPlayers(List<Player> players) {
		playerList.getItems().setAll(players);
	}

	public void addPlayer(Player player) {
		playerList.getItems().add(player);
	}

	@Override
	public void onChanged(Change<? extends Player> c) {
		startGame.setDisable(c.getList().size() < 3);
	}

	@Override
	public void handle(ActionEvent event) {
		_handler.sendCommand(StartGame.create().setGameId(_gameId));
	}

	public void initController(ClientHandler handler, String gameId) {
		_handler = handler;
		_gameId = gameId;
	}

	@Override
	public ListCell<Player> call(ListView<Player> view) {
		return new ListCell<Player>() {
			@Override
			protected void updateItem(Player item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty) {
					setText(null);
				} else {
					setText(item.getName());
				}
			}
		};
	}

}
