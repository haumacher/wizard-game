/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.haumacher.msgbuf.data.DataObject;
import de.haumacher.msgbuf.json.JsonReader;
import de.haumacher.msgbuf.json.JsonWriter;
import de.haumacher.msgbuf.server.io.ReaderAdapter;
import de.haumacher.msgbuf.server.io.WriterAdapter;
import de.haumacher.wizard.msg.Error;
import de.haumacher.wizard.server.db.DBException;
import de.haumacher.wizard.server.db.UserDB;
import de.haumacher.wizard.server.db.UserDBService;
import de.haumacher.wizard.server.db.model.AddEmail;
import de.haumacher.wizard.server.db.model.CreateAccount;
import de.haumacher.wizard.server.db.model.CreateAccountResult;
import de.haumacher.wizard.server.db.model.LoginCmd;
import de.haumacher.wizard.server.db.model.Success;
import de.haumacher.wizard.server.db.model.VerifyEmail;

/**
 * Account management servlet.
 */
@WebServlet(loadOnStartup = 1, value = "/account")
public class AccountServlet extends HttpServlet implements LoginCmd.Visitor<Void, HttpServletResponse, IOException> {

	private static final String TEXT_JSON = "text/json";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("utf-8");
		resp.setContentType("text/plain");
		resp.getWriter().println("Wizard login, pleas POST your request.");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String contentType = req.getContentType();
		if (!contentType.equals(TEXT_JSON)) {
			sendError(resp, HttpServletResponse.SC_NOT_ACCEPTABLE, "Invalid content type, expecting: " + TEXT_JSON);
			return;
		}
		
		LoginCmd cmd = LoginCmd.readLoginCmd(new JsonReader(new ReaderAdapter(req.getReader())));
		if (cmd == null) {
			sendError(resp, HttpServletResponse.SC_NOT_IMPLEMENTED, "Request not understood.");
			return;
		}
		
		cmd.visit(this, resp);
	}

	private void sendError(HttpServletResponse resp, int code, String message)
			throws IOException {
		resp.setStatus(code);
		sendResult(resp, Error.create().setMessage(message));
	}

	private void sendResult(HttpServletResponse resp, DataObject message) throws IOException {
		resp.setContentType(TEXT_JSON);
		resp.setCharacterEncoding("utf-8");
		message.writeTo(new JsonWriter(new WriterAdapter(resp.getWriter())));
	}

	@Override
	public Void visit(CreateAccount self, HttpServletResponse resp) throws IOException {
		UserDB db = UserDBService.getInstance();
		
		CreateAccountResult createUser;
		try {
			createUser = db.createUser(self.getNickname(), self.getLanguage());
		} catch (DBException ex) {
			sendError(resp, HttpServletResponse.SC_CONFLICT, "Create failed: " + ex.getMessage());
			return null;
		}
		
		sendResult(resp, CreateAccountResult.create()
			.setUid(createUser.getUid())
			.setSecret(createUser.getSecret()));
		return null;
	}

	@Override
	public Void visit(AddEmail self, HttpServletResponse resp) throws IOException {
		UserDB db = UserDBService.getInstance();
		
		try {
			db.addEmail(self.getUid(), self.getSecret(), self.getEmail());
		} catch (DBException ex) {
			sendError(resp, HttpServletResponse.SC_FORBIDDEN, "Verification failed: " + ex.getMessage());
			return null;
		}

		sendResult(resp, Success.create().setMessage("Email added."));
		return null;
	}

	@Override
	public Void visit(VerifyEmail self, HttpServletResponse resp) throws IOException {
		UserDB db = UserDBService.getInstance();
		
		try {
			db.verifyEmail(self.getUid(), self.getSecret(), self.getToken());
		} catch (DBException ex) {
			sendError(resp, HttpServletResponse.SC_FORBIDDEN, "Verification failed: " + ex.getMessage());
			return null;
		}
		
		sendResult(resp, Success.create().setMessage("Email verification successful."));
		return null;
	}
}
