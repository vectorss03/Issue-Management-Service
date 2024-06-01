package com.se14.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
    private Integer projectId;
    private String projectTitle;
    private String projectDescription;
    private Map<User, List<UserRole>> members;
    private List<Issue> issues;

    // Default constructor
    public Project() {
        members = new HashMap<User,List<UserRole>>();
        issues = new ArrayList<Issue>();

    }
    @Override
    public String toString()
    {
        return String.valueOf(this.projectId);
    }


    // Parameterized constructor
    public Project(Integer projectId, String projectTitle, String projectDescription, Map<User, List<UserRole>> members, List<Issue> issues) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.members = members;
        this.issues = issues;
    }

    // Getter and setter for projectId
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    // Getter and setter for projectName
    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    // Getter and setter for members
    public Map<User, List<UserRole>> getMembers() {
        return members;
    }

    public void setMembers(Map<User, List<UserRole>> members) {
        this.members = members;
    }

    // Getter and setter for issues
    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
}
