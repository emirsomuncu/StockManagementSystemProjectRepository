package Forms.Register;

import Forms.Welcome.WelcomePageForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUser extends JFrame {
    private JPanel registerPanel;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField conPassField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel messageLabel;

    public RegisterUser() {
        setTitle("Register Page");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == registerButton) {
                    // Get the name, email, password and confirm password from the text fields
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String password = new String(passwordField.getPassword());
                    String confirmPassword = new String(conPassField.getPassword());

                    // Check if the name, email, password and confirm password are not empty
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        // If any of them is empty, display an error message and return
                        messageLabel.setText("Please fill all the fields!");
                        messageLabel.setForeground(Color.RED);
                        return;
                    }
                    if (!password.equals(confirmPassword)) {
                        // If they are not the same, display an error message and return
                        messageLabel.setText("Passwords do not match!");
                        messageLabel.setForeground(Color.RED);
                        return;
                    }
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == backButton) {
                    // Create an instance of the LoginForm class
                    WelcomePageForm welcomePageForm = new WelcomePageForm();

                    // Dispose the current frame
                    dispose();
                }

            }
        });
    }
}
