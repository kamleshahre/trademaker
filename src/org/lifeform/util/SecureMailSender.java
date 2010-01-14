package org.lifeform.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.lifeform.configuration.Defaults;

/**
 * Sends SSL Mail
 */
public class SecureMailSender {

	private static SecureMailSender instance;
	private final String host, user, password, subject, recipient;
	private Properties props = new Properties();

	// inner class
	private class Mailer extends Thread {
		final String content;

		Mailer(final String content) {
			this.content = content;
		}

		public void run() {
			try {
				Session mailSession = Session.getDefaultInstance(props);
				// mailSession.setDebug(true); // sends debugging info to
				// System.out

				MimeMessage message = new MimeMessage(mailSession);
				message.setSubject(subject);
				message.setContent(content, "text/plain");
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipient));

				Transport transport = mailSession.getTransport();
				transport.connect(host, user, password);
				transport.sendMessage(message, message
						.getRecipients(Message.RecipientType.TO));
				transport.close();

			} catch (Exception e) {
				// Dispatcher..getReporter().report(e);
			}

		}
	}

	public static synchronized SecureMailSender getInstance() throws Exception {
		if (instance == null) {
			instance = new SecureMailSender();
		}
		return instance;
	}

	// private constructor for noninstantiability
	private SecureMailSender() throws Exception {
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.auth", "true");
		props.put("mail.smtps.quitwait", "false");

		host = Defaults.get(Defaults.MailHost);
		user = Defaults.get(Defaults.MailUser);
		password = Defaults.get(Defaults.MailPassword);
		subject = Defaults.get(Defaults.MailSubject);
		recipient = Defaults.get(Defaults.MailRecipient);
	}

	public void send(final String content) {
		new Mailer(content).start();
	}

}
