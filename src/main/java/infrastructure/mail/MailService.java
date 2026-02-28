package infrastructure.mail;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@ApplicationScoped
public class MailService {

	public void sendMail(String to, String subject, String text) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", System.getenv("MAIL_HOST"));
			props.put("mail.smtp.port", System.getenv("MAIL_PORT"));
			props.put("mail.smtp.auth", "false");
			props.put("mail.smtp.starttls.enable", "false");

			Session session = Session.getInstance(props);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(System.getenv().getOrDefault("MAIL_FROM", "no-reply@fx.com")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}