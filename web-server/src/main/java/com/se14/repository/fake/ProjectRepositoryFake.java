package com.se14.repository.fake;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserRole;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProjectRepositoryFake implements ProjectRepository {

    private final Map<Integer, Project> projects = new HashMap<>();
    public UserRepository userRepositoryFake;

    public ProjectRepositoryFake(UserRepository userRepositoryFake) {
        // Create 3 projects with IDs 1, 2, and 3
        for (int i = 1; i <= 3; i++) {
            Project project = new Project();
            project.setProjectId((int) i);
            project.setProjectTitle("Project " + i);
            project.setProjectDescription("Description for Project " + i);
            projects.put(i, project);
        }
        int projectId = 1;
        this.userRepositoryFake=userRepositoryFake;
        // Set up members for Project 1
        Project project1 = projects.get((Integer) projectId);
        if (project1 != null) {
            for (int i = 1; i <= 9; i++) {
                User user = userRepositoryFake.findById(i).orElse(null);
                if (user != null) {
                    List<UserRole> roles = new ArrayList<>();
                    if (i == 1) {
                        roles.add(UserRole.ADMIN);
                        roles.add(UserRole.PROJECT_LEAD);
                        roles.add(UserRole.DEVELOPER);
                        roles.add(UserRole.TESTER);
                    } else {
                        if ((i & 1) != 0) roles.add(UserRole.PROJECT_LEAD);
                        if ((i & 2) != 0) roles.add(UserRole.DEVELOPER);
                        if ((i & 4) != 0) roles.add(UserRole.TESTER);
                    }
                    project1.getMembers().put(user, roles);
                }
            }
        }
    }

    @Override
    public Project save(Project project) {
        if (project.getProjectId() == null) {
            Integer newId = projects.keySet().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0) + 1;
            project.setProjectId((Integer) newId);
        }
        projects.put(project.getProjectId(), project);
        return project;
    }

    @Override
    public Optional<Project> findById(Integer id) {
        return Optional.ofNullable(projects.get(id));
    }

    @Override
    public List<Project> searchByTitle(String title) {
        List<Project> result = new ArrayList<>();
        for (Project project : projects.values()) {
            if (project.getProjectTitle().contains(title)) {
                result.add(project);
            }
        }
        return result;
    }

    @Override
    public List<Project> findAll() {
        return new ArrayList<>(projects.values());
    }
}
