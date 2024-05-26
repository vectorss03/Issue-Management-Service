package com.se14.controller;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.service.ProjectService;
import com.se14.service.UserService;
import com.se14.service.SessionService;
import com.se14.service.implement1.ProjectServiceImplement;
import com.se14.service.implement1.SessionServiceImplement;
import com.se14.service.implement1.UserServiceImplement;
import com.se14.repository.fake.UserRepositoryFake;
import com.se14.repository.fake.ProjectRepositoryFake;
import com.se14.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Controller {
    private static Controller instance;
    private UserService userService;
    private ProjectService projectService;
    private SessionService sessionService;
    private MainView mainView;
    private LoginView loginView;
    private SignInView signInView;

    private Controller() {
        // Initialize the services and repositories
        UserRepositoryFake userRepository = new UserRepositoryFake();
        userService = new UserServiceImplement(userRepository);
        ProjectRepositoryFake projectRepository = new ProjectRepositoryFake(userRepository);
        projectService = new ProjectServiceImplement(projectRepository, userRepository);
        sessionService = new SessionServiceImplement();

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
            mainView.setLoggedIn(true);
            displayUserProjects(currentUser);
        } else {
            mainView.setLoggedIn(false);
        }
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
        showMainView();
    }

    private void displayUserProjects(User user) {
        List<Project> projects = projectService.listProject();
        mainView.setProjects(projects, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectId = e.getActionCommand();
                // Handle project click to show ProjectMainView (implementation needed)
                System.out.println("Project ID clicked: " + projectId);
            }
        });
    }

    public static void main(String[] args) {
        Controller.getInstance().showMainView();
    }
}
