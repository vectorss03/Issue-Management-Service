package com.se14.repository;

import com.se14.domain.Comment;
import com.se14.domain.Issue;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(long id);
    List<Comment> findByIssue(Issue issue);
}
