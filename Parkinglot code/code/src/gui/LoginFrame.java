package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import user.User;
import user.UserFactory;
import user.MaintainUser;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private MaintainUser maintainUser;

    public LoginFrame() {
        maintainUser = new MaintainUser();
        try {
            maintainUser.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("User Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Email:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Log in");
        loginButton.addActionListener(new LoginAction());
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        panel.add(loginButton);
        panel.add(registerButton);
        add(panel);
        setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (maintainUser.verifyUser(email, password)) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                new MainFrame(email);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        }
    }
}