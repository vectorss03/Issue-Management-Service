package com.se14.view;

import com.se14.domain.Issue;
import com.se14.domain.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainView extends JFrame {
    private JButton loginButton;
    private JButton signInButton;
    private JButton logoutButton;
    private JLabel userLabel;
    private JPanel cardPanel;  // 패널을 담을 CardLayout 패널
    private CardLayout cardLayout;  // CardLayout 관리자
    private ProjectPanel projectPanel;  // 프로젝트 목록을 보여주는 패널
    private IssueDetailPanel issueDetailPanel;  // 이슈 상세정보를 보여주는 패널

    private JButton homeButton;
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

        buttonPanel.add(homeButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(signInButton);

        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        projectPanel = new ProjectPanel();
        issuePanel = new IssuePanel();
        issueDetailPanel = new IssueDetailPanel();


        cardPanel.add(projectPanel, "Projects");
        cardPanel.add(issuePanel, "Issues");
        cardPanel.add(issueDetailPanel, "IssueDetail");



        add(topPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        logoutButton.setVisible(false);
        userLabel.setVisible(false);
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addSignInButtonListener(ActionListener listener) {
        signInButton.addActionListener(listener);
    }

    public void addLogoutButtonListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    public void addHomeButtonListener(ActionListener listener) {
        homeButton.addActionListener(listener);
    }

    public void setLoggedIn(boolean loggedIn, String username) {
        loginButton.setVisible(!loggedIn);
        signInButton.setVisible(!loggedIn);
        logoutButton.setVisible(loggedIn);
        userLabel.setVisible(loggedIn);

        projectPanel.setCreateProjectButtonVisible(loggedIn);

        if (loggedIn) {
            userLabel.setText("Welcome, " + username);
        } else {
            userLabel.setText("Welcome, Guest");
        }
    }

    public void setProjects(List<Project> projects, ActionListener projectClickListener) {
        projectPanel.setProjects(projects, projectClickListener);
    }
    public void setIssues(List<Issue> issues) {
        issuePanel.setIssues(issues);
        showView("Issues");
    }
    public void addCreateProjectButtonListener(ActionListener listener) {
        projectPanel.addCreateProjectButtonListener(listener);
    }
    public IssuePanel getIssuePanel() {
        return issuePanel;
    }
    public IssueDetailPanel getIssueDetailPanel() {
        return issueDetailPanel;
    }
    // 새로운 패널을 CardLayout에 추가하기 위한 메서드
    public void addView(JPanel panel, String name) {
        cardPanel.add(panel, name);
    }

    // 카드 레이아웃에서 특정 뷰를 보여주기 위한 메서드
    public void showView(String name) {
        cardLayout.show(cardPanel, name);
    }
}
