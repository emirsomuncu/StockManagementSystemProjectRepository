package Forms.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminUserListForm extends JFrame{
    private JPanel adminUserListJP;

    public AdminUserListForm() {
        setTitle("Users");
        setContentPane(adminUserListJP);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }
}
