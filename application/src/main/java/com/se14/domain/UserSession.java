package com.se14.domain;

import lombok.Data;

@Data
public class UserSession {
    private User currentUser;
    private Project currentProject;
    private Issue currentIssue;
}
