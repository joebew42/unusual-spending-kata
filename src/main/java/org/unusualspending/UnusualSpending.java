package org.unusualspending;

import static java.util.Arrays.asList;

public class UnusualSpending {
    private Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluate(Spending previous, Spending current) {
        if (isAtLeast50percentMoreThan(previous.amount(), current.amount())) {
            notifier.notifyFor(asList(current));
        }
    }

    private boolean isAtLeast50percentMoreThan(int amount, int amountToCompare) {
        return amountToCompare >= amount + amount / 2;
    }
}
