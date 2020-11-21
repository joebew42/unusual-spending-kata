package org.unusualspending;

import java.util.HashMap;
import java.util.List;

public class PaymentsFor implements Payments {
    private final HashMap<String, List<Payment>> currentMonthPayments = new HashMap<>();
    private final HashMap<String, List<Payment>> lastMonthPayments = new HashMap<>();

    public PaymentsFor(String user, List<Payment> currentMonth, List<Payment> lastMonth) {
        currentMonthPayments.put(user, currentMonth);
        lastMonthPayments.put(user, lastMonth);
    }

    @Override
    public List<Payment> currentMonth(String user) {
        return currentMonthPayments.get(user);
    }

    @Override
    public List<Payment> lastMonth(String user) {
        return lastMonthPayments.get(user);
    }
}
