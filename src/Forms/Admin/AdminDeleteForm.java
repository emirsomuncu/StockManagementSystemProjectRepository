package Forms.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminDeleteForm extends JFrame{
    private JPanel panel1;

    public AdminDeleteForm() {
        setTitle("Delete Page");
        setContentPane(panel1);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
