package Forms.User;

import Core.DatabaseConnection;
import Forms.Register.RegisterUser;
import Forms.Welcome.WelcomePageForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLoginForm extends JFrame {
    private JPanel userLoginPanel;
    private JButton registerButton;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private JButton backButton;

    public UserLoginForm() {
        setTitle("User Login");
        setContentPane(userLoginPanel);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.setEnabled(false); // Butonun etkisizleştirilmesi

                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        String email = emailField.getText();
                        char[] passwordChars = passwordField.getPassword();
                        String password = new String(passwordChars);

                        Connection connection = DatabaseConnection.connectToDatabase();

                        String query = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setString(1, email);
                            preparedStatement.setString(2, password);

                            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                return resultSet.next();
                            }
                        } catch (SQLException ex) {
                            System.err.println("Database error: " + ex.getMessage());
                            ex.printStackTrace();
                        } finally {
                            connection.close();
                        }

                        return false;
                    }

                    @Override
                    protected void done() {
                        try {
                            boolean loggedIn = get();
                            if (loggedIn) {
                                System.out.println("Successfully logged in.");
                                WelcomePageForm welcomePageForm = new WelcomePageForm();
                                dispose();
                            } else {
                                messageLabel.setText("Invalid email or password.");
                                messageLabel.setForeground(Color.RED);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            loginButton.setEnabled(true); // Butonun tekrar etkinleştirilmesi
                        }
                    }
                };

                worker.execute(); // SwingWorker'ı başlatma
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == registerButton) {
                    RegisterUser registerUser = new RegisterUser();
                    dispose();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == backButton) {
                    WelcomePageForm welcomePageForm = new WelcomePageForm();
                    dispose();
                }
            }
        });
    }
}

// loginForm.java
