package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UnusualSpending {
    private final Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluateByPayments(List<Payment> payments, List<Payment> paymentsOfTheLastMonth) {
        throw new RuntimeException("Not yet implemented");
    }

    public void evaluate(List<Spending> spendings, List<Spending> pastSpendings) {
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

    private Optional<Spending> findSpending(String name, List<Spending> spendings) {
        return spendings.stream()
                .filter(spending -> spending.name().equals(name))
                .findFirst();
    }
}
