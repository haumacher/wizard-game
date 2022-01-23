/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.GameCreated;
import de.haumacher.wizard.msg.GameDeleted;
import de.haumacher.wizard.msg.Msg;

/**
 * Wizard server logic for maintaining games and active players.
 */
public class WizardServer {

	final ConcurrentMap<String, GameClient> _clients = new ConcurrentHashMap<>();
	
	final ConcurrentMap<String, WizardGame> _games = new ConcurrentHashMap<>();

	public void broadCast(Msg msg) {
		for (GameClient other : _clients.values()) {
			other.sendMessage(msg);
		}
	}

	/** 
	 * Registers a newly logged-in client.
	 */
	public void addClient(GameClient client) {
		_clients.put(client.getId(), client);
	}

	/** 
	 * Removes the given client.
	 */
	public void removeClient(GameClient client) {
		_clients.remove(client.getId());
	}

	/** 
	 * Creates a new game on behalf of the given client.
	 */
	public WizardGame createGame(GameClient owner) {
		WizardGame game = new WizardGame(this::broadCast, g -> _games.remove(g.getGameId()));
		_games.put(game.getGameId(), game);
		broadCast(GameCreated.create().setOwnerId(owner.getId()).setGame(game.getData()));
		return game;
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
		return _games.values();
	}

	/** 
	 * Removes the given player from the given game and deletes the game, if it has no more players left.
	 */
	public void removePlayer(WizardGame game, GameClient client) {
		game.removePlayer(client);
		
		if (game.getData().getPlayers().isEmpty()) {
			String gameId = game.getGameId();
			_games.remove(gameId);
			broadCast(GameDeleted.create().setGameId(gameId));
		}
	}

	/** 
	 * Notifies all active clients about the server shutdown.
	 */
	public void shutdown() {
		// TODO.
	}

}
