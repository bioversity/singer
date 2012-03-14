package org.sgrp.singer.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;

public class MailUtils {

	// public String host = "singer.cgiar.org";
	/*
	 * public void sendMail(String from, String recpTo, String recpCc, String subject, String messageTxt, String messageHtml, String attachment) throws MessagingException { sendMail(from, new String[] {
	 * recpTo }, new String[] { recpCc }, subject, messageTxt, messageHtml, new String[] { attachment }); }
	 */
	public static boolean sendOrderMail(String recpTo[], String subject, String messageTxt, String messageHtml, String attachment) throws MessagingException {
		return sendOrderMail(recpTo, subject, messageTxt, messageHtml, new String[] { attachment });
	}

	public static boolean sendOrderMail(String recpTo, String subject, String messageHtml) throws MessagingException {
		return sendOrderMail(recpTo, subject, null, messageHtml, null);
	}
	
	public static boolean sendOrderMail(String[] recpTo, String subject, String messageHtml) throws MessagingException {
		return sendOrderMail(recpTo, subject, null, messageHtml, (String[])null);
	}

	public static boolean sendOrderMail(String recpTo, String subject, String messageTxt, String messageHtml, String attachments[]) throws MessagingException {
		return sendOrderMail(new String[] { recpTo }, subject, messageTxt, messageHtml, attachments);
	}

	public static boolean sendOrderMail(String recpTo[], String subject, String messageTxt, String messageHtml, String attachments[]) throws MessagingException {
		String host = ResourceManager.getString(AccessionConstants.ORDER_MAIL_HOST);
		String port = ResourceManager.getString(AccessionConstants.ORDER_MAIL_PORT);
		String from = ResourceManager.getString(AccessionConstants.ORDER_MAIL_FROM);
		String rCc = ResourceManager.getString(AccessionConstants.ORDER_MAIL_CC);
		String recpCc[] =null;
		if(rCc!=null && rCc.trim().length()>0)
		{
			recpCc = new String[]{rCc};
		}
		//System.out.println("Message: "+messageTxt);
		return sendMail(host, port, from, recpTo, recpCc, subject, messageTxt, messageHtml, attachments);
	}

	public static boolean sendMail(String host, String port, String from, String recpTo[], String recpCc[], String subject, String messageTxt, String messageHtml, String attachments[]) throws MessagingException {
		boolean emailSent = false;
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		if (port == null || (port != null && port.trim().length() == 0)) {
			port = "25";
		}
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props);
		//session.setDebug(true);
		try {
			if (recpTo != null && recpTo.length > 0) {
				Transport bus = session.getTransport("smtp");
				bus.connect();
				Message msg = new MimeMessage(session);
				// From
				InternetAddress addFrom = new InternetAddress(from);
				msg.setFrom(addFrom);

				InternetAddress[] addTo = null;
				String blockAdd = ResourceManager.getString(AccessionConstants.MAIL_BLOCK_REDIRECT);
				if (blockAdd != null && blockAdd.trim().length() > 0) {
					addTo = new InternetAddress[1];
					addTo[0] = new InternetAddress(blockAdd);
					subject = "HIJACKED - "+subject;
				} else {
					// To
					addTo = new InternetAddress[recpTo.length];
					for (int i = 0; i < recpTo.length; i++) {
						addTo[i] = new InternetAddress(recpTo[i]);
					}
				}
				msg.setRecipients(Message.RecipientType.TO, addTo);
				// Cc
				if (recpCc != null && recpCc.length > 0) {
					InternetAddress[] addCc = new InternetAddress[recpCc.length];
					for (int i = 0; i < recpCc.length; i++) {
						addCc[i] = new InternetAddress(recpCc[i]);
					}
					msg.setRecipients(Message.RecipientType.CC, addCc);
				}
				String rBcc = ResourceManager.getString(AccessionConstants.ORDER_MAIL_BCC);
				if(rBcc!=null && rBcc.trim().length()>0)
				{
				String recpBcc[] = new String[]{rBcc};
				// BCc
				if (recpBcc != null && recpBcc.length > 0) {
					InternetAddress[] addBcc = new InternetAddress[recpBcc.length];
					for (int i = 0; i < recpBcc.length; i++) {
						addBcc[i] = new InternetAddress(recpBcc[i]);
					}
					msg.setRecipients(Message.RecipientType.BCC, addBcc);
				}
				}
				// Subject
				msg.setSubject(subject);
				msg.setSentDate(new Date());
				// Set message content and send
				if (messageTxt != null && messageTxt.trim().length() > 0) {
					setTextContent(msg, messageTxt);
					msg.saveChanges();
					//bus.sendMessage(msg, addTo);
				}

				if (attachments != null && attachments.length > 0) {
					setFileAsAttachment(msg, attachments);
					msg.saveChanges();
					//bus.sendMessage(msg, addTo);
				}

				if (messageHtml != null && messageHtml.trim().length() > 0) {
					setHTMLContent(msg, messageHtml);
					msg.saveChanges();
					//bus.sendMessage(msg, addTo);
				}
				bus.sendMessage(msg, addTo);
				bus.close();
				emailSent = true;
			} else {
				throw new MessagingException("To Recipients address found null");
			}
		} catch (MessagingException mex) {
			mex.printStackTrace();
			while (mex.getNextException() != null) {
				Exception ex = mex.getNextException();
				ex.printStackTrace();
				if (!(ex instanceof MessagingException))
					break;
				else
					mex = (MessagingException) ex;
			}
		}
		return emailSent;
	}

	public static void setTextContent(Message msg, String message) throws MessagingException {
		msg.setText(message);
		msg.setContent(message, "text/plain");

	}

	public static void setFileAsAttachment(Message msg, String attachments[]) throws MessagingException {

		Multipart mp = new MimeMultipart();
		for (int i = 0; i < attachments.length; i++) {
			String attachment = attachments[i];
			MimeBodyPart fpart = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(attachment);
			fpart.setDataHandler(new DataHandler(fds));
			fpart.setFileName(fds.getName());
			mp.addBodyPart(fpart);
		}
		msg.setContent(mp);
	}

	public static void setHTMLContent(Message msg, String message) throws MessagingException {
		msg.setDataHandler(new DataHandler(new HTMLDataSource(message)));
	}

	static class HTMLDataSource implements DataSource {
		private String	html;

		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}

		public InputStream getInputStream() throws IOException {
			if (html == null)
				throw new IOException("Null HTML");
			return new ByteArrayInputStream(html.getBytes());
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}

		public String getContentType() {
			return "text/html";
		}

		public String getName() {
			return "DataSource to send Html e-mail only";
		}
	}
}
