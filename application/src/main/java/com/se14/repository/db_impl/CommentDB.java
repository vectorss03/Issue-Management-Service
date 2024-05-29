package com.se14.repository.db_impl;

import com.se14.domain.Comment;
import com.se14.domain.Issue;
import com.se14.repository.CommentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDB implements CommentRepository {
    private Connection connection;

    public CommentDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Comment save(Comment comment, Issue issue) {
        // 코멘트가 이미 있는지 확인
        Optional<Comment> existingComment = findById(comment.getID());
        if (existingComment.isPresent()) {
            // 코멘트가 있으면, update.
            return update(comment);
        } else {
            // 없으면 새로 insert.
            String sql = "INSERT INTO comments (title, text, timestamp, issue_id, author_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, comment.getCommentTitle());
                statement.setString(2, comment.getText());
                statement.setTimestamp(3, new Timestamp(comment.getTimestamp().getTime()));
                statement.setLong(4, issue.getIssueId());
                statement.setInt(5, comment.getAuthor().getUserId());
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    comment.setID(generatedKeys.getInt(1));
                    return comment;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Optional<Comment> findById(long id) {
        String sql = "SELECT * FROM comments WHERE comment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToComment(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Comment> findByIssue(Issue issue) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE issue_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, issue.getIssueId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comments.add(mapResultSetToComment(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return comments;
    }

    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setID(resultSet.getInt("comment_id"));
        comment.setCommentTitle(resultSet.getString("title"));
        comment.setText(resultSet.getString("text"));
        comment.setTimestamp(resultSet.getTimestamp("timestamp"));

        return comment;
    }

    private Comment update(Comment comment) {
        String sql = "UPDATE comments SET title = ?, text = ?, timestamp = ? WHERE comment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getCommentTitle());
            statement.setString(2, comment.getText());
            statement.setTimestamp(3, new Timestamp(comment.getTimestamp().getTime()));
            statement.setLong(4, comment.getID());
            statement.executeUpdate();
            return comment;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
