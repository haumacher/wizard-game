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

import de.haumacher.wizard.server.db.DBException;
import de.haumacher.wizard.server.db.UserDB;
import de.haumacher.wizard.server.db.UserDBService;

/**
 * Account management servlet.
 */
@WebServlet(loadOnStartup = 1, value = AccountServlet.PATH)
public class AccountServlet extends HttpServlet {

	public static final String PATH = "/account";
	
	public static final String UID_PARAM = "uid";

	public static final String TOKEN_PARAM = "token";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uid = req.getParameter(UID_PARAM);
		String token = req.getParameter(TOKEN_PARAM);
		
		UserDB db = UserDBService.getInstance();
		
		try {
			db.verifyEmail(uid, token);
		} catch (DBException ex) {
			System.err.println("Account activation for '" + uid + "' failed: " + ex.getMessage());
			resp.sendRedirect(req.getContextPath() + "/verification-failed.html");
			return;
		}
		
		resp.sendRedirect(req.getContextPath() + "/verification-success.html");
	}
	
}
