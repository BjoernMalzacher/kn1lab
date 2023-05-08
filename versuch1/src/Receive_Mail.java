import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Message;
import com.sun.mail.pop3.POP3Store;

import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Receive_Mail {
	public static void main(String[] args) throws Exception {
		fetchMail();
	}
	
	public static void fetchMail() {
		Properties props = new Properties();
		props.put("mail.pop3.host", "localhost");
		//props.put("mail.debug", "true");
		//props.put("mail.debug.quote", "true");
		props.put("mail.store.protocol", "pop3");
		Session session = Session.getInstance(props, null);

		try {
			Store store = session.getStore("pop3");
			store.connect("localhost", "labrat", "kn1lab");
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message[] messages = folder.getMessages();

			for (int i = 0; i<messages.length; i++) {
				System.out.println("Email Number " + i);
				System.out.println("Sender: " + messages[i].getFrom()[0]);
				System.out.println("Subject: " + messages[i].getSubject());
				System.out.println("Sent date: " + messages[i].getSentDate());
				System.out.println("Content: " + messages[i].getContent().toString());
			}


		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
