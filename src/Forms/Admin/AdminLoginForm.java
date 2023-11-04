package Forms.Admin;

import Core.DatabaseConnection;
import Forms.Welcome.WelcomePageForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLoginForm extends JFrame {
    private JPanel adminPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton backButton;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public AdminLoginForm() {
        setTitle("Login");
        setContentPane(adminPanel);
        setMinimumSize(new Dimension(700, 500));
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
                        String Name = nameField.getText();
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
                                messageLabel.setText("Invalid username or password.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            loginButton.setEnabled(true); // Butonun tekrar etkinleştirilmesi
                        }
                    }
                };

                worker.execute(); // SwingWorker başlat
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
