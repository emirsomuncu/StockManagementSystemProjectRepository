package Forms.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboardForm extends JFrame {
    private JPanel adminDashboardJP;
    private JButton productsButton;
    private JButton usersButton;
    private JButton adminsButton;

    public AdminDashboardForm() {
        Dimension buttonSize = new Dimension(210, 50);
        productsButton.setPreferredSize(buttonSize);
        usersButton.setPreferredSize(buttonSize);
        adminsButton.setPreferredSize(buttonSize);

        setTitle("Dashboard");
        setContentPane(adminDashboardJP);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminProductListForm adminProductListForm = new AdminProductListForm();
                dispose();
            }
        });
        usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminUserListForm adminUserListForm = new AdminUserListForm();
                dispose();
            }
        });
        adminsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminAdminListForm adminAdminListForm = new AdminAdminListForm();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        AdminDashboardForm adminDashboardForm = new AdminDashboardForm();
    }
}
