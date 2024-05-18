package application.domain;

import java.util.Date;

public class Comment {
    private String commentId;
    private String text;
    private Date timestamp;
    private User author;

    // Default constructor
    public Comment() {
    }

    // Parameterized constructor
    public Comment(String commentId, String text, Date timestamp, User author) {
        this.commentId = commentId;
        this.text = text;
        this.timestamp = timestamp;
        this.author = author;
    }

    // Getter and setter for commentId
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    // Getter and setter for text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Getter and setter for timestamp
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    // Getter and setter for author
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
