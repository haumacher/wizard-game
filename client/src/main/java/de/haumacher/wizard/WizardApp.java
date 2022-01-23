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
import de.haumacher.wizard.ui.ConnectDialog;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 * Client {@link Application} providing the user interface for the Wizard game.
 */
public class WizardApp extends Application implements WizardUI {
	
	@FXML
	ScrollPane main;
	
	private Scene _scene;
	WizardConnectionSPI _connection;
	
	private WizardController _client;

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WizardApp.fxml"));
		loader.setController(this);
		_scene = loader.load();
		
		stage.setScene(_scene);
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		if (_connection != null) {
			try {
				_connection.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		super.stop();
	}
	
	public void menuConnect(Event evt) {
		ConnectDialog dialog = Controller.load(ConnectDialog.class);
		dialog.setServerAddr("wss://play.haumacher.de/wizard-game/ws");
		
		dialog.show(data -> {
			try {
				if (_connection != null) {
					_connection.close();
					_connection = null;
				}
				
				String serverAddr = data.getServerAddr();
				if (serverAddr.startsWith("wss:") || serverAddr.startsWith("ws:")) {
					_connection = new WebsocketConnection(serverAddr);
				} else {
					_connection = new PlainConnection(serverAddr, 8090);
				}
				_client = new WizardController(_connection, this);
				_connection.start(_client);
				_connection.sendCommand(Login.create().setName(data.getNickName()).setVersion(WizardGame.PROTOCOL_VERSION));
				_connection.sendCommand(ListGames.create());
			} catch (Exception ex) {
				_connection = null;
				showError(ex);
			}
		});
	}
	
	private void showError(Throwable ex) {
		new Alert(AlertType.ERROR, "Kommunikation nicht möglich: " + ex.getMessage(), ButtonType.CLOSE).show();
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
