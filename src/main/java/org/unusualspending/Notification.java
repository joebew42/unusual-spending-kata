package org.unusualspending;

import java.util.List;

public class Notification {
    private final Spendings spendings;
    private final User user;

    public Notification(User user, Spendings unusualSpendings) {
        this.spendings = unusualSpendings;
        this.user = user;
    }

    public List<Spending> allSpendings() {
        return spendings.all();
    }

    public String userEmail() {
        return user.email();
    }
}
