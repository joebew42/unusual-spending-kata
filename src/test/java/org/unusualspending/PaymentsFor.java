package org.unusualspending;

import java.util.HashMap;
import java.util.List;

public class PaymentsFor implements Payments {
    private final HashMap<String, List<Payment>> currentMonthPayments = new HashMap<>();
    private final HashMap<String, List<Payment>> lastMonthPayments = new HashMap<>();

    public PaymentsFor(User user, List<Payment> currentMonth, List<Payment> lastMonth) {
        currentMonthPayments.put(user.userName(), currentMonth);
        lastMonthPayments.put(user.userName(), lastMonth);
    }

    @Override
    public List<Payment> ofCurrentMonth(String user) {
        return currentMonthPayments.get(user);
    }

    @Override
    public List<Payment> ofLastMonth(String user) {
        return lastMonthPayments.get(user);
    }
}
