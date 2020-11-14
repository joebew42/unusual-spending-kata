package org.unusualspending;

import java.util.List;

import static org.unusualspending.Payments.groupBySpendings;

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
        evaluate(user, paymentsRepository.currentMonth(user), paymentsRepository.lastMonth(user));
    }

    public void evaluate(String user, List<Payment> currentMonthPayments, List<Payment> lastMonthPayments) {
        List<Spending> unusualSpendings = spendings.thatAreAtLeast50PercentMoreThan(groupBySpendings(lastMonthPayments), groupBySpendings(currentMonthPayments));

        if (unusualSpendings.isEmpty()) {
            return;
        }

        alertSystem.send(new Notification(user, unusualSpendings));
    }
}
