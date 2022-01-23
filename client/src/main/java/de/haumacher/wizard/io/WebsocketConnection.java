/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.io;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.function.Consumer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import de.haumacher.msgbuf.io.StringR;
import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.msg.Msg;

/**
 * Handler for the client {@link Socket}.
 * 
 * <p>
 * Connects to the Wizard game server and processes messages in a separate {@link Thread}.
 * </p>
 */
@ClientEndpoint
public class WebsocketConnection implements WizardConnectionSPI {

	private String _url;

	private Session _session;

	private Consumer<Msg> _onMessage;

	/**
	 * Creates a {@link WebsocketConnection}.
	 * 
	 * @param url
	 *        The host name of the game server.
	 */
	public WebsocketConnection(String url) {
		_url = url;
	}

	@Override
	public void start(Consumer<Msg> onMessage) throws Exception {
		_onMessage = onMessage;
		
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(this, new URI(_url));
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Opening websocket.");
		_session = session;
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("Closing websocket.");
		_session = null;
	}

	@OnMessage
	public void onMessage(String message) {
		try {
			Msg msg = Msg.readMsg(new JsonReader(new StringR(message)));
			System.out.println("< " + msg);
			_onMessage.accept(msg);
		} catch (IOException ex) {
			System.err.println("Failed to read message.");
			ex.printStackTrace();
		}
	}

	@Override
	public void sendCommand(Cmd cmd) {
		_session.getAsyncRemote().sendText(cmd.toString());
		System.out.println("> " + cmd);
	}

	@Override
	public void close() throws IOException {
		_session.close();
		System.out.println("Closed game connection.");
	}
}
