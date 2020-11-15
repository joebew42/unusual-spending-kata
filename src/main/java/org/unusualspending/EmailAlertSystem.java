package org.unusualspending;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static java.lang.String.format;

class EmailAlertSystem implements AlertSystem {
    private final Session smtpSession;

    public EmailAlertSystem(Session smtpSession) {
        this.smtpSession = smtpSession;
    }

    @Override
    public void send(Notification notification) {
        EmailAlertMessage emailAlertMessage = new EmailAlertMessage(notification);
        try {
            sendEmail(emailAlertMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(EmailAlertMessage alertMessage) throws MessagingException {
        Message message = new MimeMessage(smtpSession);
        message.setFrom(new InternetAddress("foo@example.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("bar@example.com"));
        message.setSubject(alertMessage.subject());
        message.setText(alertMessage.text());
        Transport.send(message);
    }

}
