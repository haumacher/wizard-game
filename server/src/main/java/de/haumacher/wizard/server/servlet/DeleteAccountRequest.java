/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.servlet;

import java.io.IOException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.wizard.server.mail.MailServiceStarter;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet sending a confirmation code to an e-mail whose account should be deleted.
 */
@WebServlet(loadOnStartup = 1, value = DeleteAccountRequest.PATH)
public class DeleteAccountRequest extends HttpServlet {

	private static final Logger LOG = LoggerFactory.getLogger(DeleteAccountRequest.class);
	
	public static final String PATH = "/delete-account/request-token";
	
	private SecureRandom _rnd = new SecureRandom();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter(DeleteAccountConfirm.EMAIL_PARAM);
		
		String token = Integer.toString(_rnd.nextInt(100000000));
		token = "0".repeat(8 - token.length()) + token;
		
		String uid = Long.toBinaryString(_rnd.nextLong());
		
		try {
			MailServiceStarter.getInstance().sendActivationMail(email, uid, token);
		} catch (MessagingException ex) {
			LOG.error("Account activation for '" + email + "' failed: " + ex.getMessage(), ex);
			resp.sendRedirect(req.getContextPath() + "/delete-account/failed.jsp");
			return;
		}
		
		HttpSession session = req.getSession();
		session.setAttribute(AccountServlet.TOKEN_PARAM, token);
		session.setAttribute(AccountServlet.UID_PARAM, uid);
		
		req.setAttribute(DeleteAccountConfirm.EMAIL_PARAM, email);
		req.setAttribute(AccountServlet.UID_PARAM, uid);
		
		req.getRequestDispatcher("/delete-account/confirm.jsp").forward(req, resp);
	}
	
}
