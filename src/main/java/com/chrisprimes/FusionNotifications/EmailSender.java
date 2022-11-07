package com.chrisprimes.FusionNotifications;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	private String fromAddress;
	private String smtpServer;

	public EmailSender(String fromAddress, String smtpServer) {
		this.fromAddress = fromAddress;
		this.smtpServer = smtpServer;
	}

	public void sendEmailMessage(String recipient, String subject, String messageContent) throws MessagingException {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", smtpServer);

		Session session = Session.getDefaultInstance(properties);

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromAddress));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		message.setSubject(subject);

		message.setContent(messageContent, "text/html");

		Transport.send(message);
	}
}