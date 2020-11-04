package org.unusualspending;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnusualSpendingTest {
    @Test
    public void do_not_triggers_the_alarm_when_the_current_amount_is_not_the_50_percent_more_of_the_previous_one() {
        Trigger trigger = new Trigger();
        Alarm alarm = new SpyAlarm(trigger);

        new UnusualSpending(alarm).evaluate(2, 2);

        assertFalse(trigger.hasBeenTriggered());
    }

    @Test
    public void triggers_the_alarm_when_the_current_amount_is_at_least_the_50_percent_more_of_the_previous_one() {
        Trigger trigger = new Trigger();
        Alarm alarm = new SpyAlarm(trigger);

        new UnusualSpending(alarm).evaluate(3, 2);

        assertTrue(trigger.hasBeenTriggered());
    }

    private class SpyAlarm implements Alarm {
        private Trigger trigger;

        public SpyAlarm(Trigger trigger) {
            this.trigger = trigger;
        }

        @Override
        public void sendNotification() {
            trigger.turnOn();
        }
    }

    private class Trigger {
        private boolean triggered = false;

        public boolean hasBeenTriggered() {
            return triggered;
        }

        public void turnOn() {
            triggered = true;
        }
    }
}
