package com.se14.view;

import com.se14.controller.MainController;
import com.se14.domain.User;
import com.se14.dto.ProjectDTO;
import com.se14.view.panel.IssueDetailPanel;
import com.se14.view.panel.IssuePanel;
import com.se14.view.panel.ProjectPanel;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainView extends JFrame {
    @Setter
    private MainController controller;

    private JButton loginButton;
    private JButton signInButton;
    private JButton logoutButton;
    private JLabel userLabel;
    private JPanel cardPanel;  // 패널을 담을 CardLayout 패널
    private CardLayout cardLayout;  // CardLayout 관리자
    private ProjectPanel projectPanel;  // 프로젝트 목록을 보여주는 패널
    private IssueDetailPanel issueDetailPanel;  // 이슈 상세정보를 보여주는 패널

    private JButton homeButton;
    private JButton refreshButton;
    private IssuePanel issuePanel;

    public MainView() {
        setTitle("Project Main Page");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        loginButton = new JButton("Log In");
        signInButton = new JButton("Sign In");
        logoutButton = new JButton("Log Out");
        userLabel = new JLabel("Welcome, Guest");
        homeButton = new JButton("Home");
        refreshButton = new JButton("Refresh");

        buttonPanel.add(homeButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(signInButton);

        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        projectPanel = new ProjectPanel();
        issuePanel = new IssuePanel();
        issueDetailPanel = new IssueDetailPanel();
//
        cardPanel.add(projectPanel, "Projects");
        cardPanel.add(issuePanel, "Issues");
        cardPanel.add(issueDetailPanel, "IssueDetail");

        add(topPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        refreshButton.setVisible(false);
        logoutButton.setVisible(false);
        userLabel.setVisible(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showLoginView();
            }
        });

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showSignInView();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.logout();
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.goToHome();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.refresh();
            }
        });
    }

    public void updateUser(User user) {
        if (user != null) {
            setLoggedIn(true, user.getUsername());
        } else {
            setLoggedIn(false, "");
        }
        controller.displayUserProjects();
//        displayUserProjects();
    }

    public void setLoggedIn(boolean loggedIn, String username) {
        loginButton.setVisible(!loggedIn);
        signInButton.setVisible(!loggedIn);
        logoutButton.setVisible(loggedIn);
        userLabel.setVisible(loggedIn);
        refreshButton.setVisible(loggedIn);

//        projectPanel.setCreateProjectButtonVisible(loggedIn);

        if (loggedIn) {
            userLabel.setText("Welcome, " + username);
        } else {
            userLabel.setText("Welcome, Guest");
        }
    }

    public void setProjects(List<ProjectDTO> projects) {
        projectPanel.setProjects(projects);
    }

    public void showPanel(String panel) {
        cardLayout.show(cardPanel, panel);
    }


}
