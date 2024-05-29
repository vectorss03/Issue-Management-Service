package com.se14.domain;

import java.util.Date;

public class Comment {
    private String commentTitle;
    private String text;
    private Date timestamp;
    private User author;
    private int id;

    @Override
    public String toString()
    {
        return this.commentTitle + ":" +this.text;
    }
    // Default constructor
    public Comment() {
    }

    // Parameterized constructor
    public Comment(String commentTitle, String text, Date timestamp, User author) {
        this.commentTitle = commentTitle;
        this.text = text;
        this.timestamp = timestamp;
        this.author = author;
    }

    // Getter and setter for commentId
    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    //DB 구현을 위해 CommentID 따로 추가
    public void setID(int id){this.id = id;}

    public long getID(){ return this.id;}


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
