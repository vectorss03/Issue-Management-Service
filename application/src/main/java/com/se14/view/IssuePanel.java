package com.se14.view;

import com.se14.domain.Issue;
import com.se14.domain.IssuePriority;
import com.se14.domain.IssueStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;

public class IssuePanel extends JPanel {
    private JTable issueTable;
    private DefaultTableModel tableModel;
    private JComboBox<IssueStatus> statusFilter;
    private JComboBox<IssuePriority> priorityFilter;
    private JComboBox<String> assigneeFilter;
    private JComboBox<String> fixerFilter;
    private JButton filterButton;
    private JButton resetButton;

    public IssuePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));

        statusFilter = new JComboBox<>(IssueStatus.values());
        priorityFilter = new JComboBox<>(IssuePriority.values());
        assigneeFilter = new JComboBox<>();
        fixerFilter = new JComboBox<>();

        // Add null (empty) options
        statusFilter.insertItemAt(null, 0);
        priorityFilter.insertItemAt(null, 0);
        assigneeFilter.addItem(null);
        fixerFilter.addItem(null);

        // Set default selected item to null
        statusFilter.setSelectedIndex(0);
        priorityFilter.setSelectedIndex(0);
        assigneeFilter.setSelectedIndex(0);
        fixerFilter.setSelectedIndex(0);

        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("Priority:"));
        filterPanel.add(priorityFilter);
        filterPanel.add(new JLabel("Assignee:"));
        filterPanel.add(assigneeFilter);
        filterPanel.add(new JLabel("Fixer:"));
        filterPanel.add(fixerFilter);

        filterButton = new JButton("Filter");
        resetButton = new JButton("Reset");
        filterPanel.add(filterButton);
        filterPanel.add(resetButton);

        add(filterPanel);

        String[] columnNames = {"Title", "Description", "Status", "Priority", "Assignee", "Fixer"};
        tableModel = new DefaultTableModel(columnNames, 0);
        issueTable = new JTable(tableModel);
        issueTable.setDefaultEditor(Object.class, null);  // Make table non-editable

        issueTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = issueTable.rowAtPoint(evt.getPoint());
                int col = issueTable.columnAtPoint(evt.getPoint());
                if (col == 0) {
                    String issueTitle = (String) issueTable.getValueAt(row, col);
                    // Handle issue title click
                    issueTitleClicked(issueTitle);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(issueTable);
        add(scrollPane);
    }

    public void setIssues(List<Issue> issues) {
        tableModel.setRowCount(0); // Clear existing rows
        assigneeFilter.removeAllItems();
        fixerFilter.removeAllItems();

        // Add null (empty) options
        assigneeFilter.addItem(null);
        fixerFilter.addItem(null);

        for (Issue issue : issues) {
            String assignee = issue.getAssignee() != null ? issue.getAssignee().getUsername() : "";
            String fixer = issue.getFixer() != null ? issue.getFixer().getUsername() : "";
            Object[] row = {issue.getTitle(), issue.getDescription(), issue.getStatus(), issue.getPriority(), assignee, fixer};
            tableModel.addRow(row);

            if (issue.getAssignee() != null && !assigneeExists(assignee)) {
                assigneeFilter.addItem(assignee);
            }

            if (issue.getFixer() != null && !fixerExists(fixer)) {
                fixerFilter.addItem(fixer);
            }
        }
    }

    public void addFilterButtonListener(ActionListener listener) {
        filterButton.addActionListener(listener);
    }

    public void addResetButtonListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }

    private void issueTitleClicked(String issueTitle) {
        // Implement the action to be performed when an issue title is clicked
        System.out.println("Issue title clicked: " + issueTitle);
    }

    public IssueStatus getSelectedStatus() {
        return (IssueStatus) statusFilter.getSelectedItem();
    }

    public IssuePriority getSelectedPriority() {
        return (IssuePriority) priorityFilter.getSelectedItem();
    }

    public String getSelectedAssignee() {
        return (String) assigneeFilter.getSelectedItem();
    }

    public String getSelectedFixer() {
        return (String) fixerFilter.getSelectedItem();
    }

    private boolean assigneeExists(String assignee) {
        for (int i = 0; i < assigneeFilter.getItemCount(); i++) {
            if (assigneeFilter.getItemAt(i) != null && assigneeFilter.getItemAt(i).equals(assignee)) {
                return true;
            }
        }
        return false;
    }

    private boolean fixerExists(String fixer) {
        for (int i = 0; i < fixerFilter.getItemCount(); i++) {
            if (fixerFilter.getItemAt(i) != null && fixerFilter.getItemAt(i).equals(fixer)) {
                return true;
            }
        }
        return false;
    }
}
