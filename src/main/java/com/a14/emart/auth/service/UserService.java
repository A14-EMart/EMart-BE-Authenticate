package com.a14.emart.auth.service;

import com.a14.emart.auth.dto.Manager;
import com.a14.emart.auth.model.User;
import com.a14.emart.auth.model.UserRole;
import com.a14.emart.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<Manager> getManagers() {
        List<User> managers = findByRole(UserRole.MANAGER);
        List<User> customers = findByRole(UserRole.CUSTOMER);

        return Stream.concat(managers.stream(), customers.stream())
                .map(user -> new Manager(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }
}

