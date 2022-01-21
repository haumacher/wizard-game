/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.function.Consumer;

import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.msgbuf.json.JsonToken;
import de.haumacher.msgbuf.json.JsonWriter;
import de.haumacher.msgbuf.server.io.ReaderAdapter;
import de.haumacher.msgbuf.server.io.WriterAdapter;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.msg.Msg;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Handler for the client {@link Socket}.
 * 
 * <p>
 * Connects to the Wizard game server and processes messages in a separate {@link Thread}.
 * </p>
 */
public class GameConnection implements AutoCloseable, WizardServer {

	private JsonWriter _out;

	private Socket _socket;

	private Thread _consumer;

	private String _host;

	private int _port;

	/**
	 * Creates a {@link GameConnection}.
	 * 
	 * @param host
	 *        The host name of the game server.
	 * @param port
	 *        The game server port.
	 */
	public GameConnection(String host, int port) {
		_host = host;
		_port = port;
	}

	/**
	 * Opens the connection to the game server and starts receiving messages from it.
	 * 
	 * @param onMessage
	 *        the consumer of messages received from the server.
	 */
	public void start(Consumer<Msg> onMessage) throws IOException {
		_socket = new Socket(_host, _port);
		JsonReader in = new JsonReader(new ReaderAdapter(new InputStreamReader(_socket.getInputStream(), "utf-8")));
		_consumer = new Thread() {
			@Override
			public void run() {
				try {
					in.beginArray();
					while (true) {
						if (in.peek() == JsonToken.END_ARRAY) {
							break;
						}
						Msg msg = Msg.readMsg(in);

						System.out.println("< " + msg);
						onMessage.accept(msg);
					}
					in.endArray();
				} catch (IOException ex) {
					System.out.println("Connection reset.");
				}
			}
		};
		_consumer.start();

		_out = new JsonWriter(new WriterAdapter(new OutputStreamWriter(_socket.getOutputStream(), "utf-8")));
		_out.beginArray();
	}

	@Override
	public void sendCommand(Cmd cmd) {
		try {
			cmd.writeTo(_out);
			_out.flush();
		} catch (IOException ex) {
			new Alert(AlertType.ERROR,
					"Kommunikation fehlgeschlagen: " + ex.getMessage(),
					ButtonType.OK).show();
		}

		System.out.println("> " + cmd);
	}

	@Override
	public void close() throws IOException {
		_out.endArray();
		_out.flush();
		_out.close();
		_socket.close();
	}
}
