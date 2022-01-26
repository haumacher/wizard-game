/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.io.IOException;

import de.haumacher.wizard.controller.Controller;
import de.haumacher.wizard.controller.WizardController;
import de.haumacher.wizard.controller.WizardUI;
import de.haumacher.wizard.io.PlainConnection;
import de.haumacher.wizard.io.WebsocketConnection;
import de.haumacher.wizard.io.WizardConnectionSPI;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.CreateGame;
import de.haumacher.wizard.msg.ListGames;
import de.haumacher.wizard.msg.Login;
import de.haumacher.wizard.msg.Reconnect;
import de.haumacher.wizard.ui.ConnectDialog;
import de.haumacher.wizard.ui.data.ConnectData;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Client {@link Application} providing the user interface for the Wizard game.
 */
public class WizardApp extends Application implements WizardUI {
	
	@FXML
	ScrollPane main;
	
	@FXML
	MenuItem menuConnect;
	
	@FXML
	MenuItem menuDisconnect;
	
	@FXML
	MenuItem menuReconnect;
	
	private Scene _scene;
	WizardConnectionSPI _connection;
	
	private WizardController _client;

	private ConnectData _data;

	private Stage _stage;

	@Override
	public void start(Stage stage) throws Exception {
		_stage = stage;
		_stage.setTitle(R.appName);
		_stage.getIcons().add(new Image(getClass().getResourceAsStream("wizard-icon-32.png")));
		_stage.getIcons().add(new Image(getClass().getResourceAsStream("wizard-icon-64.png")));
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WizardApp.fxml"), R.BUNDLE);
		loader.setController(this);
		_scene = loader.load();
		
		stage.setScene(_scene);
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		closeConnection();
		super.stop();
	}

	public void menuConnect(Event evt) {
		ConnectDialog dialog = Controller.load(ConnectDialog.class);
		
		if (_data != null) {
			dialog.setServerAddr(_data.getServerAddr());
			dialog.setNickName(_data.getNickName());
		} else {
			dialog.setServerAddr("wss://play.haumacher.de/wizard-game/ws");
		}
		
		dialog.show(data -> {
			_data = data;
			try {
				closeConnection();
				openConnection();
				_connection.sendCommand(Login.create().setName(_data.getNickName()).setVersion(WizardGame.PROTOCOL_VERSION));
			} catch (Exception ex) {
				ex.printStackTrace();
				_connection = null;
				showError(ex);
			}
		});
	}

	private void openConnection() throws Exception {
		String serverAddr = _data.getServerAddr();
		if (serverAddr.startsWith("wss:") || serverAddr.startsWith("ws:")) {
			_connection = new WebsocketConnection(serverAddr);
		} else {
			_connection = new PlainConnection(serverAddr, 8090);
		}
		if (_client == null) {
			_client = new WizardController(_connection, this);
		} else {
			_client.connectTo(_connection, this);
		}
		_connection.start(_client);
	}
	
	@Override
	public void onLogin() {
		_connection.sendCommand(ListGames.create());

		menuDisconnect.setVisible(true);

		menuConnect.setVisible(false);
		menuReconnect.setVisible(false);
	}
	
	public void menuDisconnect(Event evt) {
		closeConnection();

		menuDisconnect.setVisible(false);
	
		menuConnect.setVisible(true);
		if (_client.getGame() != null) {
			menuReconnect.setVisible(true);
		}
		
		main.setContent(null);
	}
	
	private void closeConnection() {
		if (_connection != null) {
			try {
				_connection.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			_connection = null;
		}
	}

	public void menuReconnect(Event evt) {
		try {
			openConnection();
			_connection.sendCommand(Reconnect.create().setPlayerId(_client.getPlayerId()).setGameId(_client.getGame().getGameId()));
			
			menuDisconnect.setVisible(true);
			
			menuConnect.setVisible(false);
			menuReconnect.setVisible(false);
		} catch (Exception ex) {
			showError(ex);
		}
	}
	
	public void menuQuit(Event evt) {
		_stage.hide();
	}
	
	private void showError(Throwable ex) {
		new Alert(AlertType.ERROR, R.communicationError_msg.format(ex.getMessage()), ButtonType.CLOSE).show();
	}

	public void newGame(Event evt) {
		_connection.sendCommand(CreateGame.create());
	}

	@Override
	public <C extends Controller<?>> C showView(Class<C> controllerType) {
		Node view = Controller.loadView(controllerType);
		main.setContent(view);
		main.layout();
		
		@SuppressWarnings("unchecked")
		C controller = (C) view.getUserData();
		return controller;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
