package org.unusualspending;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.summarizingInt;

class EmailAlertSystem implements AlertSystem {
    private final Session smtpSession;

    public EmailAlertSystem(Session smtpSession) {
        this.smtpSession = smtpSession;
    }

    @Override
    public void send(Notification notification) {
        try {
            sendEMail(subjectFrom(notification), textFrom(notification));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String subjectFrom(Notification notification) {
        return format("Unusual spending of $%d detected!", total(notification.allSpendings()));
    }

    private long total(List<Spending> spendings) {
        return spendings.stream()
                .collect(summarizingInt(Spending::amount))
                .getSum();
    }

    private String textFrom(Notification notification) {
        String body = "";
        for (Spending spending : notification.allSpendings()) {
            body = format("You spent $%d on %s", spending.amount(), spending.name()).concat(body);
        }
        return body;
    }

    private void sendEMail(String subject, String text) throws MessagingException {
        Message message = new MimeMessage(smtpSession);
        message.setFrom(new InternetAddress("foo@example.com"));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress("bar@example.com"));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }
}
