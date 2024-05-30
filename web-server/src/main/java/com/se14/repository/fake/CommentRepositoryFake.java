package com.se14.repository.fake;

import com.se14.domain.Comment;
import com.se14.domain.Issue;
import com.se14.repository.CommentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CommentRepositoryFake implements CommentRepository {

    private final Map<Long, Comment> comments = new HashMap<>();
    private long currentId = 1;

    @Override
    public Comment save(Comment comment, Issue issue) {
        comments.put(currentId++, comment);
        issue.getComments().add(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(comments.get(id));
    }

    @Override
    public List<Comment> findByIssue(Issue issue) {
        return issue.getComments();
    }
}
