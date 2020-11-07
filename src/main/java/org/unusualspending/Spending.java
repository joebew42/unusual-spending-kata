package org.unusualspending;

import java.util.Objects;

public class Spending {
    private final int amount;
    private final String name;

    public Spending(int amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public int amount() {
        return amount;
    }

    boolean isAtLeast50percentMoreThan(Spending past) {
        return amount() >= past.amount() + past.amount() / 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spending spending = (Spending) o;
        return amount == spending.amount &&
                Objects.equals(name, spending.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, name);
    }
}
