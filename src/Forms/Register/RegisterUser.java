package Forms.Register;
import Database.DbHelper;
import Forms.User.UserLoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(conPassField.getPassword());

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("Please fill all the fields!");
                messageLabel.setForeground(Color.RED);
                return;
            }
                if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match!");
                messageLabel.setForeground(Color.RED);
                return;
            }

                try {
                Connection connection = DbHelper.connectToDatabase();

                String insertQuery = "INSERT INTO Users (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    messageLabel.setText("User registered successfully!");
                    messageLabel.setForeground(Color.GREEN);
                } else {
                    messageLabel.setText("User registration failed!");
                    messageLabel.setForeground(Color.RED);
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                messageLabel.setText("Database connection error");
                messageLabel.setForeground(Color.RED);
            }
        }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == backButton) {
                    UserLoginForm userLoginForm = new UserLoginForm();
                    dispose();
                }
            }
        });
    }
}
