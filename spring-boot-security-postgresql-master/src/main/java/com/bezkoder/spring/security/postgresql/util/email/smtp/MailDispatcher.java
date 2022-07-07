package com.bezkoder.spring.security.postgresql.util.email.smtp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailDispatcher extends Thread {

	private String hostName;
	private String port;
	private String password;
	private String webMail;
	private String fromAddress;
	private String contentType;

	private String subject;
	private String content;
	private List<String> attachment = new ArrayList<String>();;
	private List<String> toAddress = new ArrayList<String>();;
	private List<String> bcc = new ArrayList<String>();
	private List<String> cc = new ArrayList<String>();

	public MailDispatcher(SmtpEmailDTO emailDTO) {
		this.hostName = emailDTO.getHostName();
		this.port = emailDTO.getPort();
		this.fromAddress = emailDTO.getFromAddress();
		this.password = emailDTO.getPassword();
		this.contentType = emailDTO.getContentType();
		this.subject = emailDTO.getSubject();
		this.content = emailDTO.getContent();
		this.attachment = emailDTO.getAttachment();
		this.webMail = emailDTO.getWebMail();
		this.toAddress = emailDTO.getToAddress();
		this.bcc = emailDTO.getBcc();
		this.cc = emailDTO.getCc();
	}

	public void run() {
		// log.info("----------------Mail sending started-------------");
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", hostName);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.socketFactory.port", port); // SSL Port
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.starttls.enable", "true"); // enable false for web mail

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromAddress, password);
				}
			};

			Session mailSession = Session.getDefaultInstance(props, auth);
			mailSession.setDebug(true);

			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);

			if (!attachment.isEmpty()) {
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(content, contentType);
				multipart.addBodyPart(messageBodyPart);
				for (String obj : attachment) {
					messageBodyPart = new MimeBodyPart();
					String filename = obj;

					DataSource source = new FileDataSource(filename);
					messageBodyPart.setDataHandler(new DataHandler(source));

					messageBodyPart.setFileName(source.getName());
					multipart.addBodyPart(messageBodyPart);
				}
				message.setContent(multipart);
			} else {
				message.setContent(content, contentType);
			}
			message.setHeader("Return-Receipt-To", "siya@sayonetech.com");
			message.setHeader("Disposition-Notification-To", "siya@sayonetech.com");
			message.setSubject(subject);
			message.setFrom(new InternetAddress(fromAddress));
			InternetAddress[] bccAddress = new InternetAddress[bcc.size()];
			InternetAddress[] ccAddress = new InternetAddress[cc.size()];
			InternetAddress[] toAdd = new InternetAddress[toAddress.size()];

			for (int i = 0; i < bcc.size(); i++) {
				bccAddress[i] = new InternetAddress(bcc.get(i));
			}

			for (int i = 0; i < bccAddress.length; i++) {
				message.addRecipient(Message.RecipientType.BCC, bccAddress[i]);
			}

			for (int j = 0; j < cc.size(); j++) {
				ccAddress[j] = new InternetAddress(cc.get(j));
			}

			for (int i = 0; i < ccAddress.length; i++) {
				message.addRecipient(Message.RecipientType.CC, ccAddress[i]);
			}

			for (int j = 0; j < toAddress.size(); j++) {
				toAdd[j] = new InternetAddress(toAddress.get(j));
			}

			for (int i = 0; i < toAdd.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAdd[i]);
			}

			transport.connect();

			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (MessagingException me) {
			me.printStackTrace();
		}

	}
}
