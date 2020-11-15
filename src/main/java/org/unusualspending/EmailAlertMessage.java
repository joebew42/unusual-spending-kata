package org.unusualspending;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class EmailAlertMessage {
    public static final String HEADER = "Hello card user!" +
            "\r\n\r\n" +
            "We have detected unusually high spending on your card in these categories:" +
            "\r\n\r\n";

    public static final String FOOTER = "\r\n" +
            "Love," +
            "\r\n\r\n" +
            "The Credit Card Company";

    public static final String SPENDING_TEXT = "* You spent $%d on %s\r\n";

    private final Notification notification;

    public EmailAlertMessage(Notification notification) {
        this.notification = notification;
    }

    public String subject() {
        return format("Unusual spending of $%d detected!", total(notification.allSpendings()));
    }

    public String text() {
        return HEADER
                .concat(spendingDetails(notification.allSpendings()))
                .concat(FOOTER);
    }

    private String spendingDetails(List<Spending> spendings) {
        return spendings.stream()
                .map(this::spendingDetail)
                .reduce("", String::concat);
    }

    private String spendingDetail(Spending spending) {
        return format(SPENDING_TEXT, spending.amount(), spending.name());
    }

    private long total(List<Spending> spendings) {
        return spendings.stream()
                .collect(Collectors.summarizingInt(Spending::amount))
                .getSum();
    }
}
