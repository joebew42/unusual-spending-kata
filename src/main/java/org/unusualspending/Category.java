package org.unusualspending;

import java.util.Objects;

public class Category {
    private final int amount;

    public Category(int amount) {
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return amount == category.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
