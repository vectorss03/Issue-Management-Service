package com.se14.service.implement1;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserSession;
import com.se14.domain.Issue;
import com.se14.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionServiceImplementTest {

    private SessionService sessionService;

    private User user;
    private Project project;
    private Issue issue;

    @BeforeEach
    void setUp() {
        sessionService = new SessionServiceImplement();

        user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        project = new Project();
        project.setProjectId(1);
        project.setProjectTitle("Test Project");

        issue = new Issue();
        issue.setTitle("Test Issue");
    }

    @Test
    @DisplayName("Get current session")
    void testGetCurrentSession() {
        // Act
        UserSession userSession = sessionService.getCurrentSession();

        // Assert
        assertThat(userSession).isNotNull();
    }

    @Test
    @DisplayName("Set current user")
    void testSetCurrentUser() {
        // Act
        sessionService.setCurrentUser(user);

        // Assert
        UserSession userSession = sessionService.getCurrentSession();
        assertThat(userSession.getCurrentUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Set current project")
    void testSetCurrentProject() {
        // Act
        sessionService.setCurrentProject(project);

        // Assert
        UserSession userSession = sessionService.getCurrentSession();
        assertThat(userSession.getCurrentProject()).isEqualTo(project);
    }

    @Test
    @DisplayName("Set current issue")
    void testSetCurrentIssue() {
        // Act
        sessionService.setCurrentIssue(issue);

        // Assert
        UserSession userSession = sessionService.getCurrentSession();
        assertThat(userSession.getCurrentIssue()).isEqualTo(issue);
    }
}
