package org.unusualspending;

public class Payment {
    private int price;
    private String spending;
    private String description;

    public Payment(int price, String spending, String description) {
        this.price = price;
        this.spending = spending;
        this.description = description;
    }
}
