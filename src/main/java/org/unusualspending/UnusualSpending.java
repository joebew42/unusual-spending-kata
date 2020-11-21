package org.unusualspending;

import java.util.List;

public class UnusualSpending {
    private final AlertSystem alertSystem;
    private final Payments payments;

    public UnusualSpending(Payments payments, AlertSystem alertSystem) {
        this.payments = payments;
        this.alertSystem = alertSystem;
    }

    public void evaluate(User user) {
        Spendings currentMonthSpendings = Spendings.from(payments.currentMonth(user.userName()));
        Spendings lastMonthSpendings = Spendings.from(payments.lastMonth(user.userName()));

        Spendings unusualSpendings = currentMonthSpendings.atLeast50PercentMoreThan(lastMonthSpendings);

        if (unusualSpendings.hasNoSpendings()) {
            return;
        }

        alertSystem.send(new Notification(user, unusualSpendings));
    }

}
