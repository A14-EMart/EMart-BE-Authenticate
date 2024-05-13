package com.a14.emart.auth.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .password("securePassword123")
                .role("CUSTOMER")
                .build();
    }

    @Test
    public void testUserDetails() {
        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("securePassword123", user.getPassword());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("CUSTOMER")));
    }

    @Test
    public void testInvalidUsername() {
        assertThrows(IllegalArgumentException.class, () -> User.builder().username("a"));
        assertThrows(IllegalArgumentException.class, () -> User.builder().username("ab"));
        assertThrows(IllegalArgumentException.class, () -> User.builder().username("abc"));
        assertThrows(IllegalArgumentException.class, () -> User.builder().username("abcd"));
        assertThrows(IllegalArgumentException.class, () -> User.builder().username(null));
    }

    @Test
    public void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> User.builder().email("wkwkwkwkwkwkwk"));
        assertThrows(IllegalArgumentException.class, () -> User.builder().email(null));
    }

    @Test
    public void testInvalidPassword() {
        assertThrows(IllegalArgumentException.class, () -> User.builder().password("1234"));
        assertThrows(IllegalArgumentException.class, () -> User.builder().password(null));
    }

    @Test
    public void testInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> User.builder().role("subscriber"));
        assertThrows(IllegalArgumentException.class, () -> User.builder().role(null));
        assertThrows(IllegalArgumentException.class, () -> User.builder().role("1"));
    }
}
