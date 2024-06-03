package com.se14.controller;

import com.se14.domain.*;
import com.se14.dto.FilterDTO;
import com.se14.dto.IssueDTO;
import com.se14.dto.ProjectDTO;
import com.se14.dto.UserDTO;
import com.se14.service.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import jakarta.servlet.http.HttpSession;

import com.se14.service.ProjectService;
import com.se14.service.IssueService;
import com.se14.service.UserService;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final IssueService issueService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, IssueService issueService, UserService userService) {
        this.projectService = projectService;
        this.issueService = issueService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> projectList(HttpSession session) {
        User user = (User) session.getAttribute("USER");
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<Project> projects = projectService.findProjectByUser(user);

        return new ResponseEntity<>(projects.stream().map(ProjectDTO::new).toList(), HttpStatus.OK);

    }
    //ProjectService쪽 수정되었을 때 추가 작업 필요 => ProjectService에 findProjectByUser()추가됨, 그에 따라 수정 했음

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody Map<String, String> params, HttpSession session) {

        User creator = (User) session.getAttribute("USER");
        String title = params.get("title");
        String description = params.get("description");

        Project newProject = projectService.createProject(creator, title, description);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/issues")
    public ResponseEntity<List<IssueDTO>> issueList(@PathVariable("projectId") int projectId, @ModelAttribute FilterDTO filter, HttpSession session) {

        Project project = projectService.findProjectById(projectId);
        User user = (User) session.getAttribute("USER");
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!projectService.hasUser(project, user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setTitle(filter.getKeyword());
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) searchCriteria.setStatus(IssueStatus.valueOf(filter.getStatus()));
        if (filter.getPriority() != null && !filter.getPriority().isEmpty()) searchCriteria.setPriority(IssuePriority.valueOf(filter.getPriority()));
        searchCriteria.setAssignee(filter.getAssignee());
        searchCriteria.setFixer(filter.getFixer());
        searchCriteria.setReporter(filter.getReporter());

        List<Issue> issues = issueService.searchIssues(project, searchCriteria);

        return new ResponseEntity<>(issues.stream().map(IssueDTO::new).toList(), HttpStatus.OK);
    }

    @PostMapping("/{projectId}/issues")
    public ResponseEntity<Void> createIssue(@PathVariable("projectId") int projectId, @RequestBody Map<String, String> params, HttpSession session) {

        Project currentProject = projectService.findProjectById(projectId);
        User user = (User) session.getAttribute("USER");

        String title = params.get("title");
        String description = params.get("description");
        IssuePriority priority = IssuePriority.valueOf(params.get("priority").toUpperCase());

        /*Issue newIssue = */issueService.reportIssue(currentProject, user, title, description, priority);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/roles")
    public List<UserRole> roleList(@PathVariable("projectId") int projectId, HttpSession session) throws Exception {
        Project project = projectService.findProjectById(projectId);
        User user = (User) session.getAttribute("USER");

        return projectService.getUserRoles(project, user);
        // return project.getMembers().get(user);
    }


    @GetMapping("/{projectId}/users")
    public List<UserDTO> userList(@PathVariable("projectId") int projectId) {
        Project currentProject = projectService.findProjectById(projectId);

        return projectService.findUserByProject(currentProject).stream().map(UserDTO::new).toList();
        //프론트 엔드 쪽 수정 필요함
        //프로젝트에 참가한 유저들 반환
    }

    @GetMapping("/{projectId}/developers")
    public List<UserDTO> developerList(@PathVariable("projectId") int projectId) {
        Project currentProject = projectService.findProjectById(projectId);

        Set<User> developers = new HashSet<>(projectService.listUser(currentProject, UserRole.DEVELOPER));
        developers.addAll(projectService.listUser(currentProject, UserRole.ADMIN));
        
        return developers.stream().map(UserDTO::new).toList();
    }

    @GetMapping("/{projectId}/users/join")
    public List<UserDTO> userJoinList(@PathVariable("projectId") int projectId) {
        Project currentProject = projectService.findProjectById(projectId);

        List<User> allUsers = userService.listAllUser();
        List<User> notJoinedUsers = new ArrayList<>();

        for(User user : allUsers){
            if(!projectService.hasUser(currentProject, user))
                notJoinedUsers.add(user);
        }

        return notJoinedUsers.stream().map(UserDTO::new).toList();
        //프로젝트에 참가해 있지 않은 유저들 반환
    }
//
    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> addUser(@PathVariable("projectId") int projectId, @RequestBody Map<String, Object> params) {
        Project currentProject = projectService.findProjectById(projectId);

        User newUser = userService.findByUsername(params.get("username").toString());
        List<UserRole> userRoles = ((List<String>) params.get("userRoles")).stream().map(UserRole::valueOf).toList();
        projectService.addMemberToProject(currentProject, newUser, userRoles);

        //void addMemberToProject(Project project,User user, UserRole role);
//        projectService.addMemberToProject(currentProject, user, role);
        //Vue쪽에서 role 처리 해야 함

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{projectId}/statistics")
    public HashMap<String, Object> getStatistics(@PathVariable("projectId") int projectId) {
        Project project = projectService.findProjectById(projectId);
        return projectService.getStatistic(project);
    }
}