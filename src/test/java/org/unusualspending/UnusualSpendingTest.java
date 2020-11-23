package org.unusualspending;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.summarizingInt;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class UnusualSpendingTest {

    public static final User A_USER = new User("AnyUser", "anyUser@example.com");

    private GreenMail mailServer;
    private EmailAlertSystem alertSystem;

    @Before
    public void setUp() {
        mailServer = new GreenMail(ServerSetupTest.ALL);
        alertSystem = new EmailAlertSystem(mailServer.getSmtp().createSession());

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

        Payments payments = new PaymentsFor(A_USER.userName(), currentMonthPayments, lastMonthPayments);
        UnusualSpending unusualSpending = new UnusualSpending(payments, alertSystem);

        unusualSpending.evaluate(A_USER);

        assertNoNotificationSentTo(A_USER);
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

        Payments payments = new PaymentsFor(A_USER.userName(), currentMonthPayments, lastMonthPayments);
        UnusualSpending unusualSpending = new UnusualSpending(payments, alertSystem);

        unusualSpending.evaluate(A_USER);

        Notification notification = new Notification(A_USER, new Spendings(
                new Spending(3, "golf"),
                new Spending(6, "entertainment")
        ));

        assertNotificationSent(notification);
    }

    private void assertNoNotificationSentTo(User user) {
        assertEquals(0, numberOfReceivedMessagesFor(user.email()));
    }

    private void assertNotificationSent(Notification notification) {
        MimeMessage receivedMessage = readMessageFor(notification.userEmail());
        List<Spending> spendings = notification.allSpendings();

        assertThat(subjectOf(receivedMessage), is(format("Unusual spending of $%d detected!", total(spendings))));

        assertThat(contentOf(receivedMessage), startsWith("Hello card user!" +
                "\r\n\r\n" +
                "We have detected unusually high spending on your card in these categories:" +
                "\r\n\r\n"
        ));

        for (Spending spending : spendings) {
            assertThat(contentOf(receivedMessage), containsString(format("* You spent $%d on %s\r\n", spending.amount(), spending.name())));
        }

        assertThat(contentOf(receivedMessage), endsWith("\r\n" +
                "Love," +
                "\r\n\r\n" +
                "The Credit Card Company" +
                "\r\n"
        ));
    }

    private int numberOfReceivedMessagesFor(String email) {
        return mailServer.getReceivedMessagesForDomain(email).length;
    }

    private MimeMessage readMessageFor(String email) {
        return mailServer.getReceivedMessagesForDomain(email)[0];
    }

    private long total(List<Spending> spendings) {
        return spendings.stream()
                .collect(summarizingInt(Spending::amount))
                .getSum();
    }

    private String subjectOf(MimeMessage message) {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            return "";
        }
    }

    private String contentOf(MimeMessage message) {
        try {
            return (String) message.getContent();
        } catch (IOException | MessagingException e) {
            return "";
        }
    }

}
