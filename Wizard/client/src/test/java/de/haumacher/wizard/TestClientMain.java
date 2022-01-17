/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.io.IOException;
import java.io.InputStreamReader;

import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.msgbuf.server.io.ReaderAdapter;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.msg.Msg;

/**
 * Dummy game client that allows to communicate with a game through entering JSON messages on the command line.
 */
public class TestClientMain {

	public static void main(String[] args) throws IOException {
		ClientHandler handler = new ClientHandler() {
			@Override
			protected void onMessage(Msg msg) {
				System.out.println("< " + msg);
			}
		};
		handler.start("localhost", 8090);
		
		ReaderAdapter keyboard = new ReaderAdapter(new InputStreamReader(System.in));
		while (true) {
			System.out.print("> ");
			Cmd cmd = Cmd.readCmd(new JsonReader(keyboard));
			handler.sendCommand(cmd);
		}
		
	}

}
