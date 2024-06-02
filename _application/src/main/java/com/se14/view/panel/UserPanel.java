package com.se14.view.panel;

import com.se14.dto.UserDTO;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserPanel extends JPanel {
    @Getter
    private final List<JRadioButton> radioButtons;
    ButtonGroup group = new ButtonGroup();

    public UserPanel() {
        radioButtons = new ArrayList<>();
    }

    public void setUsers(List<UserDTO> users) {
        removeAll();
        radioButtons.clear();
        setLayout(new GridLayout(users.size(), 1));
        for (UserDTO user : users) {
            JRadioButton radioButton = new JRadioButton(user.getUsername());
            radioButton.setActionCommand(user.getUsername());
            radioButtons.add(radioButton);
            radioButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            group.add(radioButton);
            add(radioButton);
        }
        revalidate();
    }

    public String getSelectedUsername() {
        if (group.getSelection() == null) return null;

        return group.getSelection().getActionCommand();
    }
}
