package org.unusualspending;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

public class UnusualSpendingTest {

    private GreenMail mailServer;

    @Before
    public void setUp() {
        mailServer = new GreenMail(ServerSetupTest.ALL);
        mailServer.start();
    }

    @After
    public void tearDown() {
        mailServer.stop();
    }

    @Test
    public void do_not_send_any_notification_when_payments_by_spending_are_not_the_50_percent_more_of_the_past_ones() {
        List<Payment> currentMonthPayments = asList(
                new Payment(2, "golf", "Playing Golf with friends"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(1, "entertainment", "Popcorn"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(3, "entertainment", "Movie Theater")
        );

        List<Payment> lastMonthPayments = asList(
                new Payment(2, "golf", "Playing Golf with friends"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(3, "entertainment", "Movie Theater")
        );

        PaymentsRepository paymentsRepository = new InMemoryPaymentsRepository("AnyUser", currentMonthPayments, lastMonthPayments);
        UnusualSpending unusualSpending = new UnusualSpending(paymentsRepository, new SpyAlertSystem(mailServer.getSmtp().createSession()));

        unusualSpending.evaluate("AnyUser");

        assertNoNotificationSent();
    }

    @Test
    public void send_notification_when_payments_by_spending_are_the_50_percent_more_of_the_past_ones() {
        List<Payment> currentMonthPayments = asList(
                new Payment(2, "golf", "Playing Golf with friends"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(5, "gardening", "Flowers")
        );

        List<Payment> lastMonthPayments = asList(
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(1, "entertainment", "Popcorn"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(5, "gardening", "Flowers")
        );

        PaymentsRepository paymentsRepository = new InMemoryPaymentsRepository("AnyUser", currentMonthPayments, lastMonthPayments);
        UnusualSpending unusualSpending = new UnusualSpending(paymentsRepository, new SpyAlertSystem(mailServer.getSmtp().createSession()));

        unusualSpending.evaluate("AnyUser");

        Notification notification = new Notification("AnyUser", new Spendings(
                new Spending(3, "golf"),
                new Spending(6, "entertainment")
        ));

        assertNotificationSent(notification);
    }

    private void assertNoNotificationSent() {
        assertEquals(0, mailServer.getReceivedMessagesForDomain("bar@example.com").length);
    }

    private void assertNotificationSent(Notification notification) {
        MimeMessage message = mailServer.getReceivedMessagesForDomain("bar@example.com")[0];

        List<Spending> spendings = notification.allSpendings();
        for (Spending ignored : spendings) {
            MatcherAssert.assertThat(content(message), containsString("Fetch me via IMAP"));
        }
    }

    private String content(MimeMessage message){
        try {
            return (String) message.getContent();
        } catch (IOException | MessagingException e) {
            return "";
        }
    }

    private static class SpyAlertSystem implements AlertSystem {
        private final Session smtpSession;

        public SpyAlertSystem(Session smtpSession) {
            this.smtpSession = smtpSession;
        }

        @Override
        public void send(Notification notification) {
            try {
                sendEMail();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        private void sendEMail() throws MessagingException {
            Message msg = new MimeMessage(smtpSession);
            msg.setFrom(new InternetAddress("foo@example.com"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("bar@example.com"));
            msg.setSubject("Email sent to GreenMail via plain JavaMail");
            msg.setText("Fetch me via IMAP");
            Transport.send(msg);
        }
    }

    public static class InMemoryPaymentsRepository implements PaymentsRepository {
        private final HashMap<String, List<Payment>> currentMonthPayments = new HashMap<>();
        private final HashMap<String, List<Payment>> lastMonthPayments = new HashMap<>();

        public InMemoryPaymentsRepository(String user, List<Payment> currentMonth, List<Payment> lastMonth) {
            currentMonthPayments.put(user, currentMonth);
            lastMonthPayments.put(user, lastMonth);
        }

        @Override
        public List<Payment> currentMonth(String user) {
            return currentMonthPayments.get(user);
        }

        @Override
        public List<Payment> lastMonth(String user) {
            return lastMonthPayments.get(user);
        }
    }
}
