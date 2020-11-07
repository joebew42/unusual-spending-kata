package org.unusualspending;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        unusualSpending.evaluate(new Category(2), new Category(2));

        assertFalse(probe.hasBeenCalledWith(new Category(2)));
    }

    @Test
    public void call_the_notifier_when_the_current_amount_is_at_least_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(new Category(2), new Category(3));

        assertTrue(probe.hasBeenCalledWith(new Category(3)));
    }

    private static class SpyNotifier implements Notifier {
        private final Probe probe;

        public SpyNotifier(Probe probe) {
            this.probe = probe;
        }

        @Override
        public void notifyFor(Category category) {
            probe.callWith(category);
        }
    }

    private static class Probe {
        private Category calledWith = null;

        public void callWith(Category category) {
            calledWith = category;
        }

        public boolean hasBeenCalledWith(Category category) {
            if (calledWith == null) {
                return false;
            }

            assertEquals(category, calledWith);
            return true;
        }
    }
}
