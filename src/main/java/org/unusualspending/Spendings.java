package org.unusualspending;

import java.util.List;
import java.util.Optional;

public class Spendings {
    static Optional<Spending> findSpending(String name, List<Spending> spendings) {
        return spendings.stream()
                .filter(spending -> spending.name().equals(name))
                .findFirst();
    }
}
