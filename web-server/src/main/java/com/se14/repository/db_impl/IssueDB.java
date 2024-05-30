package com.se14.repository.db_impl;

import com.se14.domain.Comment;
import com.se14.domain.Issue;
import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.repository.IssueRepository;
import com.se14.repository.CommentRepository;
import com.se14.domain.IssueStatus;
import com.se14.domain.IssuePriority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IssueDB implements IssueRepository {
    private Connection connection;
    private CommentRepository commentRepository;

    // Constructor to initialize the database connection
    public IssueDB(Connection connection, CommentRepository commentRepository) {
        this.connection = connection;
        this.commentRepository = commentRepository;
    }

    @Override
    public Issue save(Issue issue, Project project) {
        // 이슈가 이미 있는지 확인
        Optional<Issue> existingIssue = findById(issue.getIssueId());
        boolean issueExistsInProject = project.getIssues().stream().anyMatch(existing -> existing.getIssueId() == issue.getIssueId());
        if (existingIssue.isPresent() && issueExistsInProject) {
            // 없으면 update.
            return update(issue, project);
        } else {
            // 새로운 이슈 insert.
            // 이슈 id가 없거나 충돌하면 새로운 id 생성.
            if (issue.getIssueId() <= 0 || existingIssue.isPresent()) {
                issue.setIssueId(generateUniqueIssueId());
            }
            String sql = "INSERT INTO issues (issue_id, title, description, status, priority, reported_date, reporter_id, fixer_id, assignee_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, issue.getIssueId());
                statement.setString(2, issue.getTitle());
                statement.setString(3, issue.getDescription());
                statement.setString(4, issue.getStatus().name());
                statement.setString(5, issue.getPriority().name());
                statement.setDate(6, new java.sql.Date(issue.getReportedDate().getTime()));
                statement.setInt(7, issue.getReporter().getUserId());
                statement.setInt(8, issue.getFixer().getUserId());
                statement.setInt(9, issue.getAssignee().getUserId());
                statement.setString(10, String.valueOf(project.getProjectId()));
                statement.executeUpdate();

                // 이슈에 달려있는 코멘트 가져와서 코멘트 리포에 저장.
                for (Comment comment : issue.getComments()) {
                    commentRepository.save(comment, issue);
                }

                return issue;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    //Issue DB를 보고 column마다 generated id  1 increment.
    private int generateUniqueIssueId() {
        String sql = "SELECT MAX(issue_id) AS max_id FROM issues";
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

    private Issue update(Issue issue, Project project) {
        String sql = "UPDATE issues SET title = ?, description = ?, status = ?, priority = ?, reported_date = ?, reporter_id = ?, fixer_id = ?, assignee_id = ? WHERE issue_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, issue.getTitle());
            statement.setString(2, issue.getDescription());
            statement.setString(3, issue.getStatus().name());
            statement.setString(4, issue.getPriority().name());
            statement.setDate(5, new java.sql.Date(issue.getReportedDate().getTime()));
            statement.setInt(6, issue.getReporter().getUserId());
            statement.setInt(7, issue.getFixer().getUserId());
            statement.setInt(8, issue.getAssignee().getUserId());
            statement.setInt(9, issue.getIssueId());
            statement.executeUpdate();

            for (Comment comment : issue.getComments()) {
                commentRepository.save(comment, issue);
            }

            return issue;
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
                Issue issue = mapResultSetToIssue(resultSet);

                //이슈에 달려있는 코멘트 가져와서 리스트 생성.
                List<Comment> comments = commentRepository.findByIssue(issue);
                issue.setComments(comments);
                return Optional.of(issue);
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
                Issue issue = mapResultSetToIssue(resultSet);

                //이슈에 달려있는 코멘트 가져와서 리스트 생성.
                List<Comment> comments = commentRepository.findByIssue(issue);
                issue.setComments(comments);
                issues.add(issue);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return issues;
    }

    // DB에서 불러온 정보 issue 객체에 mapping 하는 helper 메소드
    private Issue mapResultSetToIssue(ResultSet resultSet) throws SQLException {
        User reporter = new User();
        reporter.setUserId(resultSet.getInt("reporter_id"));
        User fixer = new User();
        fixer.setUserId(resultSet.getInt("fixer_id"));
        User assignee = new User();
        assignee.setUserId(resultSet.getInt("assignee_id"));

        return new Issue(
                resultSet.getString("title"),
                resultSet.getString("description"),
                IssueStatus.valueOf(resultSet.getString("status")),
                IssuePriority.valueOf(resultSet.getString("priority")),
                resultSet.getDate("reported_date"),
                reporter,
                fixer,
                assignee,
                new ArrayList<>()
        );
    }
}
