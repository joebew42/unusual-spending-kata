package org.unusualspending;

import java.util.List;

import static org.unusualspending.Spendings.spendingsFrom;

public class UnusualSpending {
    private final AlertSystem alertSystem;
    private final Payments payments;

    public UnusualSpending(Payments payments, AlertSystem alertSystem) {
        this.payments = payments;
        this.alertSystem = alertSystem;
    }

    public void evaluate(User user) {
        List<Payment> currentMonthPayments = payments.currentMonth(user.userName());
        List<Payment> lastMonthPayments = payments.lastMonth(user.userName());

        Spendings unusualSpendings = spendingsFrom(currentMonthPayments).atLeast50PercentMoreThan(spendingsFrom(lastMonthPayments));

        if (unusualSpendings.hasNoSpendings()) {
            return;
        }

        alertSystem.send(new Notification(user, unusualSpendings));
    }

}
