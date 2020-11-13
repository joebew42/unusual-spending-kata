package org.unusualspending;

import java.util.List;
import java.util.stream.Collectors;

public class Payments {
    public static List<Spending> groupBySpendings(List<Payment> payments) {
        return payments.stream()
                .collect(Collectors.groupingBy(Payment::spending, Collectors.summingInt(Payment::price)))
                .entrySet().stream()
                .map(entry -> new Spending(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }
}
