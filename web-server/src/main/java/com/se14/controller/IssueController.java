package com.se14.controller;

import com.se14.domain.Issue;
import com.se14.domain.IssueStatus;
import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.dto.IssueDTO;
import com.se14.dto.CommentDTO;
import com.se14.dto.UserDTO;
import com.se14.service.DeveloperRecommendationService;
import com.se14.service.IssueService;
import com.se14.service.ProjectService;
import com.se14.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}/issues/{issueId}")
public class IssueController {

    private final IssueService issueService;
    private final ProjectService projectService;
    private final UserService userService;
    private final DeveloperRecommendationService developerRecommendationService;

    @Autowired
    public IssueController(IssueService issueService, ProjectService projectService, UserService userService, DeveloperRecommendationService developerRecommendationService) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.userService = userService;
        this.developerRecommendationService = developerRecommendationService;
    }

    @GetMapping
    public ResponseEntity<IssueDTO> getIssue(@PathVariable("projectId") int projectId, @PathVariable("issueId") int issueId, HttpSession session) {
        Project currentProject = projectService.findProjectById(projectId);
        User user = (User) session.getAttribute("USER");
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!projectService.hasUser(currentProject, user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Issue> issues = issueService.searchIssues(currentProject, null);
        Issue detailedIssue = issues.stream()
                .filter(issue -> issue.getIssueId() == issueId)
                .findFirst().get();

        return new ResponseEntity<>(new IssueDTO(detailedIssue), HttpStatus.OK);
    }

    @GetMapping("/comments")
    public List<CommentDTO> getComments(@PathVariable("projectId") int projectId, @PathVariable("issueId") int issueId) {
        Issue detailedIssue = issueService.findIssueById(issueId);

        return detailedIssue.getComments().stream().map(CommentDTO::new).toList();
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> addComment(@PathVariable("projectId") int projectId, @PathVariable("issueId") int issueId, @RequestBody Map<String, String> params, HttpSession session) {

        User user = (User) session.getAttribute("USER");
        Project currentProject = projectService.findProjectById(projectId);
        Issue detailedIssue = issueService.findIssueById(issueId);

        String content = params.get("content");
        System.out.println(content);

        issueService.addComment(currentProject, user, detailedIssue, user.getUsername(), content);
        //void addComment(Project project, User commenter, Issue issue, String commentTitle, String commentText);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/assignee")
    public ResponseEntity<Void> assignDeveloper(@PathVariable("projectId") int projectId, @PathVariable("issueId") int issueId, @RequestBody Map<String, String> params) {
        Project currentProject = projectService.findProjectById(projectId);
        Issue detailedIssue = issueService.findIssueById(issueId);

        User assignee = userService.findByUsername(params.get("assignee"));

        issueService.assignIssue(currentProject,null, detailedIssue, assignee);

        //Service 쪽에서 assigner 삭제 필요
        //void assignIssue(Project project, User assigner, Issue issue, User assignee);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/assignee/recommended")
    public List<UserDTO> getDeveloperRecommend(@PathVariable("projectId") int projectId, @PathVariable("issueId") int issueId) {
        Project currentProject = projectService.findProjectById(projectId);
        Issue detailedIssue = issueService.findIssueById(issueId);
        
        return developerRecommendationService.recommendDeveloper(currentProject, detailedIssue).stream().map(UserDTO::new).toList();
    }

    @PutMapping("/status")
    public ResponseEntity<Void> changeState(@PathVariable("projectId")int projectId, @PathVariable("issueId") int issueId, @RequestBody Map<String, String> params, HttpSession session) {

        User user = (User) session.getAttribute("USER");
        Project currentProject = projectService.findProjectById(projectId);
        Issue detailedIssue = issueService.findIssueById(issueId);


        IssueStatus status = IssueStatus.valueOf(params.get("status"));

        issueService.updateIssueStatus(currentProject, user, detailedIssue, status);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

