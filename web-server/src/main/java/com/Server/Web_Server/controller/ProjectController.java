package com.Server.Web_Server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.se14.domain.Project;
import com.se14.service.ProjectService;
import com.se14.service.IssueService;
import com.se14.service.UserService;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final IssueService IssueService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService) {

        this.projectService = projectService;
        this.issueService = issueService;
    }

    @GetMapping
    public List<Project> projectList(HttpSession session) {
        User user = userService.findByUsername(session.getAttribute("username"));

        List<Project> projects = projectService.listProject();
        List<Project> accessibleProjects = new ArrayList<>();
        for (Project project : projects) {
            if (projectService.hasUser(project, user)) {
                accessibleProjects.add(project);
            }
        }
        return accessibleProjects;
    }
    //ProjectService쪽 수정되었을 때 추가 작업 필요

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody Map<String, String> params) {

        User creator = userService.findByUsername(session.getAttribute("username"));
        String title = params.get("title").toString();
        String description = params.get("description").toString();

        Project newProject = projectService.createProject(creator, title, description);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/issues")
    public List<Issue> issueList(@PathVariable int projectId) {

        <List>issue issues = issueService.searchIssues(projectId,null);

        return issues;
    }

    @PostMapping("/{projectId}/issues")
    public ResponseEntity<Void> createIssue(@PathVariable int projectId, @RequestBody Map<String, String> params, HttpSession session) {

        Project currentProject = projectService.findProjectById(projectId);
        User user = userService.findByUsername(session.getAttribute("username"));

        String title = params.get("title").toString();
        String description = params.get("description").toString();
        IssuePriority priority = (IssuePriority)params.get("priority");

        Issue newIssue = issueService.reportIssue(currentProject, user, title, description, priority);

        return newIssue;

    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public Issue getIssue(@PathVariable int projectId, @PathVariable int issueId) {

        Project currentProject = projectService.findProjectById(projectId);
        List<Issue> issues = issueService.searchIssues(currentProject, null);
        Issue detailedIssue;

        for (Issue issue : issues) {
            if (issue.getTitle().equals(issueTitle)) {
                detailedIssue = issue;
                break;
            }
        }
        return deatiledIssue;
    }

    @PostMapping("/{projectId}/issues/{issueId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable int projectId, @PathVariable int issueId, @RequestBody Map<String, String> params, HttpSession session) {

        User user = userService.findByUsername(session.getAttribute("username") != null ? session.getAttribute("username"): "Unknown");
        Project currentProject = projectService.findProjectById(projectId);
        List<Issue> issues = issueService.searchIssues(currentProject, null);

        Issue detailedIssue;
        for (Issue issue : issues) {
            if (issue.getTitle().equals(issueTitle)) {
                detailedIssue = issue;
                break;
            }
        }
        String content = params.get("content").toString();

        issueService.addComment(currentProject, user, detailedIssue, user.getUsername(), content);
        //void addComment(Project project, User commenter, Issue issue, String commentTitle, String commentText);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{projectId}/issues/{issueId}/assignee")
    public ResponseEntity<Void> assignDeveloper(@PathVariable int projectId, @PathVariable int issueId, @RequestBody Map<String, String> params) {

        Project currentProject = projectService.findProjectById(projectId);
        List<Issue> issues = issueService.searchIssues(currentProject, null);
        Issue detailedIssue;
        User assignee = userService.findByUsername(params.get("assignee"));

        for (Issue issue : issues) {
            if (issue.getTitle().equals(issueTitle)) {
                detailedIssue = issue;
                break;
            }
        }

        issueService.assignIssue(currentProject, detailedIssue, assignee);
        //확인 필요함
        //void assignIssue(Project project, User assigner, Issue issue, User assignee);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{projectId}/issues/{issueId}/status")
    public ResponseEntity<Void> changeState(@PathVariable int projectId, @PathVariable int issueId, @RequestBody Map<String, String> params, HttpSession session) {

        Project currentProject = projectService.findProjectById(projectId);
        List<Issue> issues = issueService.searchIssues(currentProject, null);

        Issue detailedIssue;
        for (Issue issue : issues) {
            if (issue.getTitle().equals(issueTitle)) {
                detailedIssue = issue;
                break;
            }
        }
        User user = userService.findByUsername(session.getAttribute("username"));
        IssueStatus status = (IssueStatus)params.get("status");

        issueService.updateIssueStatus(currentProject, user, detailedIssue, status);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/users")
    public List<User> userList(@PathVariable int projectId) {
        List<User> users = projectService.listUser();
        

        return users;
    }

    @GetMapping("/{projectId}/users/join")
    public List<User> userJoinList(@PathVariable int projectId) {
        List<user> users = userService.listAllUser();

        return users;
    }
    //


    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> addUser(@PathVariable int projectId, @RequestBody Map<String, String> params) {

        Project currentProject = projectService.findProjectById(projectId);
        List<Issue> issues = issueService.searchIssues(currentProject, null);
        Issue detailedIssue;
        for (Issue issue : issues) {
            if (issue.getTitle().equals(issueTitle)) {
                detailedIssue = issue;
                break;
            }
        }
        User user = userService.findByUsername(params.get("username"));
        UserRole role = (UserRole)params.get("role");

        //void addMemberToProject(Project project,User user, UserRole role);
        projectService.addMemberToProject(currentProject, user, role);
        //Vue쪽에서 role 처리 해야할 듯

        return new ResponseEntity<>(HttpStatus.OK);
    }

}