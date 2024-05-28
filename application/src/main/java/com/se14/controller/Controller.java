package com.se14.controller;

import com.se14.domain.*;
import com.se14.repository.CommentRepository;
import com.se14.repository.IssueRepository;
import com.se14.repository.ProjectRepository;
import com.se14.repository.UserRepository;
import com.se14.repository.fake.CommentRepositoryFake;
import com.se14.repository.fake.IssueRepositoryFake;
import com.se14.service.*;
import com.se14.service.implement1.IssueServiceImplement;
import com.se14.service.implement1.ProjectServiceImplement;
import com.se14.service.implement1.SessionServiceImplement;
import com.se14.service.implement1.UserServiceImplement;
import com.se14.repository.fake.UserRepositoryFake;
import com.se14.repository.fake.ProjectRepositoryFake;
import com.se14.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static Controller instance;
    private UserService userService;
    private ProjectService projectService;
    private SessionService sessionService;
    private IssueService issueService;
    private MainView mainView;
    private LoginView loginView;
    private SignInView signInView;

    private Controller() {
        // Initialize the services and repositories
        UserRepository userRepository = new UserRepositoryFake();
        userService = new UserServiceImplement(userRepository);
        ProjectRepository projectRepository = new ProjectRepositoryFake(userRepository);
        CommentRepository commentRepository =new CommentRepositoryFake();
        IssueRepository issueRepository = new IssueRepositoryFake(projectRepository,commentRepository);
        projectService = new ProjectServiceImplement(projectRepository, userRepository);
        sessionService = new SessionServiceImplement();
        issueService = new IssueServiceImplement(issueRepository,projectRepository,commentRepository);

        // Initialize views
        mainView = new MainView();
        loginView = new LoginView();
        signInView = new SignInView();

        // Add action listeners to MainView
        mainView.addLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginView();
            }
        });

        mainView.addSignInButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSignInView();
            }
        });

        mainView.addLogoutButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        mainView.addHomeButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sessionService.setCurrentProject(null);
                sessionService.setCurrentIssue(null);
                mainView.showView("Projects");
            }
        });
        mainView.addCreateProjectButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewProject();
            }
        });
        mainView.getIssuePanel().addFilterButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterIssues();
            }
        });
        mainView.getIssuePanel().addResetButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetIssues();
            }
        });
        mainView.getIssuePanel().setIssueDetailListener(new IssuePanel.IssueDetailListener() {
            @Override
            public void onIssueTitleClicked(String issueTitle) {
                showIssueDetail(issueTitle);
            }
        });

        mainView.setVisible(true);
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void showMainView() {
        User currentUser = sessionService.getCurrentSession().getCurrentUser();
        if (currentUser != null) {
            mainView.setLoggedIn(true,currentUser.getUsername());
        } else {
            mainView.setLoggedIn(false,"");
        }
        displayUserProjects(currentUser);
        mainView.setVisible(true);
    }

    public void showLoginView() {
        loginView.setVisible(true);
    }

    public void showSignInView() {
        signInView.setVisible(true);
    }

    public void attemptLogin(String username, String password) {
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            sessionService.setCurrentUser(user);
            loginView.dispose();
            showMainView();
        } else {
            loginView.showErrorMessage("Invalid username or password.");
        }
    }

    public void attemptSignIn(String username, String password, String email) {
        try {
            userService.addNewUser(username, password, email);
            signInView.showSuccessMessage();
        } catch (Exception e) {
            signInView.showErrorMessage(e.getMessage());
        }
    }

    public void logout() {
        sessionService.setCurrentUser(null);
        mainView.showView("Projects");
        showMainView();
    }

    private void displayUserProjects(User user) {
        List<Project> projects = projectService.listProject();
        List<Project> accessibleProjects = new ArrayList<>();
        for (Project project : projects) {
            if (projectService.hasUser(project, user)) {
                accessibleProjects.add(project);
            }
        }
        mainView.setProjects(accessibleProjects, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectId = e.getActionCommand();
                Project currentProject = projectService.findProjectById(Long.parseLong(projectId));
                sessionService.setCurrentProject(currentProject);
                displayProjectIssues(currentProject);
                // Handle project click to show ProjectMainView (implementation needed)
                //System.out.println("Project ID clicked: " + projectId);
            }
        });
    }

    private void filterIssues() {
        Project currentProject = sessionService.getCurrentSession().getCurrentProject();
        if (currentProject == null) {
            return;
        }

        IssueStatus selectedStatus = mainView.getIssuePanel().getSelectedStatus();
        IssuePriority selectedPriority = mainView.getIssuePanel().getSelectedPriority();
        String selectedAssignee = mainView.getIssuePanel().getSelectedAssignee();
        String selectedFixer = mainView.getIssuePanel().getSelectedFixer();

        SearchCriteria criteria = new SearchCriteria();
        criteria.setStatus(selectedStatus);
        criteria.setPriority(selectedPriority);
        criteria.setAssignee(null);
        criteria.setFixer(null);
        if (selectedAssignee != null) {
            User assignee = userService.findByUsername(selectedAssignee);
            criteria.setAssignee(assignee);
        }
        if (selectedFixer != null) {
            User fixer = userService.findByUsername(selectedFixer);
            criteria.setFixer(fixer);
        }

        List<Issue> filteredIssues = issueService.searchIssues(currentProject, criteria);
        mainView.getIssuePanel().setIssues(filteredIssues);
    }

    private void resetIssues() {
        Project currentProject = sessionService.getCurrentSession().getCurrentProject();
        if (currentProject != null) {
            displayProjectIssues(currentProject);
        }
    }
    private void showIssueDetail(String issueTitle) {
        Project currentProject = sessionService.getCurrentSession().getCurrentProject();
        if (currentProject == null) {
            return;
        }

        List<Issue> issues = issueService.searchIssues(currentProject, null);
        for (Issue issue : issues) {
            if (issue.getTitle().equals(issueTitle)) {
                sessionService.setCurrentIssue(issue);
                mainView.getIssueDetailPanel().setIssue(issue);
                mainView.showView("IssueDetail");
                break;
            }
        }
    }
    private void createNewProject() {
        String projectName = JOptionPane.showInputDialog(mainView, "Enter project name:");
        String projectDescription = JOptionPane.showInputDialog(mainView, "Enter project description:");

        if (projectName != null && projectDescription != null && !projectName.trim().isEmpty() && !projectDescription.trim().isEmpty()) {
            User currentUser = sessionService.getCurrentSession().getCurrentUser();
            if (currentUser != null) {
                Project newProject = projectService.createProject(currentUser, projectName, projectDescription);
                if (newProject != null) {
                    showMainView();  // Refresh the main view to show the new project
                } else {
                    JOptionPane.showMessageDialog(mainView, "Failed to create project.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainView, "You must be logged in to create a project.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void displayProjectIssues(Project project) {
        List<Issue> issues = issueService.searchIssues(sessionService.getCurrentSession().getCurrentProject(),null);
        mainView.setIssues(issues);
    }

    public static void main(String[] args) {
        Controller.getInstance().showMainView();
    }
}
