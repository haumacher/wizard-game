/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.controller;

/**
 * Abstraction of the UI logic of showing views.
 */
public interface WizardUI {

	/** 
	 * Creates and displays a view for the given {@link Controller} class.
	 */
	<C extends Controller<?>> C showView(Class<C> controllerClass);

	/** 
	 * TODO
	 *
	 */
	void onLogin();

}
