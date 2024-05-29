package com.se14.service.implement1;

import com.se14.domain.User;
import com.se14.repository.UserRepository;
import com.se14.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the userService with the mocked userRepository
        userService = new UserServiceImplement(userRepository);

        user = new User("password123");
        user.setUserId(1);
        user.setUsername("testUser");
        user.setEmail("testUser@example.com");
    }

    @Test
    @DisplayName("User authentication successful")
    void testAuthenticateUserSuccess() {
        // Arrange
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());

        // Assert
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.getUsername()).isEqualTo(user.getUsername());
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("User authentication failed due to incorrect password")
    void testAuthenticateUserFailureIncorrectPassword() {
        // Arrange
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getUsername(), "wrongPassword");

        // Assert
        assertThat(authenticatedUser).isNull();
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("User authentication failed due to non-existing username")
    void testAuthenticateUserFailureNonExistingUsername() {
        // Arrange
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());

        // Assert
        assertThat(authenticatedUser).isNull();
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Add new user successfully")
    void testAddNewUserSuccess() {
        // Arrange
        String username = "newUser";
        String password = "newPassword";
        String email = "newUser@example.com";

        // Act
        userService.addNewUser(username, password, email);

        // Assert
        verify(userRepository).save(any(User.class));
    }
}
