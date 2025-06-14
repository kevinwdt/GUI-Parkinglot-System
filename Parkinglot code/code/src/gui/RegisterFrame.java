package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import user.User;
import user.UserFactory;
import user.MaintainUser;

public class RegisterFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeDropdown;
    private JButton registerButton;
    private MaintainUser maintainUser;

    public RegisterFrame() {
        maintainUser = new MaintainUser();
        
        try {
            maintainUser.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("User Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        
        panel.add(new JLabel("User Type:"));
        userTypeDropdown = new JComboBox<>(new String[]{"Student", "Faculty", "Non-Faculty", "Visitor"});
        panel.add(userTypeDropdown);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAction());
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userType = (String) userTypeDropdown.getSelectedItem();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User newUser = UserFactory.createUser(userType, name, email, password);
            maintainUser.addUser(newUser);
            
            try {
                maintainUser.update(email);
                JOptionPane.showMessageDialog(null, userType +" " + name + " (" + email + ") " + "registered successfully");
                new LoginFrame();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Information incorrectly saved.");
            }
        }
    }
}
