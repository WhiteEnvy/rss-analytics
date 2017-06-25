package rssToCsv.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import rssToCsv.CsvReader;
//import rssToCsv.EmailSender;
import rssToCsv.Models.EmailModel;

public class EmailSender {

	public static String UserName = "java.projet.idsm@gmail.com";
	public static String Password = "javaidsm";
	public static String Recipient = "";
	
	public static void getConfig() {
		if(FileManager.isFileOrDirectoryExists("config.csv")){
			List<String[]> items = CsvReader.parse("config.csv", "\t");
			Recipient = items.get(0)[0];
		}
	}

	private static Properties GetEmailServerProperties() {
		Properties emailServerProperties = new Properties();
		emailServerProperties.put("mail.smtp.auth", "true");
		emailServerProperties.put("mail.smtp.starttls.enable", "true");
		emailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
		emailServerProperties.put("mail.smtp.port", "587");

		return emailServerProperties;
	}

	private static Session GetSession() {
		return Session.getInstance(GetEmailServerProperties(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(UserName, Password);
			}
		});
	}

	public static void SendEmail(String subject, String text) {
		
		EmailModel email = new EmailModel();
		email.From = EmailSender.UserName;
		email.Recipients = EmailSender.Recipient;
		email.Subject = subject;
		email.Text = text;
		
		Session session = GetSession();
		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(email.From));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.Recipients));
			message.setSubject(email.Subject);
			message.setText(email.Text);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
