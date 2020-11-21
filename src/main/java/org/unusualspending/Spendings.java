package org.unusualspending;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.List.copyOf;

public class Spendings {
    static Spendings from(List<Payment> currentMonthPayments) {
        List<Spending> spendings = currentMonthPayments.stream()
                .collect(Collectors.groupingBy(Payment::spending, Collectors.summingInt(Payment::price)))
                .entrySet().stream()
                .map(entry -> new Spending(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());

        return new Spendings(spendings);
    }

    private final List<Spending> spendings;

    public Spendings(Spending... spendings) {
        this(asList(spendings));
    }

    private Spendings(List<Spending> spendings) {
        this.spendings = spendings;
    }

    public Spendings thatAreAtLeast50PercentMoreThan(Spendings otherSpendings) {
        List<Spending> unusualSpendings = new ArrayList<>();
        for (Spending spending : spendings) {
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

    boolean isEmpty() {
        return spendings.isEmpty();
    }

    private Optional<Spending> findSpending(String name) {
        return spendings.stream()
                .filter(spending -> spending.name().equals(name))
                .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spendings spendings1 = (Spendings) o;
        return Objects.equals(spendings, spendings1.spendings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spendings);
    }
}
