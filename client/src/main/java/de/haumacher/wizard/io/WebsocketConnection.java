/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.io;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.function.Consumer;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

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
		container.setDefaultMaxSessionIdleTimeout(0);
		_session = container.connectToServer(this, new URI(_url));
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Opening websocket.");
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("Closing websocket.");
	}

	@OnMessage
	public void onMessage(String message) {
		try {
			Msg msg = Msg.readMsg(new JsonReader(new StringR(message)));
			if (msg != null) {
				System.out.println("< " + msg);
				_onMessage.accept(msg);
			} else {
				System.err.println("Message not understood: " + message);
			}
		} catch (IOException ex) {
			System.err.println("Failed to read message: " + message);
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
		if (_session != null) {
			_session.close();
			_session = null;
			System.out.println("Closed game connection.");
		}
	}
}
