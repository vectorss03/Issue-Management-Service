package com.se14.service;

import com.se14.domain.*;

import java.util.List;

public interface IssueService {
    void reportIssue(Project project, User reporter,String title, String description, IssuePriority priority);
    List<Issue> searchIssues(Project project,SearchCriteria criteria);
    //List<Issue> searchByFilter(String filter); // filter = "project:test status:new ..."
    void assignIssue(User assigner, Issue issue, User assignee);
    void updateIssueStatus(User updater, Issue issue, IssueStatus status);
    void addComment(Project project, User commenter, Issue issue, String commentTitle, String commentText);
}
