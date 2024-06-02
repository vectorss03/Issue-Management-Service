package com.se14.domain;

import com.se14.dto.IssueDTO;
import com.se14.dto.ProjectDTO;
import com.se14.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserSession {

    private UserDTO currentUser;
    @Getter @Setter
    private List<UserRole> userRoles;
    private ProjectDTO currentProject;
    private IssueDTO currentIssue;

    // Default constructor
    public UserSession() {
    }
    @Override
    public String toString()
    {
        return "User: "+this.currentUser+", Project: "+this.currentProject+", Issue: "+this.currentIssue;
    }

    // Parameterized constructor
    public UserSession(UserDTO currentUser, ProjectDTO currentProject, IssueDTO currentIssue) {
        this.currentUser = currentUser;
        this.currentProject = currentProject;
        this.currentIssue = currentIssue;
    }

    // Getter and setter for currentUser
    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }

    // Getter and setter for currentProject
    public ProjectDTO getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(ProjectDTO currentProject) {
        this.currentProject = currentProject;
    }

    // Getter and setter for currentIssue
    public IssueDTO getCurrentIssue() {
        return currentIssue;
    }

    public void setCurrentIssue(IssueDTO currentIssue) {
        this.currentIssue = currentIssue;
    }
}
