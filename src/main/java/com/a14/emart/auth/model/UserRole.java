package com.a14.emart.auth.model;

public enum UserRole {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    MANAGER("MANAGER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equals(value.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}