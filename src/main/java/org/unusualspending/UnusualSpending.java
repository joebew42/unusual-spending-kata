package org.unusualspending;

public class UnusualSpending {
    private Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluate(Spending previous, Spending current) {
        if (isAtLeast50percentMoreThan(previous.amount(), current.amount())) {
            notifier.notifyFor(current);
        }
    }

    private boolean isAtLeast50percentMoreThan(int amount, int amountToCompare) {
        return amountToCompare >= amount + amount / 2;
    }
}
