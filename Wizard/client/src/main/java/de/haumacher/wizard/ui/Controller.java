/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public abstract class Controller {

	@FXML
	Node main;
	
	@FXML
	public void initialize() {
		main.setUserData(this);
	}
	
	/**
	 * The top-level view of this {@link Controller}.
	 */
	public Node getView() {
		return main;
	}
	
}
