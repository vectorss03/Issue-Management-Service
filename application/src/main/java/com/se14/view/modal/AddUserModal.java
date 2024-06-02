package com.se14.view.modal;

import com.formdev.flatlaf.FlatLightLaf;
import com.se14.controller.ViewController;
import com.se14.controller.modal.AddUserController;
import com.se14.domain.UserRole;
import com.se14.dto.UserDTO;
import com.se14.view.panel.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddUserModal extends JFrame {
    private final AddUserController controller;

    private final JCheckBox adminCheckbox;
    private final JCheckBox projectLeadCheckbox;
    private final JCheckBox developerCheckbox;
    private final JCheckBox testerCheckbox;

    private final JTextField usernameField;
    private final JButton searchButton;

    private final UserPanel userPanel;
    private final JButton addButton;


    public AddUserModal(AddUserController controller) {
        this.controller = controller;

        setTitle("Add User");
        setSize(300, 380);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        adminCheckbox = new JCheckBox("Admin");
        projectLeadCheckbox = new JCheckBox("Project Lead");
        developerCheckbox = new JCheckBox("Developer");
        testerCheckbox = new JCheckBox("Tester");

        usernameField = new JTextField(20);
        searchButton = new JButton("Search");
        addButton = new JButton("Add");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addToGridBag(gbc, adminCheckbox, 0, 0, 2, 1);
        addToGridBag(gbc, projectLeadCheckbox, 0, 1, 2, 1);
        addToGridBag(gbc, developerCheckbox, 0, 2, 2, 1);
        addToGridBag(gbc, testerCheckbox, 0, 3, 2, 1);

        gbc.weightx = 1;
        addToGridBag(gbc, usernameField, 0, 4, 1, 1);
        gbc.weightx = 0;
        addToGridBag(gbc, searchButton, 1, 4, 1, 1);

        userPanel = new UserPanel();
        JScrollPane userScrollPane = new JScrollPane(userPanel);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        addToGridBag(gbc, userScrollPane, 0, 5, 2, 1);

        gbc.weighty = 0;
        addToGridBag(gbc, addButton, 0, 6, 2, 1);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userPanel.getSelectedUsername() != null && !getSelectedUserRoles().isEmpty()) {
                    controller.addUser(userPanel.getSelectedUsername(), getSelectedUserRoles());
                } else {
                    showErrorMessage("You must select roles and user");
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

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Add User Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addToGridBag(GridBagConstraints gbc, Component component, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        add(component, gbc);
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new AddUserModal(new AddUserController(new ViewController())).setVisible(true);
    }

    public void setUserPanel(List<UserDTO> users) {
        userPanel.setUsers(users);
    }

    private List<UserRole> getSelectedUserRoles() {
        List<UserRole> selectedUserRoles = new ArrayList<>();
        if (adminCheckbox.isSelected()) {
            selectedUserRoles.add(UserRole.ADMIN);
        }
        if (projectLeadCheckbox.isSelected()) {
            selectedUserRoles.add(UserRole.PROJECT_LEAD);
        }
        if (developerCheckbox.isSelected()) {
            selectedUserRoles.add(UserRole.DEVELOPER);
        }
        if (testerCheckbox.isSelected()) {
            selectedUserRoles.add(UserRole.TESTER);
        }
        return selectedUserRoles;
    }

    public void clearField() {
        adminCheckbox.setSelected(false);
        projectLeadCheckbox.setSelected(false);
        developerCheckbox.setSelected(false);
        testerCheckbox.setSelected(false);
        usernameField.setText("");
    }
}
