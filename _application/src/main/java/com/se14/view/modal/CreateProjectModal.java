package com.se14.view.modal;

import com.se14.controller.modal.CreateProjectController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateProjectModal extends JFrame {
    private final CreateProjectController controller;

    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JButton saveButton;

    public CreateProjectModal(CreateProjectController controller) {
        this.controller = controller;

        setTitle("Create New Project");
        setSize(300, 320);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(8, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        saveButton = new JButton("Save");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.WEST;
        addToGridBag(gbc, new JLabel("Title"), 0, 0, 1, 1);
        addToGridBag(gbc, new JLabel("Description"), 0, 2, 1, 1);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        addToGridBag(gbc, titleField, 0, 1, 2, 1);
        addToGridBag(gbc, scrollPane, 0, 3, 2, 1);
        addToGridBag(gbc, saveButton, 0, 5, 2, 1);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String description = descriptionArea.getText();
                if (!title.isEmpty() && !description.isEmpty()) {
                    controller.createProject(title, description);
                } else {
                    JOptionPane.showMessageDialog(null, "You must type project's title and description", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void clearField() {
        titleField.setText("");
        descriptionArea.setText("");
    }

    private void addToGridBag(GridBagConstraints gbc, Component component, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        add(component, gbc);
    }
}
