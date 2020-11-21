package org.unusualspending;

import java.util.List;

public interface Payments {
    List<Payment> currentMonth(String user);

    List<Payment> lastMonth(String user);
}
