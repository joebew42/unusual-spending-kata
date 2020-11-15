package org.unusualspending;

import java.util.List;
import java.util.Objects;

public class Notification {
    private final String user;
    private final Spendings spendings;

    public Notification(String user, Spendings spendings) {
        this.user = user;
        this.spendings = spendings;
    }

    public List<Spending> allSpendings() {
        return spendings.all();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification notification = (Notification) o;
        return Objects.equals(user, notification.user) &&
                Objects.equals(spendings, notification.spendings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, spendings);
    }

    @Override
    public String toString() {
        return "Message{" +
                "user='" + user + '\'' +
                ", spendings=" + spendings +
                '}';
    }
}
