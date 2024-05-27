package com.se14.view;

import com.se14.domain.Issue;

import javax.swing.*;
import java.awt.*;

public class IssueDetailPanel extends JPanel {
    private JLabel titleLabel;
    private JTextArea descriptionArea;
    private JLabel statusLabel;
    private JLabel priorityLabel;
    private JLabel assigneeLabel;
    private JLabel fixerLabel;
    private JLabel reporterLabel;
    private JLabel reportedDateLabel;

    public IssueDetailPanel() {
        setLayout(new BorderLayout());

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        descriptionArea = new JTextArea();
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);

        JPanel smallDetailPanel = new JPanel();
        smallDetailPanel.setLayout(new GridLayout(7, 2));
        smallDetailPanel.setBorder(BorderFactory.createTitledBorder("Details"));

        statusLabel = new JLabel();
        priorityLabel = new JLabel();
        assigneeLabel = new JLabel();
        fixerLabel = new JLabel();
        reporterLabel = new JLabel();
        reportedDateLabel = new JLabel();

        smallDetailPanel.add(new JLabel("Status:"));
        smallDetailPanel.add(statusLabel);
        smallDetailPanel.add(new JLabel("Priority:"));
        smallDetailPanel.add(priorityLabel);
        smallDetailPanel.add(new JLabel("Assignee:"));
        smallDetailPanel.add(assigneeLabel);
        smallDetailPanel.add(new JLabel("Fixer:"));
        smallDetailPanel.add(fixerLabel);
        smallDetailPanel.add(new JLabel("Reporter:"));
        smallDetailPanel.add(reporterLabel);
        smallDetailPanel.add(new JLabel("Reported Date:"));
        smallDetailPanel.add(reportedDateLabel);

        detailPanel.add(titleLabel);
        detailPanel.add(new JScrollPane(descriptionArea));

        add(detailPanel, BorderLayout.CENTER);
        add(smallDetailPanel, BorderLayout.EAST);
    }

    public void setIssue(Issue issue) {
        titleLabel.setText(issue.getTitle());
        descriptionArea.setText(issue.getDescription());
        statusLabel.setText(issue.getStatus().toString());
        priorityLabel.setText(issue.getPriority().toString());
        assigneeLabel.setText(issue.getAssignee() != null ? issue.getAssignee().getUsername() : "");
        fixerLabel.setText(issue.getFixer() != null ? issue.getFixer().getUsername() : "");
        reporterLabel.setText(issue.getReporter().getUsername());
        reportedDateLabel.setText(issue.getReportedDate().toString());
    }
}
