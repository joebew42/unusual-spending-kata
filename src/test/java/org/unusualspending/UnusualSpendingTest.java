package org.unusualspending;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnusualSpendingTest {

    private Probe<Notification> probe;

    @Before
    public void setUp() {
        probe = new Probe<>();
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

        UnusualSpending unusualSpending = new UnusualSpending(new SpyAlertSystem(probe));

        unusualSpending.evaluate("AnyUser", currentMonthPayments, lastMonthPayments);

        assertTrue(probe.hasNotBeenCalled());
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

        UnusualSpending unusualSpending = new UnusualSpending(new SpyAlertSystem(probe));

        unusualSpending.evaluate("AnyUser", currentMonthPayments, lastMonthPayments);

        Notification notification = new Notification("AnyUser", asList(
                new Spending(3, "golf"),
                new Spending(6, "entertainment")
        ));

        assertTrue(probe.hasBeenCalledWith(notification));
    }

    private static class SpyAlertSystem implements AlertSystem {
        private final Probe<Notification> probe;

        public SpyAlertSystem(Probe<Notification> probe) {
            this.probe = probe;
        }

        @Override
        public void send(Notification notification) {
            probe.callWith(notification);
        }
    }

    private static class Probe<T> {
        private T calledWith = null;

        public void callWith(T argument) {
            calledWith = argument;
        }

        public boolean hasBeenCalledWith(T argument) {
            if (calledWith == null) {
                return false;
            }

            assertEquals(argument, calledWith);
            return true;
        }

        public boolean hasNotBeenCalled() {
            return calledWith == null;
        }
    }
}
