/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.logic;

import de.haumacher.wizard.resources.StaticResources;

/**
 * Resources for the game logic.
 */
public class R extends StaticResources {

	public static M0 errAllBidsPlaced;
	public static M0 errNotYourTurnToBid;
	public static M0 errNotAllBidsPlaced;
	public static M0 errNotYourTurn;
	public static M0 errAllPlayersMustConfirm;
	public static M0 errWrongCard;
	public static M0 errMustFollowSuit;
	public static M0 errAlreadyConfirmed;
	public static M0 errTrumpAlreadySelected;
	public static M0 errYouCannotSelectTrump;
	public static M0 errMustSelectTrump;
	public static M0 errNoTrumpSelectionAllowed;
	public static M0 errNotLoggedIn;
	public static M0 errNoGameJoined;
	public static M0 errGameAlreadyJoined;
	public static M0 errGameNoLongerExists;
	public static M0 errMustNotStartForeignGame;
	public static M0 errGameAlreadyStarted;
	public static M0 errVersionToNew;
	public static M0 errVersionToOld;
	public static M0 errInvalidCredentials;
	public static M0 errPlayerNotFound;
	public static M1 errFailureOccurred_message;
	public static M1 errAccountCreationFailed_msg;
	public static M1 errCannotAddEmail_msg;
	public static M1 sendingVerificationMailFailed_msg;
	public static M1 errVerificationFailed_msg;
	public static M0 connectionAccepted;
	
	static {
		load(R.class);
	}

}
