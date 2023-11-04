package Forms.UI.UserLoginForm;

import Core.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLoginForm extends JFrame {
    private JPanel userLoginJP;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTextArea errorTextArea;

    public UserLoginForm() {
        setTitle("Login");
        setContentPane(userLoginJP);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        errorTextArea.setForeground(Color.RED);
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

                        String query = "SELECT * FROM User WHERE Email = ? AND Password = ?";
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
                            } else {
                                errorTextArea.setText("Invalid username or password.");
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
    }
}

// loginForm.java
