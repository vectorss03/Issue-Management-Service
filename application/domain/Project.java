package application.domain;

import java.util.List;
import java.util.Map;

public class Project {
    private String projectId;
    private String projectName;
    private Map<User, List<UserRole>> members;
    private List<Issue> issues;

    // Default constructor
    public Project() {
    }

    // Parameterized constructor
    public Project(String projectId, String projectName, Map<User, List<UserRole>> members, List<Issue> issues) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.members = members;
        this.issues = issues;
    }

    // Getter and setter for projectId
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    // Getter and setter for projectName
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
}
