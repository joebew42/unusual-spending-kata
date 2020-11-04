package org.unusualspending;

import org.junit.Test;

import static java.lang.Boolean.FALSE;
import static org.junit.Assert.assertFalse;

public class UnusualSpendingTest {
    @Test
    public void do_not_trigger_the_alarm_when_the_current_amount_is_not_the_50_percent_more_of_the_previous_one() {
        Boolean hasBeenTriggered = FALSE;
        Alarm alarm = new SpyAlarm(hasBeenTriggered);

        new UnusualSpending(alarm).evaluate(2, 2);

        assertFalse(hasBeenTriggered);
    }

    private class SpyAlarm implements Alarm {
        private Boolean hasBeenTriggered;

        public SpyAlarm(Boolean hasBeenTriggered) {
            this.hasBeenTriggered = hasBeenTriggered;
        }

    }
}
