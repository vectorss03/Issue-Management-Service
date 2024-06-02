package com.se14.view.modal;

import com.se14.controller.modal.ReportIssueController;
import com.se14.domain.IssuePriority;
import com.se14.domain.IssueStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportIssueModal extends JFrame {
    private ReportIssueController controller;

    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JComboBox<IssueStatus> statusComboBox;
    private final JComboBox<IssuePriority> priorityComboBox;
    private final JButton saveButton;

    public ReportIssueModal(ReportIssueController controller) {
        this.controller = controller;

        setTitle("Report New Issue");
        setSize(300, 380);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(8, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        statusComboBox = new JComboBox<>(IssueStatus.values());
        statusComboBox.setEnabled(false);
        priorityComboBox = new JComboBox<>(IssuePriority.values());
        priorityComboBox.setSelectedItem(IssuePriority.MAJOR);

        saveButton = new JButton("Save");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.WEST;
        addToGridBag(gbc, new JLabel("Title"), 0, 0, 1, 1);
        addToGridBag(gbc, new JLabel("Description"), 0, 2, 1, 1);
        addToGridBag(gbc, new JLabel("Status"), 0, 4, 1, 1);
        addToGridBag(gbc, new JLabel("Priority"), 1, 4, 1, 1);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        addToGridBag(gbc, titleField, 0, 1, 2, 1);
        addToGridBag(gbc, scrollPane, 0, 3, 2, 1);
        addToGridBag(gbc, statusComboBox, 0, 5, 1, 1);
        addToGridBag(gbc, priorityComboBox, 1, 5, 1, 1);
        addToGridBag(gbc, saveButton, 0, 6, 2, 1);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String description = descriptionArea.getText();
                IssuePriority priority = (IssuePriority) priorityComboBox.getSelectedItem();
                if (!title.isEmpty() && !description.isEmpty()) {
                    controller.reportIssue(title, description, priority);
                } else {
                    JOptionPane.showMessageDialog(null, "You must type issue's title and description", "Error", JOptionPane.ERROR_MESSAGE);
                };

            }
        });
    }

    public void clearField() {
        titleField.setText("");
        descriptionArea.setText("");
        priorityComboBox.setSelectedItem(IssuePriority.MAJOR);
    }

    private void addToGridBag(GridBagConstraints gbc, Component component, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        add(component, gbc);
    }
}
