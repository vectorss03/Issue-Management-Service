package com.se14.view;

import com.se14.controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame{
    private LoginController controller;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public void setController(LoginController controller) {
        this.controller = controller;
    }

    public LoginView() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.WEST;
        addToGridBag(gbc, new JLabel("Username"), 0, 0, 1, 1);
        addToGridBag(gbc, new JLabel("Password"), 0, 1, 1, 1);

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addToGridBag(gbc, usernameField, 1, 0, 1, 1);
        addToGridBag(gbc, passwordField, 1, 1, 1, 1);
        addToGridBag(gbc, loginButton, 0, 2, 2, 1);

        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                controller.attemptLogin(username, password);
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

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
}
