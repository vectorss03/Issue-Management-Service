package com.se14.service.implement1;

import com.se14.domain.Issue;
import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserRole;
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
            addMemberToProject(newProject, creator, UserRole.ADMIN);


            // Save the project to the repository
            projectRepository.save(newProject);
            return newProject;
        } catch (Exception e) {
            // Handle the exception, possibly logging it and returning null or throwing a custom exception
            e.printStackTrace();
            throw e;
            //return null;
        }
    }

    @Override
    public void addMemberToProject(Project project, User user, UserRole role) {
        try {
            if (!project.getMembers().containsKey(user)) {
                project.getMembers().put(user, new ArrayList<>());
            }
            if (!project.getMembers().get(user).contains(role)) {
                project.getMembers().get(user).add(role);
            }else {
                System.out.println("User already has role");
            }
            projectRepository.save(project);
        } catch (Exception e) {
            // Handle the exception, possibly logging it
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Map<Calendar, List<Issue>> getStatistic(Project project) {
        Map<Calendar, List<Issue>> issueStatistics = new HashMap<>();
        List<Issue> issues = project.getIssues();

        for (Issue issue : issues) {
            // Create a Calendar instance and set it to the issue's created date
            Calendar issueDate = Calendar.getInstance();
            issueDate.setTime(issue.getReportedDate());

            // Normalize the Calendar to remove time part for date-only comparison
            issueDate.set(Calendar.HOUR_OF_DAY, 0);
            issueDate.set(Calendar.MINUTE, 0);
            issueDate.set(Calendar.SECOND, 0);
            issueDate.set(Calendar.MILLISECOND, 0);

            // Create a new instance for the key to avoid reference equality issues
            Calendar normalizedDate = (Calendar) issueDate.clone();

            // Add the issue to the corresponding date in the map
            if (!issueStatistics.containsKey(normalizedDate)) {
                issueStatistics.put(normalizedDate, new ArrayList<Issue>());
            }
            issueStatistics.get(normalizedDate).add(issue);
        }

        return issueStatistics;
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
}
