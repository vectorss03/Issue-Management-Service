package com.se14.repository.db_impl;

import com.se14.domain.Comment;
import com.se14.domain.Issue;
import com.se14.domain.IssuePriority;
import com.se14.domain.IssueStatus;
import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserRole;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;
import com.se14.repository.CommentRepository;


import java.sql.*;
import java.util.*;

public class ProjectDB implements ProjectRepository {
    private Connection connection;
    private UserRepository userRepository; // Add UserRepository dependency
    private CommentRepository commentRepository; // Add CommentRepository dependency

    // Constructor to initialize the database connection and dependencies
    public ProjectDB(Connection connection, UserRepository userRepository, CommentRepository commentRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    public Project save(Project project) {
        if (project.getProjectId() == null) {
            project.setProjectId(generateUniqueProjectId());
        }

        //DB 상에 프로젝트가 이미 있을 때
        if (projectExists((long)project.getProjectId())) {
            updateProject(project);
        } else {
            insertProject(project);
        }

        try {
            saveProjectMembers(project);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            saveProjectIssues(project);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return project;
    }

    private boolean projectExists(Long projectId) {
        String sql = "SELECT COUNT(*) FROM projects WHERE project_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void insertProject(Project project) {
        String sql = "INSERT INTO projects (project_id, title, description) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, project.getProjectId().toString());
            statement.setString(2, project.getProjectTitle());
            statement.setString(3, project.getProjectDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProject(Project project) {
        String sql = "UPDATE projects SET title = ?, description = ? WHERE project_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, project.getProjectTitle());
            statement.setString(2, project.getProjectDescription());
            statement.setLong(3, project.getProjectId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveProjectMembers(Project project) throws SQLException {
        String memberSql = "INSERT INTO project_members (project_id, user_id) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE user_id = user_id";
        String roleSql = "INSERT INTO project_member_roles (project_id, user_id, user_role) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE user_role = VALUES(user_role)";
        try (PreparedStatement memberStatement = connection.prepareStatement(memberSql);
             PreparedStatement roleStatement = connection.prepareStatement(roleSql)) {
            for (Map.Entry<User, List<UserRole>> entry : project.getMembers().entrySet()) {
                User user = entry.getKey();
                memberStatement.setString(1, project.getProjectId().toString());
                memberStatement.setInt(2, user.getUserId());
                memberStatement.addBatch();

                for (UserRole role : entry.getValue()) {
                    roleStatement.setString(1, project.getProjectId().toString());
                    roleStatement.setInt(2, user.getUserId());
                    roleStatement.setString(3, role.toString());
                    roleStatement.addBatch();
                }
            }
            memberStatement.executeBatch();
            roleStatement.executeBatch();
        }
    }


    private void saveProjectIssues(Project project) throws SQLException {
        String sql = "INSERT INTO issues (issue_id, title, description, status, priority, date, project_id, reporter_id, fixer_id, assignee_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE title = VALUES(title), description = VALUES(description), status = VALUES(status), priority = VALUES(priority), date = VALUES(date), reporter_id = VALUES(reporter_id), fixer_id = VALUES(fixer_id), assignee_id = VALUES(assignee_id)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Issue issue : project.getIssues()) {
                statement.setInt(1, issue.getIssueId() != -1 ? issue.getIssueId() : generateUniqueIssueId());
                statement.setString(2, issue.getTitle());
                statement.setString(3, issue.getDescription());
                statement.setString(4, issue.getStatus().toString());
                statement.setString(5, issue.getPriority().toString());
                statement.setDate(6, new java.sql.Date(issue.getReportedDate().getTime()));
                statement.setString(7, project.getProjectId().toString());
                statement.setInt(8, issue.getReporter().getUserId());
                statement.setInt(9, issue.getFixer() != null ? issue.getFixer().getUserId() : null);
                statement.setInt(10, issue.getAssignee() != null ? issue.getAssignee().getUserId() : null);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
    private int generateUniqueIssueId() {
        String sql = "SELECT MAX(is) AS max_id FROM issues";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    private int generateUniqueProjectId() {
        String sql = "SELECT MAX(project_id) AS max_id FROM projects";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
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
        String memberSql = "SELECT user_id FROM project_members WHERE project_id = ?";
        String roleSql = "SELECT user_role FROM project_member_roles WHERE project_id = ? AND user_id = ?";

        Map<User, List<UserRole>> members = new HashMap<>();

        try (PreparedStatement memberStatement = connection.prepareStatement(memberSql)) {
            memberStatement.setInt(1, projectId);
            ResultSet memberResultSet = memberStatement.executeQuery();

            while (memberResultSet.next()) {
                int userId = memberResultSet.getInt("user_id");
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    List<UserRole> roles = new ArrayList<>();
                    try (PreparedStatement roleStatement = connection.prepareStatement(roleSql)) {
                        roleStatement.setInt(1, projectId);
                        roleStatement.setInt(2, userId);
                        ResultSet roleResultSet = roleStatement.executeQuery();

                        while (roleResultSet.next()) {
                            UserRole role = UserRole.valueOf(roleResultSet.getString("user_role"));
                            roles.add(role);
                        }
                    }
                    members.put(user, roles);
                }
            }
        }
        return members;
    }


    private List<Issue> getProjectIssues(Integer projectId) throws SQLException {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        List<Issue> issues = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Issue issue = new Issue();
                issue.setIssueId(resultSet.getInt("issue_id"));
                issue.setTitle(resultSet.getString("title"));
                issue.setDescription(resultSet.getString("description"));
                issue.setStatus(IssueStatus.valueOf(resultSet.getString("status")));
                issue.setPriority(IssuePriority.valueOf(resultSet.getString("priority")));
                issue.setReportedDate(resultSet.getDate("date"));

                // Fetch and set the reporter, fixer, and assignee
                issue.setReporter(userRepository.findById(resultSet.getInt("reporter_id")).orElse(null));
                issue.setFixer(resultSet.getObject("fixer_id") != null ? userRepository.findById(resultSet.getInt("fixer_id")).orElse(null) : null);
                issue.setAssignee(resultSet.getObject("assignee_id") != null ? userRepository.findById(resultSet.getInt("assignee_id")).orElse(null) : null);

                // Fetch and set the comments
                issue.setComments(commentRepository.findByIssue(issue));

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
                project.setMembers(getProjectMembers(project.getProjectId()));
                project.setIssues(getProjectIssues(project.getProjectId()));
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
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Project project = new Project();
                project.setProjectId(resultSet.getInt("project_id"));
                project.setProjectTitle(resultSet.getString("title"));
                project.setProjectDescription(resultSet.getString("description"));
                project.setMembers(getProjectMembers(project.getProjectId()));
                project.setIssues(getProjectIssues(project.getProjectId()));
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
