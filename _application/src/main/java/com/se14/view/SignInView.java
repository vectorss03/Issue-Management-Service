package com.se14.view;


import com.se14.controller.MainController;
import com.se14.controller.SignInController;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInView extends JFrame {
    private final SignInController controller;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton signInButton;

    public SignInView(SignInController controller) {
        this.controller = controller;

        setTitle("Sign In");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        emailField = new JTextField(15);
        signInButton = new JButton("Sign In");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.anchor = GridBagConstraints.WEST;
        addToGridBag(gbc, new JLabel("Username"), 0, 0, 1, 1);
        addToGridBag(gbc, new JLabel("Password"), 0, 1, 1, 1);
        addToGridBag(gbc, new JLabel("Email"), 0, 2, 1, 1);

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addToGridBag(gbc, usernameField, 1, 0, 1, 1);
        addToGridBag(gbc, passwordField, 1, 1, 1, 1);
        addToGridBag(gbc, emailField, 1, 2, 1, 1);
        addToGridBag(gbc, signInButton, 0, 3, 2, 1);

        getRootPane().setDefaultButton(signInButton);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                controller.attemptSignIn(username, password, email);
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

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Sign in successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Sign In Error", JOptionPane.ERROR_MESSAGE);
    }
}
