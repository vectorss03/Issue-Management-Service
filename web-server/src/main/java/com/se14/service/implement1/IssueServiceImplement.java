package com.se14.service.implement1;

import com.se14.domain.*;
import com.se14.repository.db_impl.DBInitializer;
import com.se14.service.*;
import com.se14.repository.IssueRepository;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;
import com.se14.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;


public class IssueServiceImplement implements IssueService{
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;

    public IssueServiceImplement(IssueRepository issueRepository, ProjectRepository projectRepository, CommentRepository commentRepository) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository;
    }
    @Override
    public void reportIssue(Project project, User reporter, String title, String description, IssuePriority priority) {
        Issue newIssue = new Issue();
        newIssue.setTitle(title);
        newIssue.setDescription(description);
        newIssue.setStatus(IssueStatus.NEW);
        newIssue.setPriority(priority);
        newIssue.setReporter(reporter);
        newIssue.setReportedDate(new Date());

        project.getIssues().add(newIssue);
        projectRepository.save(project);
        issueRepository.save(newIssue,project);
    }

    @Override
    public List<Issue> searchIssues(Project project, SearchCriteria criteria) {
        List<Issue> issues = issueRepository.findByProject(project);
        if (criteria == null) {
            return issues;
        }

        return issues.stream()
                .filter(issue -> criteria.getTitle() == null || issue.getTitle().toLowerCase().contains(criteria.getTitle().toLowerCase()))
                .filter(issue -> criteria.getDescription() == null || issue.getDescription().toLowerCase().contains(criteria.getDescription().toLowerCase()))
                .filter(issue -> criteria.getStatus() == null || issue.getStatus().equals(criteria.getStatus()))
                .filter(issue -> criteria.getPriority() == null || issue.getPriority().equals(criteria.getPriority()))
                .filter(issue -> (criteria.getStartDate() == null || !issue.getReportedDate().before(criteria.getStartDate()))
                        && (criteria.getEndDate() == null || !issue.getReportedDate().after(criteria.getEndDate())))
                .filter(issue -> criteria.getReporter() == null || issue.getReporter().getUserId().equals(criteria.getReporter().getUserId()))
                .filter(issue -> criteria.getFixer() == null || (issue.getFixer() != null && issue.getFixer().getUserId().equals(criteria.getFixer().getUserId())))
                .filter(issue -> criteria.getAssignee() == null || (issue.getAssignee() != null && issue.getAssignee().getUserId().equals(criteria.getAssignee().getUserId())))
                .collect(Collectors.toList());
    }

    @Override
    public void assignIssue(Project project,User assigner, Issue issue, User assignee) {
        if (issue.getStatus() == IssueStatus.NEW || issue.getStatus() == IssueStatus.REOPENED) {
            issue.setAssignee(assignee);
            issue.setFixer(null);
            issue.setStatus(IssueStatus.ASSIGNED);
            issueRepository.save(issue,project);
        }else{
            System.out.println("Issue cannot be assigned due to state.");
        }

    }
    @Override
    public void updateIssueStatus(Project project,User updater, Issue issue, IssueStatus status) {
        issue.setStatus(status);
        if (status == IssueStatus.FIXED) {
            issue.setFixer(updater);
        }
        issueRepository.save(issue,project);
    }

    @Override
    public void addComment(Project project, User commenter, Issue issue, String commentTitle, String commentText) {
        Comment newComment = new Comment(commentTitle, commentText, new Date(), commenter);
        issue.getComments().add(newComment);
        issueRepository.save(issue, project);
        commentRepository.save(newComment, issue);
    }
    @Override
    public Issue findIssueById(Integer id){ return issueRepository.findById(id).orElse(null); }

}
