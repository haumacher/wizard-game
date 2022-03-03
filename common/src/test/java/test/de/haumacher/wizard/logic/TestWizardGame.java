/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package test.de.haumacher.wizard.logic;

import java.io.IOException;
import java.util.Locale;

import de.haumacher.wizard.logic.ClientConnection;
import de.haumacher.wizard.logic.GameClient;
import de.haumacher.wizard.logic.WizardGame;
import de.haumacher.wizard.msg.Msg;
import de.haumacher.wizard.msg.Player;
import de.haumacher.wizard.resources.StaticResources.Resource;
import junit.framework.TestCase;

/**
 * Test for {@link WizardGame}.
 */
public class TestWizardGame extends TestCase {
	
	static class TestClient implements GameClient {

		private final String _id;
		
		/** 
		 * Creates a {@link TestWizardGame.TestClient}.
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
		public void sendError(Resource message) {
			fail(message.format(Locale.getDefault()));
		}
		
		@Override
		public void reconnectTo(ClientConnection connection) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean disconnect(ClientConnection connection) {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public void testGame() throws IOException {
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
