package com.se14.view.panel;

import com.se14.controller.panel.ProjectController;
import com.se14.dto.ProjectDTO;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProjectPanel extends JPanel {
    private final ProjectController controller;

    private JButton createProjectButton;
    public ProjectPanel(ProjectController controller) {
        this.controller = controller;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createProjectButton = new JButton("Create Project");
        add(createProjectButton);

        createProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.isAuthenticated()) {
                    controller.showCreateProjectModal();
                } else {
                    JOptionPane.showMessageDialog(null, "You must logged in to create project", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void setProjects(List<ProjectDTO> projects) {
        removeAll();
        add(createProjectButton);
        for (ProjectDTO project : projects) {
            JButton projectButton = new JButton("<html><b>" + project.getTitle() + "</b><br/>" + project.getDescription() + "</html>");
            projectButton.setActionCommand(String.valueOf(project.getProjectId()));
            projectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Integer projectId = Integer.valueOf(e.getActionCommand());
                    System.out.println("Selected Project ID: " + projectId);
                    controller.selectProject(projectId);
//                    Project currentProject = projectService.findProjectById(Long.parseLong(projectId));
//                    sessionService.setCurrentProject(currentProject);
//                    displayProjectIssues(currentProject);
//                    // Handle project click to show ProjectMainView (implementation needed)
//                    //System.out.println("Project ID clicked: " + projectId);
                }
            });
            add(projectButton);
        }
        revalidate();
        repaint();
    }

    public void showServerErrorMessage() {
        JOptionPane.showMessageDialog(this, "Server connection error\nTry Again later", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
