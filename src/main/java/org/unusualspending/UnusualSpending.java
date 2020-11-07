package org.unusualspending;

import java.util.List;

public class UnusualSpending {
    private final Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluate(Spending actual, Spending past) {
        if (isAtLeast50percentMoreThan(past.amount(), actual.amount())) {
            notifier.notifyFor(List.of(actual));
        }
    }

    private boolean isAtLeast50percentMoreThan(int amount, int amountToCompare) {
        return amountToCompare >= amount + amount / 2;
    }
}
