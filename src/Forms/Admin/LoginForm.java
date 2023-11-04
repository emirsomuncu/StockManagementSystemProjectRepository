package Forms.Admin;

import Core.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame {
    private JPanel loginJP;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTextArea errorTextArea;

    public LoginForm() {
        setTitle("Login");
        setContentPane(loginJP);
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
                        String Name = emailField.getText();
                        char[] passwordChars = passwordField.getPassword(); // Şifreyi char[] olarak al
                        String password = new String(passwordChars);
                        //String password = new String(passwordChars);

                        Connection connection = DatabaseConnection.connectToDatabase();

                        String query = "SELECT * FROM Admins WHERE Name = ? AND Password = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setString(1, Name);
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
                                AdminDashboardForm adminDashboardForm = new AdminDashboardForm();
                                dispose();
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
