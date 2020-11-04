package org.unusualspending;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class UnusualSpendingTest {
    @Test
    public void do_not_trigger_the_alarm_when_the_current_amount_is_not_the_50_percent_more_of_the_previous_one() {
        Trigger trigger = new Trigger();
        Alarm alarm = new SpyAlarm(trigger);

        new UnusualSpending(alarm).evaluate(2, 2);

        assertFalse(trigger.hasBeenTriggered());
    }

    private class SpyAlarm implements Alarm {
        private Trigger trigger;

        public SpyAlarm(Trigger trigger) {
            this.trigger = trigger;
        }
    }

    private class Trigger {
        private boolean triggered = false;

        public boolean hasBeenTriggered() {
            return triggered;
        }
    }
}
