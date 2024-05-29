package test.java.com.se14.service.implement1;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserRole;
import com.se14.repository.UserRepository;
import com.se14.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplementTest {

    @Mock
    private UserRepository userRepository;

    private SecurityService securityService;

    private User user;
    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the securityService with the mocked userRepository
        securityService = new SecurityServiceImplement(userRepository);

        user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        project = new Project();
        project.setProjectId(1);
        project.setProjectTitle("Test Project");
        project.setMembers(new HashMap<>());
        project.getMembers().put(user, Arrays.asList(UserRole.DEVELOPER, UserRole.TESTER));
    }

    @Test
    @DisplayName("User has access to project with required role")
    void testHasAccessSuccess() {
        // Act
        boolean result = securityService.hasAccess(user, project, UserRole.DEVELOPER);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("User does not have access to project with required role")
    void testHasAccessFailure() {
        // Act
        boolean result = securityService.hasAccess(user, project, UserRole.ADMIN);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("User is authenticated")
    void testIsAuthenticatedSuccess() {
        // Arrange
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // Act
        boolean result = securityService.isAuthenticated(user);

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    @DisplayName("User is not authenticated")
    void testIsAuthenticatedFailure() {
        // Arrange
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());
        // Act
        boolean result = securityService.isAuthenticated(user);
        // Assert
        assertThat(result).isFalse();
        verify(userRepository).findById(user.getUserId());
    }
}
