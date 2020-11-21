package org.unusualspending;

import java.util.List;

public interface Payments {
    List<Payment> ofCurrentMonth(String user);

    List<Payment> ofLastMonth(String user);
}
