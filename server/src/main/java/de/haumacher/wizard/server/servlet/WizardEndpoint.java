/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.servlet;

import java.io.IOException;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.msgbuf.io.StringR;
import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.wizard.logic.R;
import de.haumacher.wizard.msg.Cmd;
import de.haumacher.wizard.server.ClientHandler;

/**
 * Websocket endpoint for the wizard game.
 */
@ServerEndpoint(value = "/ws")
//@ServerEndpoint(value = "/ws", configurator = WizardEndpoint.Configurator.class)
public class WizardEndpoint {

	private static final Logger LOG = LoggerFactory.getLogger(WizardEndpoint.class);

	// Makes only sense if there is a session.
	
//	public static class Configurator extends ServerEndpointConfig.Configurator {
//
//	    private static final String HTTP_SESSION = "httpSession";
//
//		@Override
//	    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
//	        HttpSession httpSession = (HttpSession) request.getHttpSession();
//	        config.getUserProperties().put(HTTP_SESSION, httpSession);
//	    }
//
//	}
	
	private Session _session;

	private ClientHandler _clientHandler;
	
	private boolean _closed;
	
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
		LOG.info("Opened websocket connection.");
		
		_session = session;
		
		_clientHandler = new ClientHandler(WizardWebapp.getServer(), msg -> {
			if (_closed) {
				LOG.warn("Skipping message sent to closed connection: " + _clientHandler.getName() + " <- " + msg);
				return;
			}
			
			LOG.info(_clientHandler.getName() + " <- " + msg);
			synchronized (_session) {
				try {
					_session.getBasicRemote().sendText(msg.toString());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		_clientHandler.start();
		
//        HttpSession httpSession = (HttpSession) config.getUserProperties().get(Configurator.HTTP_SESSION);
//        ServletContext servletContext = httpSession.getServletContext();    
//        
//        _server = (WizardServer) servletContext.getAttribute(WizardServer.class.getName());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
		try {
	    	Cmd cmd = Cmd.readCmd(new JsonReader(new StringR(message)));
	    	_clientHandler.handleCmd(cmd);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			
			String report = ex.getMessage();
			if (report == null) {
				report = ex.getClass().getName();
			} else {
				report = ex.getClass().getName() + ": " + report;
			}
			_clientHandler.sendError(R.errFailureOccurred_message.fill(report));
		}
    }
    
    @OnClose
    public void onClose(Session session) throws IOException {
		LOG.info("Closed websocket connection.");

		_closed = true;
		_clientHandler.stop();
    }

    @OnError
    public void onError(Session session, Throwable ex) {
    	LOG.error("Error in endpoint for " + _clientHandler + ": " + ex.getMessage(), ex);
    }
}
