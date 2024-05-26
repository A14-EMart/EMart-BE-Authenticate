package com.a14.emart.auth.service;


import com.a14.emart.auth.dto.LoginRequest;
import com.a14.emart.auth.dto.LoginResponse;
import com.a14.emart.auth.dto.RegisterRequest;
import com.a14.emart.auth.model.User;
import com.a14.emart.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        String username = "usertest1";
        String email = "usertest1@gmail.com";
        String password = "abcdefgh1";

        doReturn("8hw2r5t9x7df3qc").when(passwordEncoder).encode(password);

        user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("admin")
                .build();
        LoginRequest registerRequest = new LoginRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
    }

    @Test
    void testValidRegistration() {
        String username = "usertest2";
        String email = "usertest2@gmail.com";
        String password = "abcdefgh2";
        String token = "20e909102-fwaofkwaofk";

        doReturn("213921839affn").when(passwordEncoder).encode(password);

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("customer")
                .build();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        LoginResponse registerResponse = new LoginResponse();
        registerResponse.setToken(token);

        doReturn(token).when(jwtService).generateToken(any(), eq(user));
        assertEquals(registerResponse, authService.register(registerRequest));
    }

    @Test
    void testRegisterMissingUsername() {
        String email = "abcdefg@gmail.com";
        String password = "abcdefg123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterUsernameAlreadyTaken() {
        String username = "usertest1";
        String email = "newemail@gmail.com";
        String password = "abcdefg123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterEmailAlreadyTaken() {
        String username = "newuser";
        String email = "usertest1@gmail.com";
        String password = "abcdefg123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterMissingAllFields() {
        RegisterRequest registerRequest = new RegisterRequest();

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterEmptyEmail() {
        String username = "usertest";
        String password = "abcdefg123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail("");
        registerRequest.setPassword(password);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterEmptyPassword() {
        String username = "usertest";
        String email = "abcdefg@gmail.com";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword("");

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterEmptyUsername() {
        String email = "abcdefg@gmail.com";
        String password = "abcdefg123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("");
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterNullEmail() {
        String username = "usertest";
        String password = "abcdefg123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(null);
        registerRequest.setPassword(password);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterNullPassword() {
        String username = "usertest";
        String email = "abcdefg@gmail.com";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }
    @Test
    void testRegisterNullUsername() {
        String email = "abcdefg@gmail.com";
        String password = "abcdefg123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(null);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }

    @Test
    void testAuthenticateValidWithEmail() {
        doReturn("jwt-token-123").when(jwtService).generateToken(any(), eq(user));
        doReturn(Optional.ofNullable(user)).when(userRepository).findByEmail(user.getEmail());
        doReturn(null).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                "abcdefgh1"
        ));


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword("abcdefgh1");

        LoginResponse authenticateResponse = new LoginResponse();
        authenticateResponse.setToken("jwt-token-123");

        assertEquals(authenticateResponse, authService.authenticate(loginRequest));
    }
    @Test
    void testAuthenticateValidWithUsername() {
        doReturn("jwt-token-123").when(jwtService).generateToken(any(), eq(user));
        doReturn(Optional.ofNullable(user)).when(userRepository).findByUsername(user.getUsername());
        doReturn(null).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                "abcdefgh1"
        ));

        System.out.println(user.getUsername());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword("abcdefgh1");

        LoginResponse authenticateResponse = new LoginResponse();
        authenticateResponse.setToken("jwt-token-123");

        assertEquals(authenticateResponse, authService.authenticate(loginRequest));
    }

    @Test
    void testInvalidAuthenticateWithInvalidPassword() {
        LoginRequest authenticateRequest = new LoginRequest();
        authenticateRequest.setUsername("usertest1");
        authenticateRequest.setPassword("xixixixixi");

        doThrow(new NoSuchElementException("User not found"))
                .when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("usertest1@gmail.com", "xixixixixi"));

        assertThrows(NoSuchElementException.class, () -> authService.authenticate(authenticateRequest));
    }

    @Test
    void testInvalidAuthenticateWithMissingPassword() {
        LoginRequest authenticateRequest = new LoginRequest();
        authenticateRequest.setUsername("usertest1");

        assertThrows(NoSuchElementException.class, () -> authService.authenticate(authenticateRequest));
    }

    @Test
    void testInvalidAuthenticateWithMissingUsernameOrEmail() {
        LoginRequest authenticateRequest = new LoginRequest();
        authenticateRequest.setPassword("abcdefgh1");

        assertThrows(NoSuchElementException.class, () -> authService.authenticate(authenticateRequest));
    }















}
