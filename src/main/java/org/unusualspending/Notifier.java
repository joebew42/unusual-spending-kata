package org.unusualspending;

import java.util.List;

public interface Notifier {
    void notifyFor(List<Spending> spendings);
}
