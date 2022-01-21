/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import de.haumacher.wizard.ui.Controller;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public interface WizardUI {

	/** 
	 * Creates and displays a view for the given {@link Controller} class.
	 */
	<C extends Controller<?>> C showView(Class<C> controllerClass);

}
