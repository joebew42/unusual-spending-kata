package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

public class UnusualSpending {
    private final Notifier notifier;

    public UnusualSpending(Notifier notifier) {
        this.notifier = notifier;
    }

    public void evaluateByPayments(List<Payment> payments, List<Payment> paymentsOfTheLastMonth) {
        evaluateBySpendings(groupedBySpending(payments), groupedBySpending(paymentsOfTheLastMonth));
    }

    public void evaluateBySpendings(List<Spending> spendings, List<Spending> pastSpendings) {
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

    private List<Spending> groupedBySpending(List<Payment> payments) {
        return payments.stream()
                .collect(groupingBy(Payment::spending, summingInt(Payment::price)))
                .entrySet().stream()
                .map(entry -> new Spending(entry.getValue(), entry.getKey()))
                .collect(toList());
    }

    private Optional<Spending> findSpending(String name, List<Spending> spendings) {
        return spendings.stream()
                .filter(spending -> spending.name().equals(name))
                .findFirst();
    }
}
