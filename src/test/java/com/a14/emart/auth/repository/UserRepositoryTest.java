package com.a14.emart.auth.repository;

import com.a14.emart.auth.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User createUser(String username, String firstName, String lastName, String password, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    @Test
    public void testSaveUser() {
        User newUser = createUser("john_doe", "John", "Doe", "password123", User.Role.NORMAL_USER);
        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirstName());
        assertEquals("password123", savedUser.getPassword());
    }

    @Test
    public void testDuplicateUsername() {
        User user1 = createUser("unique_user", "First", "User", "password123", User.Role.NORMAL_USER);
        userRepository.save(user1);
        User user2 = createUser("unique_user", "Second", "User", "password456", User.Role.NORMAL_USER);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }





}