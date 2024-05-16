package com.se14.repository;

import com.se14.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);
    Optional<Project> findById(long id);
    List<Project> searchByTitle(String title); // returns all projects have title as substring
    List<Project> findAll();
}
