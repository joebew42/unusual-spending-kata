package org.unusualspending;

public class UnusualSpending {
    private Alarm alarm;

    public UnusualSpending(Alarm alarm) {
        this.alarm = alarm;
    }

    public void evaluate(int currentAmount, int previousAmount) {
        if (isAtLeast50percentMoreThan(previousAmount, currentAmount)) {
            alarm.sendNotification();
        }
    }

    private boolean isAtLeast50percentMoreThan(int amount, int amountToCompare) {
        return amountToCompare >= amount + amount / 2;
    }
}
