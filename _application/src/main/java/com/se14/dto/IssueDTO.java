package com.se14.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.se14.domain.Issue;
import com.se14.domain.IssuePriority;
import com.se14.domain.IssueStatus;

import java.text.SimpleDateFormat;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IssueDTO {
    @JsonProperty
    private int issueId;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty
    private IssueStatus status;
    @JsonProperty
    private IssuePriority priority;
    @JsonProperty
    private String reportedDate;
    @JsonProperty
    private String reporter;
    @JsonProperty
    private String fixer;
    @JsonProperty
    private String assignee;

    public IssueDTO(Issue issue) {
        this.issueId = issue.getIssueId();
        this.title = issue.getTitle();
        this.description = issue.getDescription();
        this.status = issue.getStatus();
        this.priority = issue.getPriority();
        this.reportedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(issue.getReportedDate());
        this.reporter = issue.getReporter() == null ? null : issue.getReporter().getUsername();
        this.fixer = issue.getFixer() == null ? null : issue.getFixer().getUsername();
        this.assignee = issue.getAssignee() == null ? null : issue.getAssignee().getUsername();
    }
}
