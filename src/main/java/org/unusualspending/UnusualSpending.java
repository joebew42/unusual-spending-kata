package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.unusualspending.Payments.groupBySpendings;
import static org.unusualspending.Spendings.findSpending;

public class UnusualSpending {
    private final AlertSystem alertSystem;

    public UnusualSpending(AlertSystem alertSystem) {
        this.alertSystem = alertSystem;
    }

    public void evaluate(String user, List<Payment> currentMonthPayments, List<Payment> lastMonthPayments) {
        List<Spending> unusualSpendings = spendingsThatAreAtLeastThe50PercentMoreThan(groupBySpendings(lastMonthPayments), groupBySpendings(currentMonthPayments));

        if (unusualSpendings.isEmpty()) {
            return;
        }

        alertSystem.send(new Notification(user, unusualSpendings));
    }

    private List<Spending> spendingsThatAreAtLeastThe50PercentMoreThan(List<Spending> pastSpendings, List<Spending> spendings) {
        List<Spending> unusualSpendings = new ArrayList<>();
        for (Spending actualSpending : spendings) {
            Optional<Spending> pastSpending = findSpending(actualSpending.name(), pastSpendings);
            if (pastSpending.isPresent() && actualSpending.isAtLeast50PercentMoreThan(pastSpending.get())) {
                unusualSpendings.add(actualSpending);
            }
        }
        return unusualSpendings;
    }
}
