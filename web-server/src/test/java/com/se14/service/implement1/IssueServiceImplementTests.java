package test.java.com.se14.service.implement1;

import com.se14.domain.*;
import com.se14.repository.IssueRepository;
import com.se14.repository.ProjectRepository;
import com.se14.repository.CommentRepository;
import com.se14.service.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Date;
import  java.util.Calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class IssueServiceImplementTests {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private IssueServiceImplement issueService;
    private Project project_test;
    private User user_test;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project_test = new Project();
        project_test.setIssues(new ArrayList<>());
        user_test = new User();
        user_test.setUsername("johnDoe");

        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.MAY, 10, 0, 0, 0); // Starting from May 10, 2024

        // Set up a project with 10 issues and 2 comments per issue
        for (int i = 0; i < 10; i++) {
            Issue issue = new Issue();
            issue.setTitle("Issue " + i);
            issue.setDescription("Description " + i);
            issue.setPriority(IssuePriority.MAJOR);
            issue.setStatus(IssueStatus.NEW);
            issue.setReporter(user_test);
            issue.setReportedDate(cal.getTime());

            // Add comments to each issue
            List<Comment> comments = new ArrayList<>();
            comments.add(new Comment("Comment 1 for Issue " + i, "Content 1 for Issue " + i, new Date(), user_test));
            comments.add(new Comment("Comment 2 for Issue " + i, "Content 2 for Issue " + i, new Date(), user_test));
            issue.setComments(comments);

            project_test.getIssues().add(issue);
            cal.add(Calendar.DATE, 1);
        }

    }

    @Test
    @DisplayName("Search Issues with no criteria returns all issues")
    void testSearchIssuesWithNoCriteria() {
        when(issueRepository.findByProject(eq(project_test))).thenReturn(project_test.getIssues());
        // Act
        List<Issue> foundIssues = issueService.searchIssues(project_test, null);

        // Assert
        assertThat(foundIssues).hasSize(10).containsAll(project_test.getIssues());
    }

    @Test
    @DisplayName("Search Issues filters correctly by title")
    void testSearchIssuesByTitle() {
        // Arrange
        when(issueRepository.findByProject(eq(project_test))).thenReturn(project_test.getIssues());

        SearchCriteria criteria = new SearchCriteria();
        criteria.setTitle("Issue 1");

        // Act
        List<Issue> foundIssues = issueService.searchIssues(project_test, criteria);

        // Assert
        assertThat(foundIssues).hasSize(1);
        assertThat(foundIssues.get(0).getTitle()).isEqualTo("Issue 1");
    }
    @Test
    @DisplayName("Search Issues filters correctly by title2")
    void testSearchIssuesByTitle2() {
        when(issueRepository.findByProject(eq(project_test))).thenReturn(project_test.getIssues());

        // Arrange
        SearchCriteria criteria = new SearchCriteria();
        criteria.setTitle("Is");

        // Act
        List<Issue> foundIssues = issueService.searchIssues(project_test, criteria);

        // Assert
        assertThat(foundIssues).hasSize(10);
        for(int i=0; i<10; i++) {
            assertThat(foundIssues.get(i).getTitle()).isEqualTo("Issue "+i);
        }
    }
    @Test
    @DisplayName("Search Issues filters correctly by time")
    void testSearchIssuesByTime() {
        when(issueRepository.findByProject(eq(project_test))).thenReturn(project_test.getIssues());
        // Arrange
        SearchCriteria criteria = new SearchCriteria();
        Calendar startCal = Calendar.getInstance();
        startCal.set(2024, Calendar.MAY, 14, 23, 59, 59);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2024, Calendar.MAY, 17, 23, 59, 59);

        criteria.setStartDate(startCal.getTime());
        criteria.setEndDate(endCal.getTime());

        // Act
        List<Issue> foundIssues = issueService.searchIssues(project_test, criteria);

        // Assert
        assertThat(foundIssues).hasSize(3).extracting("title")
                .containsExactly("Issue 5", "Issue 6", "Issue 7");
    }


    @Test
    @DisplayName("Report Issue correctly adds issue to project")
    void reportIssue() {
        // Arrange
        User reporter = new User();
        reporter.setUsername("johnDoe");

        Project project = new Project();
        project.setIssues(new ArrayList<>());

        String title = "New Issue";
        String description = "Description of new issue";
        IssuePriority priority = IssuePriority.MAJOR;

        // Act
        issueService.reportIssue(project, reporter, title, description, priority);

        // Assert
        assertThat(project.getIssues()).isNotEmpty();
        assertThat(project.getIssues().get(0).getTitle()).isEqualTo(title);
        assertThat(project.getIssues().get(0).getDescription()).isEqualTo(description);
        assertThat(project.getIssues().get(0).getPriority()).isEqualTo(priority);

        verify(projectRepository).save(project);
        verify(issueRepository).save(any(Issue.class), any(Project.class));
    }
    @Test
    @DisplayName("Report multiple issues across multiple projects")
    void reportMultipleIssues() {
        // Arrange
        Project project1 = new Project();
        project1.setIssues(new ArrayList<>());
        Project project2 = new Project();
        project2.setIssues(new ArrayList<>());

        ArrayList<User> usersProject1 = new ArrayList<>();
        ArrayList<User> usersProject2 = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("userProj1_" + i);
            usersProject1.add(user);

            user = new User();
            user.setUsername("userProj2_" + i);
            usersProject2.add(user);
        }

        // Act
        for (int i = 0; i < 10; i++) {
            issueService.reportIssue(project1, usersProject1.get(i), "Issue " + i, "Description " + i, IssuePriority.MAJOR);
            issueService.reportIssue(project2, usersProject2.get(i), "Issue " + i, "Description " + i, IssuePriority.MINOR);
        }

        // Assert
        assertThat(project1.getIssues()).hasSize(10);
        for (int i = 0; i < 10; i++) {
            assertThat(project1.getIssues().get(i).getTitle()).isEqualTo("Issue " + i);
            assertThat(project1.getIssues().get(i).getDescription()).isEqualTo("Description " + i);
            assertThat(project1.getIssues().get(i).getPriority()).isEqualTo(IssuePriority.MAJOR);
        }

        assertThat(project2.getIssues()).hasSize(10);
        for (int i = 0; i < 10; i++) {
            assertThat(project2.getIssues().get(i).getTitle()).isEqualTo("Issue " + i);
            assertThat(project2.getIssues().get(i).getDescription()).isEqualTo("Description " + i);
            assertThat(project2.getIssues().get(i).getPriority()).isEqualTo(IssuePriority.MINOR);
        }

        verify(projectRepository, times(20)).save(any(Project.class));
        verify(issueRepository, times(20)).save(any(Issue.class), any(Project.class));
    }
    @Test
    @DisplayName("Assign issue when status is NEW or REOPENED")
    void testAssignIssueWithValidStatus() {
        // Arrange
        Project project = new Project();
        User assigner = new User();
        User assignee = new User();
        Issue issue = new Issue();
        issue.setStatus(IssueStatus.NEW); // 테스트할 이슈 상태 설정

        // Act
        issueService.assignIssue(project, assigner, issue, assignee);

        // Assert
        assertThat(issue.getAssignee()).isEqualTo(assignee);
        assertThat(issue.getStatus()).isEqualTo(IssueStatus.ASSIGNED);
        verify(issueRepository).save(issue, project);
    }

    @Test
    @DisplayName("Do not assign issue if status is not NEW or REOPENED")
    void testAssignIssueWithInvalidStatus() {
        // Arrange
        Project project = new Project();
        User assigner = new User();
        User assignee = new User();
        Issue issue = new Issue();
        issue.setStatus(IssueStatus.RESOLVED); // 테스트할 이슈 상태 설정

        // Act
        issueService.assignIssue(project, assigner, issue, assignee);

        // Assert
        assertThat(issue.getAssignee()).isNotEqualTo(assignee); // 이슈에 할당되지 않아야 합니다.
        assertThat(issue.getStatus()).isNotEqualTo(IssueStatus.ASSIGNED); // 상태가 변경되지 않아야 합니다.
        verify(issueRepository, never()).save(issue, project); // 저장되지 않아야 합니다.
    }
    @Test
    @DisplayName("Update issue status to FIXED and set fixer")
    void testUpdateIssueStatusToFixed() {
        // Arrange
        IssueStatus newStatus = IssueStatus.FIXED;
        Issue issue = project_test.getIssues().get(0);
        // Act
        issueService.updateIssueStatus(project_test, user_test, issue, newStatus);

        // Assert
        assertThat(issue.getStatus()).isEqualTo(IssueStatus.FIXED);
        assertThat(issue.getFixer()).isEqualTo(user_test);
        verify(issueRepository).save(issue, project_test);
    }

    @Test
    @DisplayName("Update issue status to RESOLVED without setting fixer")
    void testUpdateIssueStatusToResolved() {
        // Arrange
        IssueStatus newStatus = IssueStatus.RESOLVED;
        Issue issue = project_test.getIssues().get(0);
        // Act
        issueService.updateIssueStatus(project_test, user_test, issue, newStatus);

        // Assert
        assertThat(issue.getStatus()).isEqualTo(IssueStatus.RESOLVED);
        assertThat(issue.getFixer()).isNull();  // Assuming getFixer returns null if no fixer is set
        verify(issueRepository).save(issue, project_test);
    }
    @Test
    @DisplayName("Add a comment to the first issue and verify")
    void testAddCommentToIssue() {
        // Arrange
        Issue targetIssue = project_test.getIssues().get(0);
        String commentTitle = "New Comment";
        String commentText = "This is a new comment.";

        // Act
        issueService.addComment(project_test, user_test, targetIssue, commentTitle, commentText);

        // Assert
        assertThat(targetIssue.getComments()).hasSize(3); // Assuming there were already 2 comments from setup
        Comment addedComment = targetIssue.getComments().get(2); // The new comment should be at index 2
        assertThat(addedComment.getCommentTitle()).isEqualTo(commentTitle);
        assertThat(addedComment.getText()).isEqualTo(commentText);
        assertThat(addedComment.getAuthor()).isEqualTo(user_test);
        assertThat(addedComment.getTimestamp()).isNotNull();

        verify(issueRepository).save(targetIssue, project_test);
        verify(commentRepository).save(addedComment, targetIssue);
    }
}
