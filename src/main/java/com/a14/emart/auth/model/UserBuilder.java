package com.a14.emart.auth.model;

import java.util.regex.Pattern;

public class UserBuilder {
    private String username;

    private String email;

    private String password;

    private UserRole role;

    public UserBuilder username(String username) {
        if (username == null || username.length() < 5) {
            throw new IllegalArgumentException();
        }

        this.username = username;
        return this;
    }

    public UserBuilder email(String email) {
        if (email == null || !Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,7}\\b").matcher(email).matches()) {
            throw new IllegalArgumentException();
        }

        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException();
        }

        this.password = password;
        return this;
    }

    public UserBuilder role(String role) {
        if (role == null || !UserRole.contains(role)) {
            throw new IllegalArgumentException();
        }

        this.role = UserRole.valueOf(role.toUpperCase());
        return this;
    }

    public User build() {
        User user = new User();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setRole(this.role);
        return user;
    }
}