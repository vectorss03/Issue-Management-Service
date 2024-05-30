package com.se14.repository.fake;

import com.se14.domain.*;
import com.se14.domain.IssueStatus;
import com.se14.domain.IssuePriority;
import com.se14.repository.CommentRepository;
import com.se14.repository.IssueRepository;
import com.se14.repository.ProjectRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class IssueRepositoryFake implements IssueRepository {

    private final Map<Long, Issue> issues = new HashMap<>();
    private long currentId = 1;
    public ProjectRepository projectRepositoryFake;
    public CommentRepository commentRepositoryFake;

    public IssueRepositoryFake(ProjectRepository projectRepositoryFake,CommentRepository commentRepositoryFake) {
        this.projectRepositoryFake = projectRepositoryFake;
        this.commentRepositoryFake =commentRepositoryFake;
        Project project1 = projectRepositoryFake.findById(1).orElse(null);

        if (project1 != null) {
            Map<User, List<UserRole>> members = project1.getMembers();
            List<User> reporters = new ArrayList<>();
            List<User> developers = new ArrayList<>();
            List<User> fixers = new ArrayList<>();

            // Collect users based on roles
            members.forEach((user, roles) -> {
                if (roles.contains(UserRole.TESTER)) {
                    reporters.add(user);
                }
                if (roles.contains(UserRole.DEVELOPER)) {
                    developers.add(user);
                    fixers.add(user);  // Assuming developers can also be fixers
                }
            });

            Calendar cal = Calendar.getInstance();
            Random rand = new Random();

            // Create NEW issues
            for (int i = 0; i < 5; i++) {
                User reporter = reporters.get(i);
                Issue issue = createIssue("NEW", i + 1, reporter, IssueStatus.NEW, cal, 18 + i, project1);
                project1.getIssues().add(issue);
            }

            // Create ASSIGNED issues
            for (int i = 0; i < 5; i++) {
                User reporter = reporters.get(i);
                User assignee = developers.get(i);
                Issue issue = createIssue("ASSIGNED", i + 1*5, reporter, IssueStatus.ASSIGNED, cal, 13 + i, project1);
                issue.setAssignee(assignee);
                project1.getIssues().add(issue);
            }

            // Create FIXED issues
            for (int i = 0; i < 5; i++) {
                User reporter = reporters.get(i);
                User fixer = fixers.get(i);
                User assignee = developers.get(i);
                Issue issue = createIssue("FIXED", i + 1*10, reporter, IssueStatus.FIXED, cal, 7 + i, project1);
                issue.setFixer(fixer);
                issue.setAssignee(assignee);
                project1.getIssues().add(issue);
            }
        }
    }

    private Issue createIssue(String status, int num, User reporter, IssueStatus issueStatus, Calendar cal, int dayOffset, Project project) {
        Issue issue = new Issue();
        issue.setIssueId((int) currentId++);
        issue.setTitle("Issue " + status + " " + num);
        issue.setDescription("Description for Issue " + status + " " + num);
        issue.setStatus(issueStatus);
        issue.setPriority(IssuePriority.MAJOR);
        cal.set(2024, Calendar.MAY, dayOffset, 0, 0, 0);
        issue.setReportedDate(cal.getTime());
        issue.setReporter(reporter);
        if(issueStatus == IssueStatus.FIXED) {
            addComment(issue, status, num, reporter,issue.getFixer());
        }
        else{
            addComment(issue, status, num, reporter,issue.getFixer());
        }

        save(issue, project);
        return issue;
    }

    private void addComment(Issue issue, String status, int num, User commenter, User fixer) {
        Comment comment = new Comment();
        comment.setCommentTitle("Report issue " + num+" ");
        comment.setText("Report issue " + num+" ");
        comment.setTimestamp(new Date());
        comment.setAuthor(commenter);
        commentRepositoryFake.save(comment,issue);

        if (status.equals("FIXED")) {
            Comment fixComment = new Comment();
            fixComment.setCommentTitle("Fix issue " + num);
            fixComment.setText("Fix issue " + num);
            fixComment.setTimestamp(new Date());
            fixComment.setAuthor(fixer);
            //issue.setComments(Arrays.asList(comment, fixComment));
            commentRepositoryFake.save(fixComment,issue);
        } else {
            //issue.setComments(Collections.singletonList(comment));
        }
    }

    @Override
    public Issue save(Issue issue, Project project) {
        issue.setTitle(issue.getTitle() + " - Project " + project.getProjectTitle());
        issues.put(currentId++, issue);
        return issue;
    }

    @Override
    public Optional<Issue> findById(long id) {
        return Optional.ofNullable(issues.get(id));
    }

    @Override
    public List<Issue> findByProject(Project project) {
        return project.getIssues();
    }
}
