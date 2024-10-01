/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.servlet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.server.db.DBException;
import de.haumacher.wizard.server.db.UserDB;
import de.haumacher.wizard.server.db.UserDBService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet checking the confirmation code sent to an e-mail whose account should be deleted.
 */
@WebServlet(loadOnStartup = 1, value = DeleteAccountConfirm.PATH)
public class DeleteAccountConfirm extends HttpServlet {

	private static final Logger LOG = LoggerFactory.getLogger(DeleteAccountConfirm.class);
	
	public static final String PATH = "/delete-account/confirm-token";
	
	public static final String EMAIL_PARAM = "e-mail";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String email = req.getParameter(EMAIL_PARAM);
		String token = req.getParameter(AccountServlet.TOKEN_PARAM);
		String uid = req.getParameter(AccountServlet.UID_PARAM);

		if (session == null || email == null || token == null || uid == null) {
			if (session == null) {
				LOG.error("No session.");
			}
			if (email == null) {
				LOG.error("No e-mail.");
			}
			if (token == null) {
				LOG.error("No token.");
			}
			if (uid == null) {
				LOG.error("No UID.");
			}
			resp.sendRedirect(req.getContextPath() + "/delete-account/failed.jsp");
			return;
		}
		
		String expectedToken = (String) session.getAttribute(AccountServlet.TOKEN_PARAM);
		session.removeAttribute(AccountServlet.TOKEN_PARAM);
		
		String expectedUid = (String) session.getAttribute(AccountServlet.UID_PARAM);
		session.removeAttribute(AccountServlet.UID_PARAM);
		
		if (expectedUid == null || !expectedUid.equals(uid)) {
			LOG.error("Uid mismatch: " + expectedUid + ", vs. " + uid);
			resp.sendRedirect(req.getContextPath() + "/delete-account/failed.jsp");
			return;
		}
		
		if (expectedToken == null || !expectedToken.equals(token)) {
			LOG.error("Token mismatch: " + expectedToken + ", vs. " + token);
			resp.sendRedirect(req.getContextPath() + "/delete-account/failed.jsp");
			return;
		}
		
		UserDB db = UserDBService.getInstance();
		
		boolean success;
		try {
			success = db.deleteUser(email);
		} catch (DBException ex) {
			LOG.error("Account deletion for '" + email + "' failed: " + ex.getMessage(), ex);
			resp.sendRedirect(req.getContextPath() + "/delete-account/failed.jsp");
			return;
		}

		if (success) {
			resp.sendRedirect(req.getContextPath() + "/delete-account/success.jsp");
		} else {
			resp.sendRedirect(req.getContextPath() + "/delete-account/failed.jsp");
		}
	}
	
}
