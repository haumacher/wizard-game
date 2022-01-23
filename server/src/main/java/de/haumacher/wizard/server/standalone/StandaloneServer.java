/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.standalone;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import de.haumacher.msgbuf.server.io.ReaderAdapter;
import de.haumacher.msgbuf.server.io.WriterAdapter;
import de.haumacher.wizard.server.WizardServer;

/**
 * Stand-alone server for the wizard game that uses plain sockets for communication.
 */
public class StandaloneServer {

	public static void main(String[] args) throws IOException {
		new StandaloneServer().run();
	}
	
	private final WizardServer _server = new WizardServer();
	
	private void run() throws IOException {
		try (ServerSocket server = new ServerSocket(8090)) {
			while (true) {
				try {
					Socket socket = server.accept();
					StandaloneClient client = createClient(socket.getInputStream(), socket.getOutputStream());
					new Thread(client).start();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	final StandaloneClient createClient(InputStream inStream, OutputStream outStream) throws UnsupportedEncodingException {
		ReaderAdapter in = new ReaderAdapter(new InputStreamReader(inStream, "utf-8"));
		WriterAdapter out = new WriterAdapter(new OutputStreamWriter(outStream, "utf-8"));
		return new StandaloneClient(_server, in, out);
	}
	
}
