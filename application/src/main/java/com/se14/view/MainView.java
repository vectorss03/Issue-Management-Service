package com.se14.view;

import com.se14.domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JButton loginButton;
    private JButton signInButton;
    private JButton logoutButton;
    private JPanel projectsPanel;

    public MainView() {
        setTitle("Project Main Page");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Log In");
        signInButton = new JButton("Sign In");
        logoutButton = new JButton("Log Out");

        buttonPanel.add(loginButton);
        buttonPanel.add(signInButton);
        buttonPanel.add(logoutButton);

        projectsPanel = new JPanel();
        projectsPanel.setLayout(new BoxLayout(projectsPanel, BoxLayout.Y_AXIS));

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(projectsPanel), BorderLayout.CENTER);

        logoutButton.setVisible(false);
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

    public void setLoggedIn(boolean loggedIn) {
        loginButton.setVisible(!loggedIn);
        signInButton.setVisible(!loggedIn);
        logoutButton.setVisible(loggedIn);
    }

    public void setProjects(java.util.List<Project> projects, ActionListener projectClickListener) {
        projectsPanel.removeAll();
        for (Project project : projects) {
            JButton projectButton = new JButton("<html><b>" + project.getProjectTitle() + "</b><br/>" + project.getProjectDescription() + "</html>");
            projectButton.setActionCommand(String.valueOf(project.getProjectId()));
            projectButton.addActionListener(projectClickListener);
            projectsPanel.add(projectButton);
        }
        projectsPanel.revalidate();
        projectsPanel.repaint();
    }
}
