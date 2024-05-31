package com.se14.view.panel;

import com.se14.domain.Project;
import com.se14.dto.ProjectDTO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProjectPanel extends JPanel {
    private JButton createProjectButton;
    public ProjectPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createProjectButton = new JButton("Create Project");
        add(createProjectButton);
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
                    String projectId = e.getActionCommand();
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
    public void addCreateProjectButtonListener(ActionListener listener) {
        createProjectButton.addActionListener(listener);
    }
    public void setCreateProjectButtonVisible(boolean visible) {
        createProjectButton.setVisible(visible);
    }
}
