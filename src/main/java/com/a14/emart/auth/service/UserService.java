package com.a14.emart.auth.service;

import com.a14.emart.auth.model.User;
import com.a14.emart.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
public class UserService {
    UserRepository userRepository;
    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).orElseThrow();
    }

}
