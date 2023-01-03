/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import de.haumacher.wizard.logic.ClientConnection;
import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.GameCreated;
import de.haumacher.wizard.msg.GameDeleted;
import de.haumacher.wizard.msg.Msg;

/**
 * Wizard server logic for maintaining games and active players.
 */
public class WizardServer {

	final ConcurrentMap<ClientConnection, ClientConnection> _clients = new ConcurrentHashMap<>();
	
	final ConcurrentMap<String, WizardGame> _games = new ConcurrentHashMap<>();

	/**
	 * Sends the given message to all currently logged-in clients.
	 */
	public void broadCast(Msg msg) {
		for (ClientConnection client : _clients.values()) {
			client.sendMessage(msg);
		}
	}

	/** 
	 * Registers a newly logged-in client.
	 */
	public void addClient(ClientConnection client) {
		_clients.put(client, client);
	}

	/** 
	 * Removes the given client.
	 */
	public void removeClient(ClientConnection client) {
		_clients.remove(client);
	}

	/** 
	 * Creates a new game on behalf of the given client.
	 */
	public WizardGame createGame(GameClient owner) {
		WizardGame game = new WizardGame(this::broadCast, this::onGameFinished);
		_games.put(game.getGameId(), game);
		broadCast(GameCreated.create().setOwnerId(owner.getId()).setGame(game.getData()));
		return game;
	}
	
	private void onGameFinished(String gameId) {
		WizardGame game = _games.remove(gameId);
		if (game != null) {
			broadCast(GameDeleted.create().setGameId(game.getGameId()));
		}
	}

	/** 
	 * Resolves the game with the given ID.
	 */
	public WizardGame getGame(String gameId) {
		return _games.get(gameId);
	}

	/** 
	 * Retrieves all games that wait for players.
	 */
	public Collection<WizardGame> getWaitingGames() {
		return _games.values().stream().filter(g -> g.isAcceptingPlayers()).collect(Collectors.toList());
	}

	/** 
	 * Notifies all active clients about the server shutdown.
	 */
	public void shutdown() {
		// TODO.
	}

}
