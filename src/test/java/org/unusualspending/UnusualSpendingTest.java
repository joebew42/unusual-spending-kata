package org.unusualspending;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.summarizingInt;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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

        User user = new User("AnyUser");
        PaymentsRepository paymentsRepository = new InMemoryPaymentsRepository(user.userName(), currentMonthPayments, lastMonthPayments);
        UnusualSpending unusualSpending = new UnusualSpending(paymentsRepository, new EmailAlertSystem(mailServer.getSmtp().createSession()));

        unusualSpending.evaluate(new User("AnyUser"));

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

        User user = new User("AnyUser");
        PaymentsRepository paymentsRepository = new InMemoryPaymentsRepository(user.userName(), currentMonthPayments, lastMonthPayments);
        UnusualSpending unusualSpending = new UnusualSpending(paymentsRepository, new EmailAlertSystem(mailServer.getSmtp().createSession()));

        unusualSpending.evaluate(user);

        Notification notification = new Notification(user.userName(), new Spendings(
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

        assertThat(subject(message), is(format("Unusual spending of $%d detected!", total(spendings))));

        for (Spending spending : spendings) {
            assertThat(content(message), containsString(format("You spent $%d on %s", spending.amount(), spending.name())));
        }
    }

    private long total(List<Spending> spendings) {
        return spendings.stream()
                .collect(summarizingInt(Spending::amount))
                .getSum();
    }

    private String subject(MimeMessage message) {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            return "";
        }
    }

    private String content(MimeMessage message){
        try {
            return (String) message.getContent();
        } catch (IOException | MessagingException e) {
            return "";
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
