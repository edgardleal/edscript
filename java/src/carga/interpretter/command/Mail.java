/**
 * 
 */
package carga.interpretter.command;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import carga.interpretter.*;

/**
 * @author edgardleal
 *
 */
public class Mail extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see carga.interpretter.command.Command#execute(carga.interpretter.Scoop,
	 * java.lang.String[])
	 */
	@Override
	public Scoop execute(Scoop _scoop, String... args) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		final String user = scoope.get("mail.user").toString();
		final String pass = scoope.get("mail.pass").toString();
		String from = scoope.get("mail.from").toString();
		String to = this.scoope.parse(this.arg[0]);
		String subject = this.scoope.parse(this.arg[1]);
		String text = scoope.parse(this.arg[2]);

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
						return new javax.mail.PasswordAuthentication(user, pass);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
