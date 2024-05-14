package com.se14.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Issue {
    private long id;
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private Member reporter;
    private Member fixer;
    private List<Comment> comments;
}
