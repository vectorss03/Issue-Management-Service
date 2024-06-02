package com.se14.view.modal;

import com.se14.controller.modal.AssignDeveloperController;
import com.se14.domain.User;
import com.se14.dto.UserDTO;
import com.se14.view.panel.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AssignDeveloperModal extends JFrame {
    private final AssignDeveloperController controller;

    private final UserPanel recommendedUserPanel;

    private final JTextField usernameField;
    private final JButton searchButton;

    private final UserPanel userPanel;

    private final JButton assignButton;
    ButtonGroup group;

    public AssignDeveloperModal(AssignDeveloperController controller) {
        this.controller = controller;

        setTitle("Create New Project");
        setSize(300, 380);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        usernameField = new JTextField(20);
        searchButton = new JButton("Search");
        assignButton = new JButton("Assign");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;

        recommendedUserPanel = new UserPanel();
        recommendedUserPanel.setBorder(BorderFactory.createTitledBorder("Recommended"));

        gbc.weighty = 0.3;
        addToGridBag(gbc, recommendedUserPanel, 0, 0, 2, 1);
        gbc.weighty = 0;
        gbc.weightx = 1;
        addToGridBag(gbc, usernameField, 0, 1, 1, 1);
        gbc.weightx = 0;
        addToGridBag(gbc, searchButton, 1, 1, 1, 1);

        userPanel = new UserPanel();
        JScrollPane userScrollPane = new JScrollPane(userPanel);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.7;
        addToGridBag(gbc, userScrollPane, 0, 2, 2, 1);

        gbc.weighty = 0;
        addToGridBag(gbc, assignButton, 0, 3, 2, 1);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (group.getSelection() != null) {
                    controller.assignDeveloper(group.getSelection().getActionCommand());
                } else {
                    JOptionPane.showMessageDialog(null, "You must select developer to assign", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.searchUsers(usernameField.getText());
            }
        });
    }


    private void addToGridBag(GridBagConstraints gbc, Component component, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        add(component, gbc);
    }

    public void setRecommendedUserPanel(List<UserDTO> recommendedUsers) {
        recommendedUserPanel.setUsers(recommendedUsers);
        setButtonGroup();
    }

    public void setUserPanel(List<UserDTO> users) {
        userPanel.setUsers(users);
        setButtonGroup();
    }

    public void setButtonGroup() {
        group = new ButtonGroup();

        userPanel.getRadioButtons().forEach(jRadioButton -> {
            jRadioButton.setActionCommand(jRadioButton.getText());
            group.add(jRadioButton);
        });
        recommendedUserPanel.getRadioButtons().forEach(jRadioButton -> {
            jRadioButton.setActionCommand(jRadioButton.getText());
            group.add(jRadioButton);
        });
    }

    public void clearField() {
        usernameField.setText("");
        group.clearSelection();
    }
}
