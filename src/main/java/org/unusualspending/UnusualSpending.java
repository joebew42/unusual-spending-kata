package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.unusualspending.Payments.groupBySpendings;
import static org.unusualspending.Spendings.findSpending;

public class UnusualSpending {
    private final Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluate(String user, List<Payment> payments, List<Payment> paymentsOfTheLastMonth) {
        List<Spending> unusualSpendings = spendingsThatAreAtLeastThe50PercentMoreThan(groupBySpendings(paymentsOfTheLastMonth), groupBySpendings(payments));

        if (unusualSpendings.isEmpty()) {
            return;
        }

        notifier.send(new Notification(user, unusualSpendings));
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
