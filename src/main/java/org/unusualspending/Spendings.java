package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class Spendings {
    private List<Spending> spendings;

    public Spendings() {
        this(emptyList());
    }

    public Spendings(List<Spending> spendings) {
        this.spendings = spendings;
    }

    static Spendings spendingsFrom(List<Payment> currentMonthPayments) {
        List<Spending> spendings = currentMonthPayments.stream()
                .collect(Collectors.groupingBy(Payment::spending, Collectors.summingInt(Payment::price)))
                .entrySet().stream()
                .map(entry -> new Spending(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());

        return new Spendings(spendings);
    }

    public List<Spending> atLeast50PercentMoreThan(Spendings otherSpendings) {
        List<Spending> unusualSpendings = new ArrayList<>();
        for (Spending spending : all()) {
            Optional<Spending> pastSpending = otherSpendings.findSpending(spending.name());
            if (pastSpending.isPresent() && spending.isAtLeast50PercentMoreThan(pastSpending.get())) {
                unusualSpendings.add(spending);
            }
        }
        return unusualSpendings;
    }

    private List<Spending> all() {
        return List.copyOf(spendings);
    }

    private Optional<Spending> findSpending(String name) {
        return this.spendings.stream()
                .filter(spending -> spending.name().equals(name))
                .findFirst();
    }
}
