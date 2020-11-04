package org.unusualspending;

public class UnusualSpending {
    private Alarm alarm;

    public UnusualSpending(Alarm alarm) {
        this.alarm = alarm;
    }

    public void evaluate(int currentAmount, int previousAmount) {
        if (currentAmount >= previousAmount + previousAmount / 2) {
            alarm.sendNotification();
        }
    }
}
