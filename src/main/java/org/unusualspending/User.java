package org.unusualspending;

public class User {

    private final String userName;
    private final String email;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String userName() {
        return userName;
    }

    public String email() {
        return email;
    }
}
