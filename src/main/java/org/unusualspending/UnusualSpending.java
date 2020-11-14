package org.unusualspending;

import java.util.List;

import static org.unusualspending.Spendings.fromPayments;

public class UnusualSpending {
    private final AlertSystem alertSystem;
    private final Spendings spendings;
    private final PaymentsRepository paymentsRepository;

    public UnusualSpending(PaymentsRepository paymentsRepository, AlertSystem alertSystem, Spendings spendings) {
        this.paymentsRepository = paymentsRepository;
        this.alertSystem = alertSystem;
        this.spendings = spendings;
    }

    public void evaluate(String user) {
        List<Payment> currentMonthPayments = paymentsRepository.currentMonth(user);
        List<Payment> lastMonthPayments = paymentsRepository.lastMonth(user);

        List<Spending> unusualSpendings = spendings.thatAreAtLeast50PercentMoreThan(fromPayments(lastMonthPayments), fromPayments(currentMonthPayments));

        if (unusualSpendings.isEmpty()) {
            return;
        }

        alertSystem.send(new Notification(user, unusualSpendings));
    }

}
