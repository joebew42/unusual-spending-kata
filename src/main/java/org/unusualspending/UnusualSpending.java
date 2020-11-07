package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UnusualSpending {
    private final Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluate(List<Spending> actualSpendings, List<Spending> pastSpendings) {
        List<Spending> spendings = allSpendingsThatAreAtLeastThe50percentMoreThan(pastSpendings, actualSpendings);
        if (!spendings.isEmpty()) {
            notifier.notifyFor(spendings);
        }
    }

    private List<Spending> allSpendingsThatAreAtLeastThe50percentMoreThan(List<Spending> pastSpendings, List<Spending> actualSpendings) {
        List<Spending> spendings = new ArrayList<>();
        for (Spending actualSpending : actualSpendings) {
            Optional<Spending> pastSpending = findSpending(actualSpending.name(), pastSpendings);
            if (pastSpending.isPresent() && isAtLeast50percentMoreThan(pastSpending.get(), actualSpending)) {
                spendings.add(actualSpending);
            }
        }
        return spendings;
    }

    private boolean isAtLeast50percentMoreThan(Spending past, Spending actual) {
        return actual.amount() >= past.amount() + past.amount() / 2;
    }

    private Optional<Spending> findSpending(String name, List<Spending> spendings) {
        return spendings.stream()
                .filter(spending -> spending.name().equals(name))
                .findFirst();
    }
}
