package com.se14.service.implement1;

import com.se14.domain.*;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplementTest2 {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectServiceImplement projectService;

    private List<Project> projects;
    private List<User> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projects = new ArrayList<>();
        users = new ArrayList<>();

        // Create Users
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUserId(i);
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            users.add(user);
        }

        // Create Projects
        for (int i = 0; i < 3; i++) {
            Project project = new Project();
            project.setProjectId(i);
            project.setProjectTitle("Project " + i);
            project.setProjectDescription("Description for Project " + i);
            project.setMembers(new HashMap<>());
            project.setIssues(new ArrayList<>());
            projects.add(project);
        }

        // Assign Users to Projects
        for (Project project : projects) {
            project.setMembers(new HashMap<>());
            for (User user : users) {
                List<UserRole> roles = new ArrayList<>(Arrays.asList(UserRole.DEVELOPER, UserRole.TESTER));
                project.getMembers().put(user, roles);
            }
            project.getMembers().get(users.get(0)).add(UserRole.ADMIN); // Add ADMIN role to the first user
        }

        // Assign Issues to Projects
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.MAY, 10, 0, 0, 0); // Starting from May 10, 2024
        for (Project project : projects) {
            for (int j = 0; j < 10; j++) {
                Issue issue = new Issue();
                issue.setTitle("Issue " + j);
                issue.setDescription("Description " + j);
                issue.setPriority(IssuePriority.MAJOR);
                issue.setStatus(IssueStatus.NEW);
                issue.setReporter(users.get(j % users.size()));
                issue.setReportedDate(cal.getTime());

                // Add comments to each issue
                List<Comment> comments = new ArrayList<>();
                comments.add(new Comment("Comment 1 for Issue " + j, "Content 1 for Issue " + j, new Date(), users.get(j % users.size())));
                comments.add(new Comment("Comment 2 for Issue " + j, "Content 2 for Issue " + j, new Date(), users.get((j + 1) % users.size())));
                issue.setComments(comments);

                project.getIssues().add(issue);
                cal.add(Calendar.DATE, 1);
            }
        }
    }

    @Test
    @DisplayName("List Projects returns all projects")
    void testListProjects() {
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> foundProjects = projectService.listProject();

        assertThat(foundProjects).hasSize(3).containsAll(projects);
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Create Project and add creator as Admin")
    void testCreateProject() {
        User creator = users.get(1);
        String projectName = "New Project";
        String projectDescription = "New Project Description";

        Project createdProject = projectService.createProject(creator, projectName, projectDescription);

        assertThat(createdProject).isNotNull();
        assertThat(createdProject.getProjectTitle()).isEqualTo(projectName);
        assertThat(createdProject.getProjectDescription()).isEqualTo(projectDescription);
        assertThat(createdProject.getMembers()).containsKey(creator);
        assertThat(createdProject.getMembers().get(creator)).contains(UserRole.ADMIN);

        verify(projectRepository, atLeastOnce()).save(createdProject);
    }

    @Test
    @DisplayName("Add Member to Project")
    void testAddMemberToProject() {
        Project project = projects.get(0);
        User user = users.get(1);
        UserRole role = UserRole.TESTER;
        UserRole role2 = UserRole.PROJECT_LEAD;

        projectService.addMemberToProject(project, user, Arrays.asList(role));
        projectService.addMemberToProject(project, user, Arrays.asList(role2));

        assertThat(project.getMembers()).containsKey(user);
        assertThat(project.getMembers().get(user)).contains(role);
        assertThat(project.getMembers().get(user)).contains(role2);

        verify(projectRepository, atLeastOnce()).save(project);
    }

    @Test
    @DisplayName("Get Statistic by Date")
    void testGetStatistic() {
        Project project = projects.get(0);

        Map<String, Object> statistics = projectService.getStatistic(project);

        assertThat(statistics).isNotNull();
        assertThat(statistics).containsKeys("dailyIssueCount", "monthlyIssueCount", "priorityCount", "statusCount");

        // Verify the dateIssues map
        Map<Calendar, List<Issue>> dateIssues = (Map<Calendar, List<Issue>>) statistics.get("dailyIssueCount");
        assertThat(dateIssues).isNotNull();
        assertThat(dateIssues).hasSize(7);

        Map<Calendar, List<Issue>> monthIssues = (Map<Calendar, List<Issue>>) statistics.get("monthlyIssueCount");
        assertThat(monthIssues).isNotNull();
        assertThat(monthIssues).hasSize(6);

        // Verify the priorityCount map
        Map<IssuePriority, Integer> priorityCount = (Map<IssuePriority, Integer>) statistics.get("priorityCount");
        assertThat(priorityCount).isNotNull();
        assertThat(priorityCount.get(IssuePriority.MAJOR)).isEqualTo(10);

        // Verify the statusCount map
        Map<IssueStatus, Integer> statusCount = (Map<IssueStatus, Integer>) statistics.get("statusCount");
        assertThat(statusCount).isNotNull();
        assertThat(statusCount.get(IssueStatus.NEW)).isEqualTo(10);

        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("List Users by Role")
    void testListUserByRole() {
        Project project = projects.get(0);
        UserRole role = UserRole.TESTER;

        List<User> usersWithRole = projectService.listUser(project, role);

        assertThat(usersWithRole).isNotEmpty();
        assertThat(usersWithRole).hasSize(5);  // Assuming 5 users were added to each project in the setup
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Check if Project has User")
    void testHasUser() {
        Project project = projects.get(0);
        User user = users.get(0);

        boolean result = projectService.hasUser(project, user);

        assertThat(result).isTrue();
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Handle exception in listProject")
    void testListProjectException() {
        // Arrange
        when(projectRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> projectService.listProject());
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Handle exception in createProject")
    void testCreateProjectException() {
        // Arrange
        User creator = users.get(0);
        String projectName = "New Project";
        String projectDescription = "New Project Description";

        when(projectRepository.save(any(Project.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> projectService.createProject(creator, projectName, projectDescription));
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Handle exception in addMemberToProject")
    void testAddMemberToProjectException() {
        // Arrange
        Project project = projects.get(0);
        User user = users.get(1);
        UserRole role = UserRole.TESTER;

        doThrow(new RuntimeException("Database error")).when(projectRepository).save(any(Project.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> projectService.addMemberToProject(project, user, Arrays.asList(role)));
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Handle empty issues in getStatistic")
    void testGetStatisticEmptyIssues() {
        // Arrange
        Project project = projects.get(0);
        project.setIssues(new ArrayList<>()); // No issues

        // Act
        Map<String, Object> statistics = projectService.getStatistic(project);

        // Assert
        assertThat(statistics).isNotNull();
        assertThat(((Map<Calendar, List<Issue>>) statistics.get("dateIssues")).isEmpty()).isTrue();
        assertThat(((Map<IssuePriority, Integer>) statistics.get("priorityCount")).isEmpty()).isTrue();
        assertThat(((Map<IssueStatus, Integer>) statistics.get("statusCount")).isEmpty()).isTrue();
        //assertThat(statistics.get("dateIssues")).isInstanceOf(Map.class).isEmpty();
        //assertThat(statistics.get("priorityCount")).isInstanceOf(Map.class).isEmpty();
        //assertThat(statistics.get("statusCount")).isInstanceOf(Map.class).isEmpty();
    }

    @Test
    @DisplayName("Handle no users with specified role in listUser")
    void testListUserNoRole() {
        // Arrange
        Project project = projects.get(0);
        UserRole role = UserRole.PROJECT_LEAD; // Assuming no PL in the setup

        // Act
        List<User> usersWithRole = projectService.listUser(project, role);

        // Assert
        assertThat(usersWithRole).isEmpty();
    }

    @Test
    @DisplayName("Handle user not in project in hasUser")
    void testHasUserNotInProject() {
        // Arrange
        Project project = projects.get(0);
        User userNotInProject = new User();
        userNotInProject.setUserId(999);
        userNotInProject.setUsername("notInProject");

        // Act
        boolean result = projectService.hasUser(project, userNotInProject);

        // Assert
        assertThat(result).isFalse();
    }
    @Test
    @DisplayName("Find Projects by User")
    void testFindProjectByUser() {
        User user = users.get(0);
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> userProjects = projectService.findProjectByUser(user);

        assertThat(userProjects).isNotEmpty();
        assertThat(userProjects.size()).isEqualTo(3);  // Assuming the user is part of all projects
        verify(projectRepository).findAll();
    }

}
