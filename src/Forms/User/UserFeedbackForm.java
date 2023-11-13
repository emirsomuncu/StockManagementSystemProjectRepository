package Forms.User;

import Database.DbHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserFeedbackForm extends JFrame {
    private JPanel panel1;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea messageArea;
    private JButton sendButton;
    private JButton backButton;
    private JLabel messageLabel;
    private JTextField subjectField;

    public UserFeedbackForm() {
        setTitle("User Feedback Page");
        setContentPane(panel1);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(feedbackIsValid()) {
                    String subject = subjectField.getText();
                    String message = messageArea.getText();
                    String name = nameField.getText();
                    String email = emailField.getText();
                    int phone = Integer.parseInt(phoneField.getText());

                    try {
                        Connection connection = DbHelper.connectToDatabase();

                        String insertQuery = "INSERT INTO Feedback (subject, message, name, email, phone) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                        preparedStatement.setString(1, subject);
                        preparedStatement.setString(2, message);
                        preparedStatement.setString(3, name);
                        preparedStatement.setString(4, email);
                        preparedStatement.setInt(5, phone);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            messageLabel.setText("Successfully sended your feedback!");
                            messageLabel.setForeground(Color.GREEN);
                        } else {
                            messageLabel.setText("Sending feedback failed!");
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

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == backButton) {
                    UserAccessForm userAccessForm = new UserAccessForm();
                    dispose();
                }
            }
        });
    }

    private boolean feedbackIsValid() {
        String subject = subjectField.getText();
        String message = messageArea.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String phoneText = phoneField.getText();

        if (name.isEmpty() || email.isEmpty() || message.isEmpty() || subject.isEmpty()) {
            messageLabel.setText("Please fill all the fields!");
            messageLabel.setForeground(Color.RED);
            return false;
        }

        try {
            Integer.parseInt(phoneText);
        }
        catch (NumberFormatException e) {
            messageLabel.setText("Your phone number must contain valid numbers (Country code not included).");
            messageLabel.setForeground(Color.RED);
            return false;
        }
        return true;
    } //end of feedbackIsValid
}
