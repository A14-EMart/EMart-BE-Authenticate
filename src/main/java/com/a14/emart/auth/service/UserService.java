package com.a14.emart.auth.service;

import com.a14.emart.auth.model.User;
import com.a14.emart.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

}
