package com.bezkoder.spring.security.postgresql.util.email.smtp;

import java.util.List;

import javax.mail.MessagingException;

public class SMTPBroker {

	/**
	 * 
	 * @param content
	 * @param toAddress
	 * @param bcc
	 * @throws MessagingException
	 */
	@SuppressWarnings("unused")
	public void sendMessage(String port, String fromAddress, String hostName, String password, String contentType,
			String subject, String content, List<String> attachment, List<String> toAddress, List<String> bcc,
			List<String> cc) throws MessagingException {

		SmtpEmailDTO emailDTO = new SmtpEmailDTO();
		emailDTO.setHostName(hostName);
		emailDTO.setPort(port);
		emailDTO.setPassword(password);
		emailDTO.setFromAddress(fromAddress);
		// emailDTO.setWebMail(webMail);
		emailDTO.setContentType(contentType);
		emailDTO.setSubject(subject);
		emailDTO.setContent(content);
		emailDTO.setAttachment(attachment);
		emailDTO.setToAddress(toAddress);
		emailDTO.setBcc(bcc);
		emailDTO.setCc(cc);

		MailDispatcher dispatcher = new MailDispatcher(emailDTO);
		dispatcher.start();

	}

}
