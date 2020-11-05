package org.unusualspending;

public class UnusualSpending {
    private Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluate(int currentAmount, int previousAmount) {
        if (isAtLeast50percentMoreThan(previousAmount, currentAmount)) {
            notifier.send();
        }
    }

    private boolean isAtLeast50percentMoreThan(int amount, int amountToCompare) {
        return amountToCompare >= amount + amount / 2;
    }
}
