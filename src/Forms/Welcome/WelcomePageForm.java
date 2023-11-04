package Forms.Welcome;

import Forms.Admin.AdminLoginForm;
import Forms.User.UserLoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePageForm extends JFrame {

    private JPanel loginPanel;
    private JButton userLoginButton;
    private JButton adminLoginButton;

    public WelcomePageForm() {
        setTitle("Welcome Page");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == userLoginButton) {
                    UserLoginForm userLoginForm = new UserLoginForm();
                    dispose();
                }
            }
        });

        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == adminLoginButton) {
                    AdminLoginForm adminLoginForm = new AdminLoginForm();
                    dispose();
                }
            }
        });
    }

    public static void main(String[] args) {

        WelcomePageForm welcomePageForm = new WelcomePageForm();
    }
}

