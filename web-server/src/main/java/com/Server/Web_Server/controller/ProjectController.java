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

//import ProjectServiceImplement
//import ProjectRepository

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private List<User> users = new ArrayList<>();

    @GetMapping
    public List<Project> projectList(HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = getUser(username);

        return projects.stream()
                .filter(project -> user.getProjects().contains(project.getProjectId()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody Map<String, String> params) {
        int newId = projects.isEmpty() ? 1 : projects.get(projects.size() - 1).getProjectId() + 1;
        Project newProject = new Project();
        newProject.setProjectId(newId);
        newProject.setTitle(params.get("title"));
        newProject.setDescription(params.get("description"));
        newProject.setIssues(new ArrayList<>());

        projects.add(newProject);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/issues")
    public List<Issue> issueList(@PathVariable int projectId) {
        return projects.stream()
                .filter(project -> project.getProjectId() == projectId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"))
                .getIssues();
    }

    @PostMapping("/{projectId}/issues")
    public ResponseEntity<Void> createIssue(@PathVariable int projectId, @RequestBody Map<String, String> params, HttpSession session) {
        Project project = projects.stream()
                .filter(p -> p.getProjectId() == projectId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));

        int newId = project.getIssues().isEmpty() ? 1 : project.getIssues().get(project.getIssues().size() - 1).getIssueId() + 1;

        Issue newIssue = new Issue();
        newIssue.setIssueId(newId);
        newIssue.setTitle(params.get("title"));
        newIssue.setDescription(params.get("description"));
        newIssue.setStatus("new");
        newIssue.setPriority(params.get("priority"));
        newIssue.setAssignee(null);
        newIssue.setFixer(null);
        newIssue.setReporter(session.getAttribute("username") != null ? (String) session.getAttribute("username") : "Unknown");
        newIssue.setReportedDate(java.time.LocalDateTime.now().toString());
        newIssue.setComments(new ArrayList<>());

        project.getIssues().add(newIssue);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public Issue getIssue(@PathVariable int projectId, @PathVariable int issueId) {
        Project project = projects.stream()
                .filter(p -> p.getProjectId() == projectId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return project.getIssues().stream()
                .filter(issue -> issue.getIssueId() == issueId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Issue not found"));
    }

    @PostMapping("/{projectId}/issues/{issueId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable int projectId, @PathVariable int issueId, @RequestBody Map<String, String> params, HttpSession session) {
        Issue issue = getIssue(projectId, issueId);

        int newId = issue.getComments().isEmpty() ? 1 : issue.getComments().get(issue.getComments().size() - 1).getCommentId() + 1;

        Comment newComment = new Comment();
        newComment.setCommentId(newId);
        newComment.setContent(params.get("content"));
        newComment.setAuthor(session.getAttribute("username") != null ? (String) session.getAttribute("username") : "Unknown");
        newComment.setTimestamp(java.time.LocalDateTime.now().toString());

        issue.getComments().add(newComment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{projectId}/issues/{issueId}/assignee")
    public ResponseEntity<Void> assignDeveloper(@PathVariable int projectId, @PathVariable int issueId, @RequestBody Map<String, String> params) {
        Issue issue = getIssue(projectId, issueId);
        issue.setAssignee(params.get("assignee"));
        issue.setStatus("assigned");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{projectId}/issues/{issueId}/status")
    public ResponseEntity<Void> changeState(@PathVariable int projectId, @PathVariable int issueId, @RequestBody Map<String, String> params, HttpSession session) {
        Issue issue = getIssue(projectId, issueId);
        issue.setStatus(params.get("status"));
        if ("fixed".equals(params.get("status"))) {
            issue.setFixer(session.getAttribute("username") != null ? (String) session.getAttribute("username") : "Unknown");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/users")
    public List<User> userList(@PathVariable int projectId) {
        return users.stream()
                .filter(user -> user.getProjects().contains(projectId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{projectId}/users/join")
    public List<User> userJoinList(@PathVariable int projectId) {
        return users.stream()
                .filter(user -> !user.getProjects().contains(projectId))
                .collect(Collectors.toList());
    }

    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> addUser(@PathVariable int projectId, @RequestBody Map<String, String> params) {

        User user = getUser(params.get("username"));
        user.getProjects().add(projectId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getUser(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}