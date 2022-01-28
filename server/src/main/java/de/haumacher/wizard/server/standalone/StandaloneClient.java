/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.standalone;

import java.io.IOException;

import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.msgbuf.json.JsonToken;
import de.haumacher.msgbuf.json.JsonWriter;
import de.haumacher.msgbuf.server.io.ReaderAdapter;
import de.haumacher.msgbuf.server.io.WriterAdapter;
import de.haumacher.wizard.logic.R;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.server.ClientHandler;
import de.haumacher.wizard.server.WizardServer;


public class StandaloneClient implements Runnable {

	private JsonReader _in;
	private JsonWriter _out;
	
	private ClientHandler _clientHandler;

	public StandaloneClient(WizardServer server, ReaderAdapter in, WriterAdapter out) {
		_in = new JsonReader(in);
		_out = new JsonWriter(out);

		_clientHandler = new ClientHandler(server, msg -> {
			try {
				synchronized (_out) {
					System.out.println(_clientHandler.getName() + " <- " + msg);
					msg.writeTo(_out);
					_out.flush();
				}
			} catch (IOException ex) {
				System.err.println("Cannot send to " + _clientHandler + ": " + msg + " (" + ex.getMessage() + ")");
			}
		});
	}

	@Override
	public void run() {
		_clientHandler.start();
		try {
			_out.beginArray();
			_in.beginArray();
			while (true) {
				try {
					if (_in.peek() == JsonToken.END_ARRAY) {
						break;
					}
					
					Cmd cmd = Cmd.readCmd(_in);
					_clientHandler.handleCmd(cmd);
				} catch (RuntimeException ex) {
					ex.printStackTrace();
					
					String message = ex.getMessage();
					if (message == null) {
						message = ex.getClass().getName();
					} else {
						message = ex.getClass().getName() + ": " + message;
					}
					_clientHandler.sendError(R.errFailureOccurred_message.fill(message));
				}
			}
			_in.endArray();
			_out.endArray();
		} catch (IOException ex) {
			System.out.println("Client stopped: " + ex.getMessage());
		} finally {
			_clientHandler.stop();
			System.err.println("Client terminated: " + _clientHandler);
		}
	}


}