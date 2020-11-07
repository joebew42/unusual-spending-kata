package org.unusualspending;

import java.util.Objects;

public class Category {
    private final int amount;
    private final String name;

    public Category(int amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public int amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return amount == category.amount &&
                Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, name);
    }
}
