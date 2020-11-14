package org.unusualspending;

import java.util.List;

import static org.unusualspending.Spendings.spendingsFrom;

public class UnusualSpending {
    private final AlertSystem alertSystem;
    private final PaymentsRepository paymentsRepository;

    public UnusualSpending(PaymentsRepository paymentsRepository, AlertSystem alertSystem) {
        this.paymentsRepository = paymentsRepository;
        this.alertSystem = alertSystem;
    }

    public void evaluate(String user) {
        List<Payment> currentMonthPayments = paymentsRepository.currentMonth(user);
        List<Payment> lastMonthPayments = paymentsRepository.lastMonth(user);

        List<Spending> unusualSpendings = spendingsFrom(currentMonthPayments).atLeast50PercentMoreThan(spendingsFrom(lastMonthPayments));

        if (unusualSpendings.isEmpty()) {
            return;
        }

        alertSystem.send(new Notification(user, unusualSpendings));
    }

}
