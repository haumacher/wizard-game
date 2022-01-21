/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard;

import java.io.IOException;
import java.io.InputStreamReader;

import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.msgbuf.server.io.ReaderAdapter;
import de.haumacher.wizard.msg.Cmd;

/**
 * Dummy game client that allows to communicate with a game through entering JSON messages on the command line.
 */
public class TestClientMain {

	public static void main(String[] args) throws IOException {
		try (GameConnection connection = new GameConnection("localhost", 8090)) {
			connection.start(msg -> {
				System.out.println("< " + msg);
			});
			
			ReaderAdapter keyboard = new ReaderAdapter(new InputStreamReader(System.in));
			while (true) {
				System.out.print("> ");
				Cmd cmd = Cmd.readCmd(new JsonReader(keyboard));
				connection.sendCommand(cmd);
			}
		}
	}

}
