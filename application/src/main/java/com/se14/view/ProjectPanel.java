package com.se14.view;

import com.se14.domain.Project;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ProjectPanel extends JPanel {
    private JButton createProjectButton;
    public ProjectPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createProjectButton = new JButton("Create Project");
        add(createProjectButton);
    }

    public void setProjects(List<Project> projects, ActionListener projectClickListener) {
        removeAll();
        add(createProjectButton);
        for (Project project : projects) {
            JButton projectButton = new JButton("<html><b>" + project.getProjectTitle() + "</b><br/>" + project.getProjectDescription() + "</html>");
            projectButton.setActionCommand(String.valueOf(project.getProjectId()));
            projectButton.addActionListener(projectClickListener);
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
