/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.util.ResourceBundle;

import de.haumacher.wizard.resources.StaticResources;

/**
 * Loaded resources for the game.
 */
public class R extends StaticResources {
	
	public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("de/haumacher/wizard/WizardResources");

	public static R1 communicationError_msg;
	
	public static String nicknameRequired;

	public static String serverAddressRequired;

	public static String join;

	public static R2 round_round_maxRound;

	public static R1 tricksAnnounced_tricks;

	public static String yourTurn;

	public static R1 playersTurn_player;

	public static R1 trickWonBy_player;

	public static String waitingForConfirm;

	public static String trickWonByYou;

	public static String youEarn_points;

	public static String waitingForOthers;

	public static R1 makeYourProphecy_remaining;

	public static R1 makesProphecy_player;

	public static R2 ranking_player_points;

	public static String appName;

	public static String selectTrump;

	public static R1 selectingTrump_player;

	public static String valueWizard;
	
	public static String valueJester;
	
	static {
		load(R.class, BUNDLE);
	}
}
