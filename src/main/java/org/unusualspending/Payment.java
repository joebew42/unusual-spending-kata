package org.unusualspending;

public class Payment {
    private final int price;
    private final String spending;
    private final String description;

    public Payment(int price, String spending, String description) {
        this.price = price;
        this.spending = spending;
        this.description = description;
    }

    public String spending() {
        return spending;
    }

    public int price() {
        return price;
    }
}
