package org.unusualspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Spendings {
    public List<Spending> thatAreAtLeast50PercentMoreThan(List<Spending> pastSpendings, List<Spending> spendings) {
        List<Spending> unusualSpendings = new ArrayList<>();
        for (Spending actualSpending : spendings) {
            Optional<Spending> pastSpending = findSpending(actualSpending.name(), pastSpendings);
            if (pastSpending.isPresent() && actualSpending.isAtLeast50PercentMoreThan(pastSpending.get())) {
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
