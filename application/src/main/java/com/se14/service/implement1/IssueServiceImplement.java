package com.se14.service.implement1;

import com.se14.domain.*;
import com.se14.service.*;
import com.se14.repository.IssueRepository;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;
public class IssueServiceImplement implements IssueService{
    private IssueRepository issueRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    public IssueServiceImplement(IssueRepository issueRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
                .filter(issue -> criteria.getTitle() == null || issue.getTitle().contains(criteria.getTitle()))
                .filter(issue -> criteria.getDescription() == null || issue.getDescription().contains(criteria.getDescription()))
                .filter(issue -> criteria.getStatus() == null || issue.getStatus().equals(criteria.getStatus()))
                .filter(issue -> criteria.getPriority() == null || issue.getPriority().equals(criteria.getPriority()))
                .filter(issue -> (criteria.getStartDate() == null || !issue.getReportedDate().before(criteria.getStartDate()))
                        && (criteria.getEndDate() == null || !issue.getReportedDate().after(criteria.getEndDate())))
                .filter(issue -> criteria.getReporter() == null || issue.getReporter().equals(criteria.getReporter()))
                .filter(issue -> criteria.getFixer() == null || issue.getFixer().equals(criteria.getFixer()))
                .filter(issue -> criteria.getAssignee() == null || issue.getAssignee().equals(criteria.getAssignee()))
                .collect(Collectors.toList());
    }

    @Override
    public void assignIssue(Project project,User assigner, Issue issue, User assignee) {
        if (issue.getStatus() == IssueStatus.NEW || issue.getStatus() == IssueStatus.REOPENED) {
            issue.setAssignee(assignee);
            issue.setStatus(IssueStatus.ASSIGNED);
            issueRepository.save(issue,project);
        }else{
            System.out.println("Issue cannot be assigned due to state.");
        }

    }

    @Override
    public void updateIssueStatus(Project progject,User updater, Issue issue, IssueStatus status) {

    }

    @Override
    public void addComment(Project project, User commenter, Issue issue, String commentTitle, String commentText) {

    }
}
