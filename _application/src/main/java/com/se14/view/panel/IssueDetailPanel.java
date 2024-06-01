package com.se14.view.panel;

import com.se14.controller.ViewController;
import com.se14.controller.panel.IssueDetailController;
import com.se14.domain.IssueStatus;
import com.se14.dto.CommentDTO;
import com.se14.dto.IssueDTO;
import com.se14.view.panel.CommentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class IssueDetailPanel extends JPanel {
    private final IssueDetailController controller;

    private JLabel titleLabel;
    private JTextArea descriptionArea;
    private JLabel statusLabel;
    private JLabel priorityLabel;
    private JLabel assigneeLabel;
    private JLabel fixerLabel;
    private JLabel reporterLabel;
    private JLabel reportedDateLabel;

    private JPanel commentsPanel;
    private JTextArea contentArea;
    private JButton postButton;
    private JScrollPane commentsScrollPane;

    private JButton changeStateButton;
    private JButton assignDeveloperButton;

    private IssueDTO issue;

    public IssueDetailPanel(IssueDetailController controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        descriptionArea = new JTextArea();
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);

        JPanel smallDetailPanel = new JPanel();
        smallDetailPanel.setLayout(new GridBagLayout());
        smallDetailPanel.setBorder(BorderFactory.createTitledBorder("Details"));

        statusLabel = new JLabel();
        priorityLabel = new JLabel();
        assigneeLabel = new JLabel();
        fixerLabel = new JLabel();
        reporterLabel = new JLabel();
        reportedDateLabel = new JLabel();

        changeStateButton = new JButton("Change State");
        assignDeveloperButton = new JButton("Assign Developer");

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        addToGridBag(smallDetailPanel, gbc, new JLabel("Status"), 0, 0, 1, 1);
        addToGridBag(smallDetailPanel, gbc, new JLabel("Priority"), 0, 1, 1, 1);
        addToGridBag(smallDetailPanel, gbc, new JLabel("Assignee"), 0, 2, 1, 1);
        addToGridBag(smallDetailPanel, gbc, new JLabel("Fixer"), 0, 3, 1, 1);
        addToGridBag(smallDetailPanel, gbc, new JLabel("Reporter"), 0, 4, 1, 1);
        addToGridBag(smallDetailPanel, gbc, new JLabel("Reported Date"), 0, 5, 1, 1);
        addToGridBag(smallDetailPanel, gbc, statusLabel, 1, 0, 1, 1);
        addToGridBag(smallDetailPanel, gbc, priorityLabel, 1, 1, 1, 1);
        addToGridBag(smallDetailPanel, gbc, assigneeLabel, 1, 2, 1, 1);
        addToGridBag(smallDetailPanel, gbc, fixerLabel, 1, 3, 1, 1);
        addToGridBag(smallDetailPanel, gbc, reporterLabel, 1, 4, 1, 1);
        addToGridBag(smallDetailPanel, gbc, reportedDateLabel, 1, 5, 1, 1);
        addToGridBag(smallDetailPanel, gbc, changeStateButton, 2, 0, 1, 1);
        addToGridBag(smallDetailPanel, gbc, assignDeveloperButton, 2, 2, 1, 1);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Comments"));

        contentArea = new JTextArea(4, 80);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        postButton = new JButton("Post");

        commentsPanel = new JPanel();
        commentsScrollPane = new JScrollPane(commentsPanel);
        commentsScrollPane.setPreferredSize(new Dimension(getPreferredSize().width, 300));

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.9;
        gbc.weighty = 0.2;
        addToGridBag(bottomPanel, gbc, contentScrollPane, 0, 0, 1, 1);
        gbc.weightx = 0.1;
        addToGridBag(bottomPanel, gbc, postButton, 1, 0, 1, 1);
        gbc.weighty = 0.8;
        addToGridBag(bottomPanel, gbc, commentsScrollPane, 0, 1, 2, 1);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        addToGridBag(this, gbc, titleLabel, 0, 0, 2, 1);
        gbc.weightx = 0.6;
        gbc.weighty = 0.2;
        addToGridBag(this, gbc, descriptionArea, 0, 1, 1, 1);
        gbc.weightx = 0.4;
        addToGridBag(this, gbc, smallDetailPanel, 1, 1, 1, 1);
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        addToGridBag(this, gbc, bottomPanel, 0, 2, 2, 1);

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.postComment(contentArea.getText());
            }
        });

        changeStateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int select = JOptionPane.showConfirmDialog(IssueDetailPanel.this, getChangeStateMessage(), "Change State", JOptionPane.YES_NO_OPTION);
                if (select == JOptionPane.YES_OPTION) {
                    controller.changeState();
                }
            }
        });

        assignDeveloperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAssignDeveloperModal();
            }
        });
    }

    private String getChangeStateMessage() {
        IssueStatus current = IssueStatus.valueOf(statusLabel.getText());
        IssueStatus next = controller.getNextStatus(current);
        return current + " -> " + next;
    }

    private void addToGridBag(JPanel panel, GridBagConstraints gbc, Component component, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        panel.add(component, gbc);
    }

    public static void main(String[] args) {
        new IssueDetailPanel(new IssueDetailController(new ViewController())).setVisible(true);
    }

    public void setIssue(IssueDTO issue) {
        titleLabel.setText(issue.getTitle());
        descriptionArea.setText(issue.getDescription());
        statusLabel.setText(issue.getStatus().toString());
        priorityLabel.setText(issue.getPriority().toString());
        assigneeLabel.setText(issue.getAssignee() == null ? "Unassigned" : issue.getAssignee());
        fixerLabel.setText(issue.getFixer() == null ? "Unassigned" : issue.getFixer());
        reporterLabel.setText(issue.getReporter());
        reportedDateLabel.setText(issue.getReportedDate());

        this.issue = issue;
    }

    public void setButtons(boolean assignDeveloperVisible, boolean changeStateVisible) {
        assignDeveloperButton.setEnabled(assignDeveloperVisible);
        changeStateButton.setEnabled(changeStateVisible);
    }

    public void setComments(List<CommentDTO> comments) {
        commentsPanel.removeAll();
        commentsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        for (CommentDTO comment : comments) {
            commentsPanel.add(new CommentPanel(comment), gbc);
            gbc.gridy++;
        }
        commentsPanel.revalidate();
        commentsPanel.repaint();

        // always scroll bottom -> not working
        commentsScrollPane.getVerticalScrollBar().setValue(commentsScrollPane.getVerticalScrollBar().getMaximum());
    }

    public void clearCommentField() {
        contentArea.setText("");
    }
}
