package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.unusualspending.Payments.groupedBySpending;
import static org.unusualspending.Spendings.findSpending;

public class UnusualSpending {
    private final Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluateByPayments(String user, List<Payment> payments, List<Payment> paymentsOfTheLastMonth) {
        evaluateBySpendings(groupedBySpending(payments), groupedBySpending(paymentsOfTheLastMonth));
    }

    private void evaluateBySpendings(List<Spending> spendings, List<Spending> pastSpendings) {
        List<Spending> unusualSpendings = allSpendingsThatAreAtLeastThe50percentMoreThan(pastSpendings, spendings);
        if (!unusualSpendings.isEmpty()) {
            notifier.notifyFor(unusualSpendings);
        }
    }

    private List<Spending> allSpendingsThatAreAtLeastThe50percentMoreThan(List<Spending> pastSpendings, List<Spending> spendings) {
        List<Spending> unusualSpendings = new ArrayList<>();
        for (Spending actualSpending : spendings) {
            Optional<Spending> pastSpending = findSpending(actualSpending.name(), pastSpendings);
            if (pastSpending.isPresent() && actualSpending.isAtLeast50percentMoreThan(pastSpending.get())) {
                unusualSpendings.add(actualSpending);
            }
        }
        return unusualSpendings;
    }
}
