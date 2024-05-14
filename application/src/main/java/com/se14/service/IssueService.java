package com.se14.service;

import com.se14.domain.*;

import java.util.List;

public interface IssueService {
    void report(Member reporter, Issue issue);
    List<Issue> findByProject(Project project);
    List<Issue> searchByFilter(String filter); // filter = "project:test status:new ..."
    void assignDeveloper(Member assigner, Issue issue, Member assignee);
    void updateStatus(Member updater, Issue issue, IssueStatus status);
    void addComment(Member author, Issue issue, Comment comment);
}
