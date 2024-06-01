package com.se14.view.panel;

import com.se14.controller.panel.IssueController;
import com.se14.domain.IssuePriority;
import com.se14.domain.IssueStatus;
import com.se14.dto.IssueDTO;
import com.se14.dto.UserDTO;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class IssuePanel extends JPanel {
    private final IssueController controller;

    private JTable issueTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JComboBox<IssueStatus> statusFilter;
    private JComboBox<IssuePriority> priorityFilter;
    private JComboBox<String> assigneeFilter;
    private JComboBox<String> fixerFilter;
    private JComboBox<String> reporterFilter;
    private JButton filterButton;
    private JButton resetButton;
    private JButton reportIssueButton;
    private IssueDetailListener issueDetailListener;

    public interface IssueDetailListener {
        void onIssueTitleClicked(int issueId);
    }

    public void setIssueDetailListener(IssueDetailListener listener) {
        this.issueDetailListener = listener;
    }

    public IssuePanel(IssueController controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        titleField = new JTextField();
        resetButton = new JButton("Reset");
        filterButton = new JButton("Filter");
        reportIssueButton = new JButton("Report Issue");
        statusFilter = new JComboBox<>(IssueStatus.values());
        priorityFilter = new JComboBox<>(IssuePriority.values());
        assigneeFilter = new JComboBox<>();
        fixerFilter = new JComboBox<>();
        reporterFilter = new JComboBox<>();

        statusFilter.insertItemAt(null, 0);
        priorityFilter.insertItemAt(null, 0);

        statusFilter.setSelectedIndex(0);
        priorityFilter.setSelectedIndex(0);

        gbc.weightx = 0;
        addToGridBag(gbc, titleField, 0, 0, 4, 1);
        addToGridBag(gbc, filterButton, 4, 0, 1, 1);
        addToGridBag(gbc, resetButton, 5, 0, 1, 1);
        addToGridBag(gbc, reportIssueButton, 11, 0, 1, 1);
        addToGridBag(gbc, new JLabel("Status"), 0, 1, 1, 1);
        addToGridBag(gbc, new JLabel("Priority"), 2, 1, 1, 1);
        addToGridBag(gbc, new JLabel("Assignee"), 4, 1, 1, 1);
        addToGridBag(gbc, new JLabel("Fixer"), 6, 1, 1, 1);
        addToGridBag(gbc, new JLabel("Reporter"), 8, 1, 1, 1);
        addToGridBag(gbc, statusFilter, 1, 1, 1, 1);
        addToGridBag(gbc, priorityFilter, 3, 1, 1, 1);
        addToGridBag(gbc, assigneeFilter, 5, 1, 1, 1);
        addToGridBag(gbc, fixerFilter, 7, 1, 1, 1);
        addToGridBag(gbc, reporterFilter, 9, 1, 1, 1);

        gbc.weightx = 0.7;
        addToGridBag(gbc, new JLabel(), 10, 1, 1, 1);

        gbc.weightx = 0.3;
        addToGridBag(gbc, new JLabel(), 11, 1, 1, 1);

        String[] columnNames = {"Title", "Description", "Status", "Priority", "Assignee", "Fixer", "Reporter", "Reported Date", "ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        issueTable = new JTable(tableModel);
        issueTable.setDefaultEditor(Object.class, null);  // Make table non-editable

        JScrollPane scrollPane = new JScrollPane(issueTable);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        addToGridBag(gbc, scrollPane, 0, 2, 12, 1);


        setIssueDetailListener(new IssueDetailListener() {
            @Override
            public void onIssueTitleClicked(int issueId) {
                controller.selectIssue(issueId);
            }
        });

        issueTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = issueTable.rowAtPoint(evt.getPoint());
                int col = issueTable.columnAtPoint(evt.getPoint());
                if (col == 0 && issueDetailListener != null) {
                    int issueId = (int) issueTable.getValueAt(row, 8);
                    issueDetailListener.onIssueTitleClicked(issueId);
                    System.out.println("Selected Issue ID: " + issueId);
                }
            }
        });

        reportIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showReportIssueModal();
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               controller.searchIssue(titleField.getText(),
                        (IssueStatus) statusFilter.getSelectedItem(), (IssuePriority) priorityFilter.getSelectedItem(),
                        (String) assigneeFilter.getSelectedItem(), (String) fixerFilter.getSelectedItem(), (String) reporterFilter.getSelectedItem());
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilter();

                controller.searchIssue(titleField.getText(),
                        (IssueStatus) statusFilter.getSelectedItem(), (IssuePriority) priorityFilter.getSelectedItem(),
                        (String) assigneeFilter.getSelectedItem(), (String) fixerFilter.getSelectedItem(), (String) reporterFilter.getSelectedItem());
            }
        });
    }

    private void clearFilter() {
        titleField.setText("");
        statusFilter.setSelectedIndex(0);
        priorityFilter.setSelectedIndex(0);
        assigneeFilter.setSelectedIndex(0);
        fixerFilter.setSelectedIndex(0);
        reporterFilter.setSelectedIndex(0);
    }

    public void setIssues(List<IssueDTO> issues) {
        tableModel.setRowCount(0); // Clear existing rows

        for (IssueDTO issue : issues) {
            Object[] row = {issue.getTitle(), issue.getDescription(), issue.getStatus(), issue.getPriority(), issue.getAssignee(), issue.getFixer(), issue.getReporter(), issue.getReportedDate(), issue.getIssueId()};
            tableModel.addRow(row);
        }
    }

    public void setFilterUsers(List<UserDTO> users, String currentUser) {
        List<JComboBox<String>> userFilters = new ArrayList<>();
        userFilters.add(assigneeFilter);
        userFilters.add(fixerFilter);
        userFilters.add(reporterFilter);

        userFilters.forEach(JComboBox::removeAllItems);
        userFilters.forEach(filter -> filter.addItem(""));
        userFilters.forEach(filter -> filter.addItem(currentUser));
        userFilters.forEach(filter -> filter.addItem("Unassigned"));

        for (UserDTO user : users) {
            if (user.getUsername().equals(currentUser)) continue;
            userFilters.forEach(filter -> filter.addItem(user.getUsername()));
        }

        clearFilter();
    }

    public void setButtons(boolean reportIssueButtonEnabled) {
        reportIssueButton.setEnabled(reportIssueButtonEnabled);
    }

    private void addToGridBag(GridBagConstraints gbc, Component component, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        add(component, gbc);
    }
}
