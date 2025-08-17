package com.ecommerce.platform.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");
        user.setPassword("plainPassword");
    }

    @Test
    void registerUser_ShouldSaveUserSuccessfully() {
        // User does not exist
        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        userService.registerUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals("user", user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void registerUser_UsernameAlreadyExists_ShouldThrowException() {
    	when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));


        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser(user));

        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
