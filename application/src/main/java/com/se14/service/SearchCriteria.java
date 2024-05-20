package com.se14.service;

import com.se14.domain.*;

import java.util.Date;

public class SearchCriteria {
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private Date startDate;
    private Date endDate;
    private User reporter;
    private User fixer;
    private User assignee;

    // Constructors
    public SearchCriteria() { }
    public SearchCriteria(String title, String description, IssueStatus status, IssuePriority priority,
                          Date startDate, Date endDate, User reporter, User fixer, User assignee) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reporter = reporter;
        this.fixer = fixer;
        this.assignee = assignee;
    }


    // Getters and setters for each field
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public User getFixer() {
        return fixer;
    }

    public void setFixer(User fixer) {
        this.fixer = fixer;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

