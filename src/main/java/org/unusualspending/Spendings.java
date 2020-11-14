package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.List.copyOf;

public class Spendings {
    private final List<Spending> spendings;

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

    public Spendings atLeast50PercentMoreThan(Spendings otherSpendings) {
        List<Spending> unusualSpendings = new ArrayList<>();
        for (Spending spending : all()) {
            Optional<Spending> pastSpending = otherSpendings.findSpending(spending.name());
            if (pastSpending.isPresent() && spending.isAtLeast50PercentMoreThan(pastSpending.get())) {
                unusualSpendings.add(spending);
            }
        }
        return new Spendings(unusualSpendings);
    }

    public List<Spending> all() {
        return copyOf(spendings);
    }

    boolean hasNoSpendings() {
        return all().isEmpty();
    }

    private Optional<Spending> findSpending(String name) {
        return this.spendings.stream()
                .filter(spending -> spending.name().equals(name))
                .findFirst();
    }
}
