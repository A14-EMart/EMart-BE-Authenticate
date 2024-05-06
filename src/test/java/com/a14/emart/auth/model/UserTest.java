package com.a14.emart.auth.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    void init() {
        user = new User("John", "Doe", "johndoe", "1234",  User.Role.NORMAL_USER);
    }

    @Test
    void testUserInstantiation() {
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe", user.getUsername());
        assertEquals(User.Role.NORMAL_USER, user.getRole());
    }

    @Test
    void testSettersAndGetters() {
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setUsername("janesmith");
        user.setRole(User.Role.MANAGER);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("janesmith", user.getUsername());
        assertEquals(User.Role.MANAGER, user.getRole());
    }
}
