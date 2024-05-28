package com.se14.repository.db_impl;

import com.se14.domain.Comment;
import com.se14.domain.Issue;
import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.repository.IssueRepository;
import com.se14.domain.IssueStatus;
import com.se14.domain.IssuePriority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IssueDB implements IssueRepository {
    private Connection connection;

    // Constructor to initialize the database connection
    public IssueDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Issue save(Issue issue, Project project) {
        String sql = "INSERT INTO issues (title, description, status, priority, reported_date, reporter_id, fixer_id, assignee_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, issue.getTitle());
            statement.setString(2, issue.getDescription());
            statement.setString(3, issue.getStatus().name());
            statement.setString(4, issue.getPriority().name());
            statement.setDate(5, new java.sql.Date(issue.getReportedDate().getTime()));
            statement.setInt(6, issue.getReporter().getUserId());
            statement.setInt(7, issue.getFixer().getUserId());
            statement.setInt(8, issue.getAssignee().getUserId());
            statement.setString(9, String.valueOf(project.getProjectId()));
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                issue.setIssueId(generatedKeys.getLong(1));
                return issue;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Issue> findById(long id) {
        String sql = "SELECT * FROM issues WHERE issue_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToIssue(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Issue> findByProject(Project project) {
        List<Issue> issues = new ArrayList<>();
        String sql = "SELECT * FROM issues WHERE project_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(project.getProjectId()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                issues.add(mapResultSetToIssue(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return issues;
    }




    // Helper method to map ResultSet to Issue object
    private Issue mapResultSetToIssue(ResultSet resultSet) throws SQLException {
        User reporter = new User(); reporter.setUserId(resultSet.getInt("reporter_id"));
        User fixer = new User(); fixer.setUserId(resultSet.getInt("fixer_id"));
        User assignee = new User(); assignee.setUserId(resultSet.getInt("assignee_id"));

        return new Issue(

                resultSet.getString("title"),
                resultSet.getString("description"),
                IssueStatus.valueOf(resultSet.getString("status")),
                IssuePriority.valueOf(resultSet.getString("priority")),
                resultSet.getDate("reported_date"),
                reporter,
                fixer,
                assignee,
                new ArrayList<>() // comments will be fetched separately
        );
    }
}
