package org.unusualspending;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnusualSpendingTest {

    private Probe probe;
    private UnusualSpending unusualSpending;

    @Before
    public void setUp() {
        probe = new Probe();
        unusualSpending = new UnusualSpending(new SpyNotifier(probe));
    }

    @Test
    public void do_not_call_the_notifier_when_the_current_amount_is_not_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(2, 2);

        assertFalse(probe.hasBeenCalled());
    }

    @Test
    public void call_the_notifier_when_the_current_amount_is_at_least_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(3, 2);

        assertTrue(probe.hasBeenCalled());
    }

    private static class SpyNotifier implements Notifier {
        private final Probe probe;

        public SpyNotifier(Probe probe) {
            this.probe = probe;
        }

        @Override
        public void send() {
            probe.call();
        }
    }

    private static class Probe {
        private boolean called = false;

        public boolean hasBeenCalled() {
            return called;
        }

        public void call() {
            called = true;
        }
    }
}
