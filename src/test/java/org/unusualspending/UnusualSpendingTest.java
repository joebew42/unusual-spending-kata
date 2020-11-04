package org.unusualspending;

import org.junit.Test;

import static java.lang.Boolean.FALSE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnusualSpendingTest {
    @Test
    public void alarm_is_not_triggered_when_the_difference_between_two_amounts_is_less_then_50_percent() {
        Boolean hasBeenTriggered = FALSE;
        Alarm alarm = new SpyAlarm(hasBeenTriggered);

        new UnusualSpending(alarm).evaluate(10, 6);

        assertFalse(hasBeenTriggered);
    }

    private class SpyAlarm implements Alarm {
        private Boolean hasBeenTriggered;

        public SpyAlarm(Boolean hasBeenTriggered) {
            this.hasBeenTriggered = hasBeenTriggered;
        }
    }
}
