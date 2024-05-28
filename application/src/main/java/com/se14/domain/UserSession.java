package com.se14.domain;

public class UserSession {

    private User currentUser;
    private Project currentProject;
    private Issue currentIssue;

    // Default constructor
    public UserSession() {
    }
    @Override
    public String toString()
    {
        return "User: "+this.currentUser+", Project: "+this.currentProject+", Issue: "+this.currentIssue;
    }

    // Parameterized constructor
    public UserSession(User currentUser, Project currentProject, Issue currentIssue) {
        this.currentUser = currentUser;
        this.currentProject = currentProject;
        this.currentIssue = currentIssue;
    }

    // Getter and setter for currentUser
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Getter and setter for currentProject
    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    // Getter and setter for currentIssue
    public Issue getCurrentIssue() {
        return currentIssue;
    }

    public void setCurrentIssue(Issue currentIssue) {
        this.currentIssue = currentIssue;
    }
}
