package com.se14.service.implement1;

import com.se14.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2EmbeddingClient;
import org.springframework.ai.vertexai.palm2.api.VertexAiPaLm2Api;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class DeveloperRecommendationServiceImplementTest {

    private DeveloperRecommendationServiceImplement developerRecommendationService;
    private Project project;
    private List<User> users;
    private List<Issue> issues;

    @BeforeEach
    void setUp() {
        /*
        String apiKey = "AIzaSyAGWMK7yH1XmPmeEe4z5PE8S0HKZFBJABU";
        VertexAiPaLm2Api vertexAiApi = new VertexAiPaLm2Api(apiKey);
        VertexAiPaLm2EmbeddingClient embeddingClient = new VertexAiPaLm2EmbeddingClient(vertexAiApi);
*/
        developerRecommendationService = new DeveloperRecommendationServiceImplement();

        // Set up the project and users
        project = new Project();
        project.setProjectId(1);
        project.setProjectTitle("Main Project");
        project.setProjectDescription("Project Description");

        users = new ArrayList<>();
        Map<User, List<UserRole>> memberRoles = new HashMap<>();
        issues = new ArrayList<>();

        // Create 10 users and assign them as DEVELOPERS
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserId(i);
            user.setUsername("Developer_" + i);
            users.add(user);
            memberRoles.put(user, Collections.singletonList(UserRole.DEVELOPER));
        }

        project.setMembers(memberRoles);

        String[] issueTitles = new String[] {
                "UI Layout Break on Resize", "Database Connection Timeout", "API Latency Issues",
                "Memory Leak in Data Processing", "CSS Overlapping Styles", "Javascript Function Error",
                "Backend Server Crash", "Mobile Responsiveness Fail", "Authentication Timeout Failure",
                "Cross-Site Scripting (XSS) Vulnerability", "Missing Icons in Production", "Infinite Loop in Calculation",
                "Data Mismatch on Synchronization", "Incorrect User Role Permissions", "Email Notification Failure",
                "Search Functionality Returning Errors", "Video Playback Interruption", "Payment Gateway Rejection Issue",
                "Location Services Not Responding", "API Gateway Throttling Problems"
        };

        // Generate issues, each solved by a different developer, cycling through developers
        for (int i = 0; i < issueTitles.length; i++) {
            Issue issue = new Issue(users.get(i % 10), issueTitles[i], "Description for " + issueTitles[i], IssuePriority.MAJOR);
            issue.setFixer(users.get(i % 10));
            issues.add(issue);
        }

        project.setIssues(issues);
    }

    @Test
    @DisplayName("Recommend top 3 developers for a new issue using real embeddings")
    void testRecommendDeveloper() {
        Issue newIssue = new Issue();
        newIssue.setTitle("RDB repository sync error");
        newIssue.setDescription("Sync error needs resolution");
        newIssue.setStatus(IssueStatus.NEW);
        newIssue.setReportedDate(new Date());

        List<User> recommendedDevelopers = developerRecommendationService.recommendDeveloper(project, newIssue);

        assertThat(recommendedDevelopers).isNotEmpty();
        assertThat(recommendedDevelopers.size()).isEqualTo(3);
        System.out.println("Recommended Developers: " + recommendedDevelopers);
    }
    @Test
    @DisplayName("Recommend a single developer when only one has fixed all issues in a smaller project")
    void testRecommendSingleDeveloperForSmallProject() {
        // Set up a smaller project with only 2 issues, both fixed by the same developer
        Project smallProject = new Project();
        smallProject.setProjectId(2);
        smallProject.setProjectTitle("Small Project");
        smallProject.setProjectDescription("A small project with limited issues");

        //List<User> smallProjectUsers = new ArrayList<>();
        Map<User, List<UserRole>> smallMemberRoles = new HashMap<>();
        List<Issue> smallProjectIssues = new ArrayList<>();

        // Create one developer and assign them as DEVELOPER
        User singleDeveloper = new User();
        singleDeveloper.setUserId(10);
        singleDeveloper.setUsername("Solo_Developer");
        //smallProjectUsers.add(singleDeveloper);
        smallMemberRoles.put(singleDeveloper, Collections.singletonList(UserRole.DEVELOPER));

        smallProject.setMembers(smallMemberRoles);

        // Add two issues fixed by the same developer
        Issue issue1 = new Issue(singleDeveloper, "Minor UI Bug", "Minor alignment issue on the dashboard", IssuePriority.MINOR);
        issue1.setFixer(singleDeveloper);
        smallProjectIssues.add(issue1);

        Issue issue2 = new Issue(singleDeveloper, "Login Page Error", "Error on login page under specific conditions", IssuePriority.MINOR);
        issue2.setFixer(singleDeveloper);
        smallProjectIssues.add(issue2);

        smallProject.setIssues(smallProjectIssues);

        // Simulate the scenario for a new issue
        Issue newIssue = new Issue();
        newIssue.setTitle("New Minor Feature Request");
        newIssue.setDescription("Request to add a new button to UI");
        newIssue.setStatus(IssueStatus.NEW);
        newIssue.setReportedDate(new Date());

        // Use the recommendation service to get recommendations
        List<User> recommendedDevelopers = developerRecommendationService.recommendDeveloper(smallProject, newIssue);

        // Assert that only one developer is recommended and it's the correct one
        assertThat(recommendedDevelopers).hasSize(1);
        assertThat(recommendedDevelopers.get(0).getUserId()).isEqualTo(singleDeveloper.getUserId());
        System.out.println("Recommended Developer for small project: " + recommendedDevelopers.get(0).getUsername());
    }

}