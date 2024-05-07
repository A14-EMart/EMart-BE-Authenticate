package com.a14.emart.auth.repository;

import com.a14.emart.auth.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        // Setup data before each test
        user = new User("John", "Doe", "johndoe", "password123", User.Role.NORMAL_USER);
        entityManager.persist(user);
        entityManager.flush();
    }

    @AfterEach
    void cleanUp() {
        entityManager.clear();
    }

    @Test
    void whenFindByUsername_thenReturnUser() {
        Optional<User> found = userRepository.findById(user.getUsername());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void whenInvalidUsername_thenReturnEmpty() {
        Optional<User> notFound = userRepository.findById("nonexistent");

        assertThat(notFound.isEmpty()).isTrue();
    }

    @Test
    void whenFindAll_thenReturnUsers() {
        List<User> users = userRepository.findAll();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    void whenSaveUser_thenUserIsSaved() {
        User newUser = new User("Alice", "Smith", "alicesmith", "securepass", User.Role.MANAGER);

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
    }

}
