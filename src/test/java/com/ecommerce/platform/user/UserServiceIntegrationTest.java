package com.ecommerce.platform.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_SavesUserSuccessfully() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("mypassword");

        userService.registerUser(user);

        User savedUser = userRepository.findByUsername("alice").orElse(null);
        assertNotNull(savedUser, "User should be saved");
        assertEquals("user", savedUser.getRole());
        assertTrue(passwordEncoder.matches("mypassword", savedUser.getPassword()));
    }

    @Test
    void registerUser_DuplicateUsername_ShouldFail() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("12345");

        userService.registerUser(user);

        User duplicate = new User();
        duplicate.setUsername("bob");
        duplicate.setPassword("54321");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(duplicate));
        assertEquals("User already exists", ex.getMessage());
    }
}
