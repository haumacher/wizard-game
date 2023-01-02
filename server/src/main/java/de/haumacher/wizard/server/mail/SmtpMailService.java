/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.server.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.haumacher.msgbuf.json.Base64Utils;
import de.haumacher.wizard.server.servlet.AccountServlet;

/**
 * Service for sending e-mail messages.
 */
public class SmtpMailService implements MailService {

	private static final Logger LOG = LoggerFactory.getLogger(SmtpMailService.class);
	
	private Session _session;
	private String _user;
	private String _password;
	private Properties _properties;
	private Transport _transport;
	private Address _from;

	/** 
	 * Creates a {@link SmtpMailService}.
	 */
	public SmtpMailService(String user, String password, Properties properties) {
		_user = user;
		_password = password;
		_properties = properties;
	}
	
	@Override
	public void sendActivationMail(String receiver, String uid, String code) throws MessagingException {
		String image = "https://play.haumacher.de/zauberer/images/zauberer.png";
		
		String link = "https://play.haumacher.de/zauberer" + AccountServlet.PATH + "?" + AccountServlet.UID_PARAM + "=" + uid + "&" + AccountServlet.TOKEN_PARAM + "=" + code;
		Message msg = createMessage();
		msg.setSubject("Zauberer account creation");
		
	    MimeMultipart alternativePart = new MimeMultipart("alternative");
	    {
	    	{
    			MimeBodyPart sourcePart = new MimeBodyPart();
    			sourcePart.setText(read("/templates/mail-template.html", link, code, image), "utf-8", "html");
	    		alternativePart.addBodyPart(sourcePart);
	    	}

	    	{
	    		MimeBodyPart text = new MimeBodyPart();
	    		text.setText(read("/templates/mail-template.txt", link, code, image), "utf-8");
	    		alternativePart.addBodyPart(text);
	    	}
	    }
		
		msg.setContent(alternativePart);
		sendMail(receiver, msg);
	}

	public void sendMail(String receiver, String subject, String body) throws NoSuchProviderException, MessagingException {
		Message msg = createMessage();
		msg.setSubject(subject);
		msg.setText(body);
		
		sendMail(receiver, msg);
	}

	public Message createMessage() throws MessagingException {
		Message msg = new MimeMessage(_session);
		msg.setFrom(_from);
		return msg;
	}

	public void sendMail(String receiver, Message msg) throws AddressException, MessagingException {
		InternetAddress address = new InternetAddress(receiver);
		msg.setRecipient(RecipientType.TO, address);
		Address[] addresses = {address};
		_transport.sendMessage(msg, addresses);
	}

	@Override
	public void startUp() {
		try {
			_session = startSession();
			_transport = _session.getTransport();
			_transport.connect(_user, _password);
		} catch (MessagingException ex) {
			LOG.error("Error starting mail service: " + ex.getMessage(), ex);
		}
	}

	private Session startSession() throws AddressException {
		_from = new InternetAddress(_properties.getProperty("mail.smtp.from"));
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(_user, _password);
			}
		};
		return Session.getDefaultInstance(_properties, authenticator);
	}
	
	@Override
	public void shutdown() {
		if (_session != null) {
			try {
				_transport.close();
			} catch (MessagingException ex) {
				ex.printStackTrace();
			}
			
			_transport = null;
			_session = null;
		}
	}

	private String read(String resource, String link, String code, String image) {
		StringBuilder result = new StringBuilder();
		char[] buffer = new char[4096];
		try (InputStream in = getClass().getResourceAsStream(resource)) {
			try (Reader r = new InputStreamReader(in, "utf-8")) {
				while (true) {
					int direct = r.read(buffer);
					if (direct < 0) {
						break;
					}
					result.append(buffer, 0, direct);
				}
			}
		} catch (IOException ex) {
			LOG.error("Cannot read mail template: " + resource, ex);
		}
		return result.toString().replace("{link}", link).replace("{code}", code).replace("{image}", image);
	}

	private String readBase64(String resource) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		try (InputStream in = getClass().getResourceAsStream(resource)) {
			while (true) {
				int direct = in.read(buffer);
				if (direct < 0) {
					break;
				}
				result.write(buffer, 0, direct);
			}
		}
		return Base64Utils.toBase64(result.toByteArray());
	}
	
}
