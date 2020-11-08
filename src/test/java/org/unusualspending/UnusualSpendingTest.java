package org.unusualspending;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnusualSpendingTest {

    private Probe<List<Spending>> probe;
    private UnusualSpending unusualSpending;

    @Before
    public void setUp() {
        probe = new Probe<>();
        unusualSpending = new UnusualSpending(new SpyNotifier(probe));
    }

    @Test
    public void do_not_send_any_notification_when_payments_by_spending_are_not_the_50_percent_more_of_the_past_ones() {
        List<Payment> payments = asList(
                new Payment(2, "golf", "Playing Golf with friends"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(1, "entertainment", "Popcorn"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(3, "entertainment", "Movie Theater")
        );

        List<Payment> paymentsOfTheLastMonth = asList(
                new Payment(2, "golf", "Playing Golf with friends"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(3, "entertainment", "Movie Theater")
        );

        unusualSpending.evaluateByPayments(payments, paymentsOfTheLastMonth);

        assertTrue(probe.hasNotBeenCalled());
    }

    @Test
    public void send_notification_when_payments_by_spending_are_the_50_percent_more_of_the_past_ones() {
        List<Payment> payments = asList(
                new Payment(2, "golf", "Playing Golf with friends"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(5, "gardening", "Flowers")
        );

        List<Payment> paymentsOfTheLastMonth = asList(
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(1, "golf", "Drink at Golf court"),
                new Payment(1, "entertainment", "Popcorn"),
                new Payment(3, "entertainment", "Movie Theater"),
                new Payment(5, "gardening", "Flowers")
        );

        unusualSpending.evaluateByPayments(payments, paymentsOfTheLastMonth);

        assertTrue(probe.hasBeenCalledWith(asList(
                new Spending(3, "golf"),
                new Spending(6, "entertainment")
        )));
    }

    @Test
    public void do_not_send_any_notification_when_spendings_are_not_the_50_percent_more_of_the_past_ones() {
        List<Spending> spendings = asList(
                new Spending(3, "golf"),
                new Spending(7, "entertainment")
        );

        List<Spending> pastSpendings = asList(
                new Spending(3, "golf"),
                new Spending(6, "entertainment")
        );

        unusualSpending.evaluateBySpendings(spendings, pastSpendings);

        assertTrue(probe.hasNotBeenCalled());
    }

    @Test
    public void send_notification_for_the_spendings_that_are_the_50_percent_more_of_the_past_ones() {
        List<Spending> spendings = asList(
                new Spending(3, "golf"),
                new Spending(6, "entertainment"),
                new Spending(5, "gardening")
        );

        List<Spending> pastSpendings = asList(
                new Spending(2, "golf"),
                new Spending(4, "entertainment"),
                new Spending(5, "gardening")
        );

        unusualSpending.evaluateBySpendings(spendings, pastSpendings);

        assertTrue(probe.hasBeenCalledWith(asList(
                new Spending(3, "golf"),
                new Spending(6, "entertainment")
        )));
    }

    private static class SpyNotifier implements Notifier {
        private final Probe<List<Spending>> probe;

        public SpyNotifier(Probe<List<Spending>> probe) {
            this.probe = probe;
        }

        @Override
        public void notifyFor(List<Spending> spendings) {
            probe.callWith(spendings);
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
