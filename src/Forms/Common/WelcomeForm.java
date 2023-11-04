package Forms.Common;

import Forms.Admin.LoginForm;
import Forms.UI.UserLoginForm.UserLoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeForm extends JFrame {

    private JPanel welcomeJP;
    private JTextArea welcomeToProductManagementTextArea;
    private JButton userLoginButton;
    private JButton adminLoginButton;

    public WelcomeForm() {
        setTitle("Welcome");
        setContentPane(welcomeJP);
        setMinimumSize(new Dimension(700, 500));
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
                    LoginForm loginForm = new LoginForm();
                    dispose();
                }
            }
        });
    }

    public static void main(String[] args) {
        WelcomeForm welcomeForm = new WelcomeForm();
    }
}

