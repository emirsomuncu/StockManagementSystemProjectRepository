package Forms.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminProductListForm extends JFrame {


    private JPanel adminProductListJP;

    public AdminProductListForm() {
        setTitle("Products");
        setContentPane(adminProductListJP);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }


    public static void main(String[] args) {
        AdminProductListForm adminProductListForm = new AdminProductListForm();
    }
}
