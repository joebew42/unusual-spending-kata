package org.unusualspending;

import java.util.List;
import java.util.Objects;

public class Message {
    private String user;
    private List<Spending> spendings;

    public Message(String user, List<Spending> spendings) {
        this.user = user;
        this.spendings = spendings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(user, message.user) &&
                Objects.equals(spendings, message.spendings);
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
