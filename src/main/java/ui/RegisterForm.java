package ui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JFrame {
    public RegisterForm() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Register New User");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField fullNameField = new JTextField();
        String[] roles = {"student", "admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Role:"));
        panel.add(roleBox);
        panel.add(registerBtn);
        panel.add(backBtn);

        registerBtn.addActionListener(e -> handleRegistration(
                usernameField.getText(),
                new String(passwordField.getPassword()),
                fullNameField.getText(),
                (String) roleBox.getSelectedItem()
        ));

        backBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        add(panel);
        setVisible(true);
    }

    private void handleRegistration(String username, String password,
                                    String fullName, String role) {
        if (username.trim().isEmpty() || password.trim().isEmpty() ||
                fullName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required!",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User(0, username, password, fullName, role);
        if (new UserService().registerUser(user)) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginForm();
        }
    }
}