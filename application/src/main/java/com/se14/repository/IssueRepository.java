package com.se14.repository;

import com.se14.domain.Issue;
import com.se14.domain.Project;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    Issue save(Issue issue);
    Optional<Issue> findById(long id);
    List<Issue> findByProject(Project project);
}
