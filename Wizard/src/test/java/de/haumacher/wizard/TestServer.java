/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.io.IOException;

import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import junit.framework.TestCase;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class TestServer extends TestCase {
	
	static class TestClient implements GameClient {

		private final String _id;
		
		/** 
		 * Creates a {@link TestServer.TestClient}.
		 */
		public TestClient(String id) {
			_id = id;
		}

		@Override
		public String getId() {
			return _id;
		}

		@Override
		public void sendMessage(Msg msg) {
			System.out.println(_id + ": " + msg);
		}

		@Override
		public Player getData() {
			return Player.create().setId(getId()).setName(getId());
		}
		
		@Override
		public void sendError(String message) {
			fail(message);
		}
		
	}
	
	public void testServer() throws IOException {
		WizardGame game = new WizardGame();
		
		TestClient p1 = new TestClient("P1");
		game.addPlayer(p1);
		TestClient p2 = new TestClient("P2");
		game.addPlayer(p2);
		TestClient p3 = new TestClient("P3");
		game.addPlayer(p3);
		
		game.start();
		
	}

}
