package org.unusualspending;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class EmailAlertMessage {
    private final Notification notification;

    public EmailAlertMessage(Notification notification) {
        this.notification = notification;
    }

    public String subject() {
        return format("Unusual spending of $%d detected!", total(notification.allSpendings()));
    }

    public String text() {
        String body = "";
        for (Spending spending : notification.allSpendings()) {
            body = format("You spent $%d on %s", spending.amount(), spending.name()).concat(body);
        }
        return body;
    }

    private long total(List<Spending> spendings) {
        return spendings.stream()
                .collect(Collectors.summarizingInt(Spending::amount))
                .getSum();
    }
}
