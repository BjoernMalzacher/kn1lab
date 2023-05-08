import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 

public class Send_Mail {
	public static void main(String[] args) {
		sendMail();   
	}
	
	public static void sendMail() {
		Properties props = new Properties();
		props.put("mail.pop3.host", "localhost");
		props.put("mail.debug", "true");
		props.put("mail.debug.quote", "true");
		Session session = Session.getInstance(props, null);

		session.setDebug(true);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom("me@localhost");
			msg.setRecipients(Message.RecipientType.TO,
					"labrat@localhost");
			msg.setSubject("Servus!");
			msg.setSentDate(new Date());
			msg.setText("Wir lieben Kommunikationsnetze!\n");
			Transport.send(msg, "labrat@localhost", "kn1lab");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}