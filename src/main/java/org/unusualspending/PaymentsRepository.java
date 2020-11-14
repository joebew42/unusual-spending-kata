package org.unusualspending;

import java.util.List;

public interface PaymentsRepository {
    List<Payment> currentMonth(String user);

    List<Payment> lastMonth(String user);
}
