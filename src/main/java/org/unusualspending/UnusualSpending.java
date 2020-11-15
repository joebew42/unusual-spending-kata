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

    public void evaluate(User user) {
        List<Payment> currentMonthPayments = paymentsRepository.currentMonth(user.userName());
        List<Payment> lastMonthPayments = paymentsRepository.lastMonth(user.userName());

        Spendings unusualSpendings = spendingsFrom(currentMonthPayments).atLeast50PercentMoreThan(spendingsFrom(lastMonthPayments));

        if (unusualSpendings.hasNoSpendings()) {
            return;
        }

        alertSystem.send(new Notification(user.userName(), unusualSpendings));
    }

}
