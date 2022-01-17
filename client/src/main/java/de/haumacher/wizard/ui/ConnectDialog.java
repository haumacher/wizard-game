/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.util.function.Consumer;

import de.haumacher.wizard.data.ConnectData;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class ConnectDialog {
	
	@FXML
	Stage main;
	
	@FXML
	TextField nickName;
	
	@FXML
	TextField serverAddr;

	private Consumer<ConnectData> _onConnect;
	
	/** 
	 * Creates a {@link ConnectDialog}.
	 */
	public ConnectDialog() {
	}
	
	@FXML
	public void initialize() {
		main.setUserData(this);
		
		nickName.requestFocus();
	}
	
	public void show(Consumer<ConnectData> onConnect) {
		_onConnect = onConnect;
		main.showAndWait();
	}

	public void onCloseDialog(Event evt) {
		main.hide();
	}
	
	public void onEnter(Event evt) {
		Control source = (Control) evt.getSource();
		source.setStyle("");
		source.setTooltip(null);
	}
	
	public void onConnect(Event evt) {
		String name = nickName.getText().trim();
		String addr = serverAddr.getText().trim();
		boolean error = false;
		if (name.isEmpty()) {
			nickName.setStyle("-fx-border-color: red;");
			nickName.setTooltip(new Tooltip("Du musst einen Nick-Name angeben."));
			error = true;
		}
		if (addr.isEmpty()) {
			serverAddr.setStyle("-fx-border-color: red;");
			serverAddr.setTooltip(new Tooltip("Die Server-Adresse darf nicht leer sein."));
			error = true;
		}
		if (!error) {
			_onConnect.accept(ConnectData.create().setNickName(name).setServerAddr(addr));
			main.hide();
		}
	}
}
