package org.unusualspending;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UnusualSpendingTest {

    private Probe<List<Spending>> probe;
    private UnusualSpending unusualSpending;

    @Before
    public void setUp() {
        probe = new Probe<>();
        unusualSpending = new UnusualSpending(new SpyNotifier(probe));
    }

    @Test
    public void do_not_call_the_notifier_when_the_current_spending_is_not_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(new Spending(2, "golf"), new Spending(2, "golf"));

        assertFalse(probe.hasBeenCalledWith(List.of(new Spending(2, "golf"))));
    }

    @Test
    public void call_the_notifier_when_the_current_spending_is_at_least_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(new Spending(3, "golf"), new Spending(2, "golf"));

        assertTrue(probe.hasBeenCalledWith(List.of(new Spending(3, "golf"))));
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
    }
}
