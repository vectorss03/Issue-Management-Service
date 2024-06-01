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
    private String reporter;
    private String fixer;
    private String assignee;

    // Constructors
    public SearchCriteria() { }
    public SearchCriteria(String title, String description, IssueStatus status, IssuePriority priority,
                          Date startDate, Date endDate, String reporter, String fixer, String assignee) {
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

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getFixer() {
        return fixer;
    }

    public void setFixer(String fixer) {
        this.fixer = fixer;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
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

