package com.a14.emart.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.a14.emart.auth.model.User;
import com.a14.emart.auth.repository.UserRepository;
import com.a14.emart.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.NoSuchElementException;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .username("usertest1")
                .email("usertest1@gmail.com")
                .password("encodedpassword")
                .role("admin")
                .build();
    }

    @Test
    void testFindByUsernameSuccess() {
        String username = "usertest1";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername(username);

        assertEquals(user, foundUser);
    }

    @Test
    void testFindByUsernameNotFound() {
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findByUsername(username));
    }

    @Test
    void testFindByEmailSuccess() {
        String email = "usertest1@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail(email);

        assertEquals(user, foundUser);
    }

    @Test
    void testFindByEmailNotFound() {
        String email = "nonexistentemail@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findByEmail(email));
    }
}
