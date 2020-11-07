package org.unusualspending;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnusualSpendingTest {

    private Probe<Spending> probe;
    private UnusualSpending unusualSpending;

    @Before
    public void setUp() {
        probe = new Probe<>();
        unusualSpending = new UnusualSpending(new SpyNotifier(probe));
    }

    @Test
    public void do_not_call_the_notifier_when_the_current_amount_is_not_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(new Spending(2, "golf"), new Spending(2, "golf"));

        assertFalse(probe.hasBeenCalledWith(new Spending(2, "golf")));
    }

    @Test
    public void call_the_notifier_when_the_current_amount_is_at_least_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(new Spending(2, "golf"), new Spending(3, "golf"));

        assertTrue(probe.hasBeenCalledWith(new Spending(3, "golf")));
    }

    private static class SpyNotifier implements Notifier {
        private final Probe<Spending> probe;

        public SpyNotifier(Probe<Spending> probe) {
            this.probe = probe;
        }

        @Override
        public void notifyFor(Spending spending) {
            probe.callWith(spending);
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
