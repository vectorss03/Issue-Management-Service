package com.se14.service.implement1;

import com.mysql.cj.x.protobuf.MysqlxExpr;
import com.se14.domain.*;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;
import com.se14.service.ProjectService;

import java.util.*;

public class ProjectServiceImplement implements ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    public ProjectServiceImplement(ProjectRepository projectRepo, UserRepository userRepo) {
        this.projectRepository = projectRepo;
        this.userRepository = userRepo;
        // 필요한 필드를 여기에 초기화
        // 예: this.anotherRepo = anotherRepo;
    }
    @Override
    public List<Project> listProject() {
        try {
            List<Project> tmp = projectRepository.findAll();
            if(tmp.isEmpty())
            {
                System.out.println("Empty Project");
                return null;
            }
            return tmp;
        } catch (Exception e) {
            // Handle the exception, possibly logging it and returning an empty list or null
            e.printStackTrace();
            throw e;
            //return null;
        }
    }

    @Override
    public Project createProject(User creator, String name, String description) {
        try {
            // Create a new Project instance
            Project newProject = new Project();
            newProject.setProjectTitle(name);
            newProject.setProjectDescription(description);

            // Add the creator as an admin to the project
            addMemberToProject(newProject, creator, Arrays.asList(UserRole.ADMIN));


            // Save the project to the repository
            //projectRepository.save(newProject);
            return newProject;
        } catch (Exception e) {
            // Handle the exception, possibly logging it and returning null or throwing a custom exception
            e.printStackTrace();
            throw e;
            //return null;
        }
    }

    @Override
    public void addMemberToProject(Project project, User user, List<UserRole> roles) {
        try {
            if (!project.getMembers().containsKey(user)) {
                project.getMembers().put(user, new ArrayList<>());
            }
            List<UserRole> existingRoles = project.getMembers().get(user);
            for (UserRole role : roles) {
                if (!existingRoles.contains(role)) {
                    existingRoles.add(role);
                } else {
                    System.out.println("User already has role: " + role);
                }
            }
            projectRepository.save(project);
        } catch (Exception e) {
            // Handle the exception, possibly logging it
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public Map<String, Object> getStatistic(Project project) {
        Map<String, Object> statistics = new HashMap<>();
        Map<Calendar, List<Issue>> dateIssuesMap = new HashMap<>();
        Map<IssuePriority, Integer> priorityCountMap = new HashMap<>();
        Map<IssueStatus, Integer> statusCountMap = new HashMap<>();

        List<Issue> issues = project.getIssues();

        for (Issue issue : issues) {
            // Date-based statistics
            Calendar issueDate = Calendar.getInstance();
            issueDate.setTime(issue.getReportedDate());
            issueDate.set(Calendar.HOUR_OF_DAY, 0);
            issueDate.set(Calendar.MINUTE, 0);
            issueDate.set(Calendar.SECOND, 0);
            issueDate.set(Calendar.MILLISECOND, 0);
            Calendar normalizedDate = (Calendar) issueDate.clone();
            dateIssuesMap.computeIfAbsent(normalizedDate, k -> new ArrayList<>()).add(issue);

            // Priority-based statistics
            priorityCountMap.put(issue.getPriority(), priorityCountMap.getOrDefault(issue.getPriority(), 0) + 1);

            // Status-based statistics
            statusCountMap.put(issue.getStatus(), statusCountMap.getOrDefault(issue.getStatus(), 0) + 1);
        }

        statistics.put("dateIssues", dateIssuesMap);
        statistics.put("priorityCount", priorityCountMap);
        statistics.put("statusCount", statusCountMap);

        return statistics;
    }

    @Override
    public List<User> listUser(Project project, UserRole role) {
        List<User> usersWithRole = new ArrayList<User>();

        // Get the members of the project
        Map<User, List<UserRole>> members = project.getMembers();

        // Iterate through the members and check their roles
        for (Map.Entry<User, List<UserRole>> entry : members.entrySet()) {
            User user = entry.getKey();
            List<UserRole> roles = entry.getValue();

            // If the user has the specified role, add them to the list
            if (roles.contains(role)) {
                usersWithRole.add(user);
            }
        }
        return usersWithRole;
    }

    @Override
    public boolean hasUser(Project project,User user) {
        // Get the members of the project
        Map<User, List<UserRole>> members = project.getMembers();

        // Check if the user is in the project's members map
        return members.containsKey(user);
    }
    @Override
    public Project findProjectById(long id) {
        return projectRepository.findById(id).orElse(null);
    }
    @Override
    public List<Project> findProjectByUser(User user) {
        List<Project> allProjects = projectRepository.findAll();
        List<Project> userProjects = new ArrayList<>();

        for (Project project : allProjects) {
            if (project.getMembers().containsKey(user)) {
                userProjects.add(project);
            }
        }
        return userProjects;
    }
}
