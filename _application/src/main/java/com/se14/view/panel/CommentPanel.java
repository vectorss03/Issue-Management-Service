package com.se14.view.panel;

import com.se14.domain.Comment;
import com.se14.dto.CommentDTO;

import javax.swing.*;
import java.awt.*;

public class CommentPanel extends JPanel {
    private final JLabel authorLabel;
    private final JTextArea contentLabel;
    private final JLabel dateLabel;

    public CommentPanel(CommentDTO comment) {
        authorLabel = new JLabel("<html><B>" + comment.getAuthor() + "</B></html>");
        contentLabel = new JTextArea(comment.getContent());
        contentLabel.setEditable(false);
        contentLabel.setRows(4);
        dateLabel = new JLabel(comment.getTimeStamp());

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        addToGridBag(gbc, authorLabel, 0, 0, 1, 1);
        addToGridBag(gbc, contentLabel, 0, 1, 2, 1);
        gbc.weightx = 1;
        addToGridBag(gbc, dateLabel, 1, 0, 1, 1);

        setPreferredSize(new Dimension(1140, getPreferredSize().height));

    }

    private void addToGridBag(GridBagConstraints gbc, Component component, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        add(component, gbc);
    }
}
