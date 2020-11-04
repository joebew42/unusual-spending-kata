package org.unusualspending;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnusualSpendingTest {

    private Trigger trigger;
    private UnusualSpending unusualSpending;

    @Before
    public void setUp() {
        trigger = new Trigger();
        unusualSpending = new UnusualSpending(new SpyAlarm(trigger));
    }

    @Test
    public void do_not_triggers_the_alarm_when_the_current_amount_is_not_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(2, 2);

        assertFalse(trigger.hasBeenTriggered());
    }

    @Test
    public void triggers_the_alarm_when_the_current_amount_is_at_least_the_50_percent_more_of_the_previous_one() {
        unusualSpending.evaluate(3, 2);

        assertTrue(trigger.hasBeenTriggered());
    }

    private static class SpyAlarm implements Alarm {
        private final Trigger trigger;

        public SpyAlarm(Trigger trigger) {
            this.trigger = trigger;
        }

        @Override
        public void sendNotification() {
            trigger.turnOn();
        }
    }

    private static class Trigger {
        private boolean triggered = false;

        public boolean hasBeenTriggered() {
            return triggered;
        }

        public void turnOn() {
            triggered = true;
        }
    }
}
