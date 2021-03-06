/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.controller;

import java.io.IOError;
import java.io.IOException;
import java.util.PropertyResourceBundle;

import de.haumacher.wizard.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Base class to use as controller for FXML-defined views.
 * 
 * <p>
 * A view using a specialization of this class as controller is assumed to have it's top-level component labeled with
 * the ID <code>main</code>.
 * </p>
 */
public abstract class Controller<N extends Node> {

	@FXML
	N main;
	
	@FXML
	public void initialize() {
		main.setUserData(this);
	}
	
	/**
	 * The top-level view of this {@link Controller}.
	 */
	public N getView() {
		return main;
	}

	public static <T extends Controller> T load(Class<T> controllerClass)  {
		return load(controllerClass, resourceName(controllerClass));
	}

	public static <T extends Controller> T load(Class<T> controllerClass, String resource)  {
		Node view = loadView(controllerClass, resource);
		@SuppressWarnings("unchecked")
		T controller = (T) view.getUserData();
		return controller;
	}
	
	public static <T> T loadView(Class<?> controllerClass)  {
		return loadView(controllerClass, resourceName(controllerClass));
	}
	
	public static <T> T loadView(Class<?> controllerClass, String resource)  {
		
		PropertyResourceBundle x;
		try {
			return FXMLLoader.load(controllerClass.getResource(resource), R.BUNDLE);
		} catch (IOException ex) {
			throw new IOError(ex);
		}
	}

	private static String resourceName(Class<?> controllerClass) {
		return controllerClass.getSimpleName() + ".fxml";
	}
	
}
