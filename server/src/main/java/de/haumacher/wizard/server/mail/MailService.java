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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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

import de.haumacher.msgbuf.json.Base64Utils;

/**
 * Service for sending e-mail messages.
 */
public class MailService {

	private Session _session;
	private String _user;
	private String _password;
	private Properties _properties;
	private Transport _transport;
	private Address _from;

	/** 
	 * Creates a {@link MailService}.
	 */
	public MailService(String user, String password, Properties properties) {
		_user = user;
		_password = password;
		_properties = properties;
	}
	
	public void sendActivationMail(String receiver, String code)
			throws MessagingException, IOException, AddressException {
		String imageId = "zauberer_id";
		String image = "cid:" + imageId;
		
		String link = "https://play.haumacher.de/zauberer/activate?uid=4711&token=" + code;
		Message msg = createMessage();
		msg.setSubject("Zauberer account creation");
		
	    MimeMultipart alternativePart = new MimeMultipart("alternative");
	    {
	    	{
	    		MimeBodyPart htmlPart = new MimeBodyPart();
	    		{
	    			Multipart relatedPart = new MimeMultipart("related");
	    			{
	    				MimeBodyPart sourcePart = new MimeBodyPart();
	    				sourcePart.setText(read("/templates/mail-template.html", link, code, image), "utf-8", "html");
	    				relatedPart.addBodyPart(sourcePart);
	    			}

	    			{
	    		    	MimeBodyPart imagePart = new MimeBodyPart();
	    		    	DataSource source = new URLDataSource(getClass().getResource("/META-INF/resources/images/zauberer.png"));
	    		    	imagePart.setDataHandler(new DataHandler(source));
	    		    	imagePart.setHeader("Content-Disposition", "inline");
	    		    	imagePart.setHeader("Content-Type", "image/png");
	    		    	imagePart.setFileName("zauberer.png"); 
	    		    	imagePart.setContentID(imageId);
	    		    	relatedPart.addBodyPart(imagePart);		
	    			}
	    			htmlPart.setContent(relatedPart);
	    		}
	    		alternativePart.addBodyPart(htmlPart);
	    	}
	    	
	    	MimeBodyPart text = new MimeBodyPart();
	    	text.setText(read("/templates/mail-template.txt", link, code, image), "utf-8");
	    	alternativePart.addBodyPart(text);
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

	/** 
	 * TODO
	 * @throws MessagingException 
	 * @throws NoSuchProviderException 
	 *
	 */
	public void startUp() throws NoSuchProviderException, MessagingException {
		_session = startSession();
		_transport = _session.getTransport();
		_transport.connect(_user, _password);
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
	
	/** 
	 * TODO
	 *
	 */
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

	private String read(String resource, String link, String code, String image) throws IOException {
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
