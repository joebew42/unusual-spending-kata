package org.unusualspending;

public class UnusualSpending {
    private final AlertSystem alertSystem;
    private final Payments payments;

    public UnusualSpending(Payments payments, AlertSystem alertSystem) {
        this.payments = payments;
        this.alertSystem = alertSystem;
    }

    public void evaluate(User user) {
        Spendings currentMonthSpendings = Spendings.from(payments.ofCurrentMonth(user.userName()));
        Spendings lastMonthSpendings = Spendings.from(payments.ofLastMonth(user.userName()));

        Spendings unusualSpendings = currentMonthSpendings.thatAreAtLeast50PercentMoreThan(lastMonthSpendings);

        if (unusualSpendings.isEmpty()) {
            return;
        }

        alertSystem.send(new Notification(user, unusualSpendings));
    }

}
