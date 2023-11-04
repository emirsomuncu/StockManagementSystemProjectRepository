package Forms.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminAdminListForm extends JFrame {

    private JPanel adminListJP;

    public AdminAdminListForm() {
        setTitle("Admins");
        setContentPane(adminListJP);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }


    public static void main(String[] args) {
        AdminAdminListForm adminAdminListForm = new AdminAdminListForm();
    }
}

