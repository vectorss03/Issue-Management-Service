package com.se14.repository.db_impl;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserRole;
import com.se14.domain.Issue;
import com.se14.domain.IssueStatus;
import com.se14.domain.IssuePriority;
import com.se14.domain.Comment;
import com.se14.repository.ProjectRepository;
import java.util.Date;

import java.sql.*;
import java.util.*;

public class ProjectDB implements ProjectRepository {
    private Connection connection;

    // Constructor to initialize the database connection
    public ProjectDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Project save(Project project) {
        String sql = "INSERT INTO projects (project_id, title, description) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, project.getProjectId().toString());
            statement.setString(2, project.getProjectTitle());
            statement.setString(3, project.getProjectDescription());
            statement.executeUpdate();

            saveProjectMembers(project);
            saveProjectIssues(project);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }

    private void saveProjectMembers(Project project) throws SQLException {
        String sql = "INSERT INTO project_members (project_id, user_id, user_role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Map.Entry<User, List<UserRole>> entry : project.getMembers().entrySet()) {
                User user = entry.getKey();
                for (UserRole role : entry.getValue()) {
                    statement.setString(1, project.getProjectId().toString());
                    statement.setInt(2, user.getUserId());
                    statement.setString(3, role.toString());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        }
    }

    private void saveProjectIssues(Project project) throws SQLException {
        String sql = "INSERT INTO issues (title, description, status, priority, date, project_id, reporter_id, fixer_id, assignee_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Issue issue : project.getIssues()) {
                statement.setString(1, issue.getTitle());
                statement.setString(2, issue.getDescription());
                statement.setString(3, issue.getStatus().toString());
                statement.setString(4, issue.getPriority().toString());
                statement.setDate(5, new java.sql.Date(issue.getReportedDate().getTime()));
                statement.setString(6, project.getProjectId().toString());
                statement.setInt(7, issue.getReporter().getUserId());
                statement.setInt(8, issue.getFixer() != null ? issue.getFixer().getUserId() : null);
                statement.setInt(9, issue.getAssignee() != null ? issue.getAssignee().getUserId() : null);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    @Override
    public Optional<Project> findById(long id) {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        Project project = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                project = new Project();
                project.setProjectId(resultSet.getInt("project_id"));
                project.setProjectTitle(resultSet.getString("title"));
                project.setProjectDescription(resultSet.getString("description"));
                project.setMembers(getProjectMembers(project.getProjectId()));
                project.setIssues(getProjectIssues(project.getProjectId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(project);
    }

    private Map<User, List<UserRole>> getProjectMembers(Integer projectId) throws SQLException {
        String sql = "SELECT * FROM project_members WHERE project_id = ?";
        Map<User, List<UserRole>> members = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(); // Assuming you have a User class with proper fields
                user.setUserId(resultSet.getInt("user_id"));
                UserRole role = UserRole.valueOf(resultSet.getString("user_role"));
                members.computeIfAbsent(user, k -> new ArrayList<>()).add(role);
            }
        }
        return members;
    }


    private List<Issue> getProjectIssues(Integer projectId) throws SQLException {
        String sql = "SELECT * FROM issues WHERE project_id = ?";
        List<Issue> issues = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Issue issue = new Issue(); // Assuming you have an Issue class with proper fields
                //issue.setIssueId(resultSet.getInt("issue_id"));
                issue.setTitle(resultSet.getString("title"));
                issue.setDescription(resultSet.getString("description"));
                issue.setStatus(IssueStatus.valueOf(resultSet.getString("status")));
                issue.setPriority(IssuePriority.valueOf(resultSet.getString("priority")));
                issue.setReportedDate(resultSet.getDate("date"));
                // Set reporter, fixer, and assignee using similar logic
                issues.add(issue);
            }
        }
        return issues;
    }

    @Override
    public List<Project> searchByTitle(String title) {
        String sql = "SELECT * FROM projects WHERE title LIKE ?";
        List<Project> projects = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Project project = new Project();
                project.setProjectId(resultSet.getInt("project_id"));
                project.setProjectTitle(resultSet.getString("title"));
                project.setProjectDescription(resultSet.getString("description"));
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public List<Project> findAll() {
        String sql = "SELECT * FROM projects";
        List<Project> projects = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Project project = new Project();
                project.setProjectId(resultSet.getInt("project_id"));
                project.setProjectTitle(resultSet.getString("title"));
                project.setProjectDescription(resultSet.getString("description"));
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
