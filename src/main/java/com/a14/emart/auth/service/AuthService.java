package com.a14.emart.auth.service;

import com.a14.emart.auth.dto.LoginRequest;
import com.a14.emart.auth.dto.LoginResponse;
import com.a14.emart.auth.dto.RegisterRequest;
import com.a14.emart.auth.model.User;
import com.a14.emart.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public LoginResponse register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Optional<User> existingUser = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getUsername().equals(request.getUsername())) {
                throw new IllegalArgumentException("Username is already taken.");
            } else {
                throw new IllegalArgumentException("Email is already in use.");
            }
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .role("CUSTOMER")
                .build();

        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());

        var jwtToken = jwtService.generateToken(extraClaims, user);

        return LoginResponse.builder().token(jwtToken).build();
    }


    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsernameOrEmail(),
                request.getPassword()));
        String usernameOrEmail = request.getUsernameOrEmail();

        Optional<User> userOptional;

        if (usernameOrEmail.contains("@")) {
            userOptional = userRepository.findByUsernameOrEmail(null, usernameOrEmail);
        } else {
            userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, null);
        }

        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole());

        var jwtToken = jwtService.generateToken(extraClaims, user);
        return LoginResponse.builder().token(jwtToken).build();
    }
}