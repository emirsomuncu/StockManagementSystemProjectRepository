package Forms.Admin;

import Database.DbHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class AdminAddForm extends JFrame {
    private JPanel panel1;
    private JTextField productField;
    private JComboBox<String> categoryCbx;
    private JTextField priceField;
    private JTextField unitField;
    private JTextField placeField;
    private JButton addButton;
    private JButton backButton;
    private JLabel messageLabel;
    private JTextField barcodeField;

    private DefaultTableModel tableModel;

    public AdminAddForm(DefaultTableModel tableModel) {
        this.tableModel = tableModel;

        setTitle("Add Page");
        setContentPane(panel1);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        loadCategories(); // Kategorileri JComboBox'a yükle

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInputValid()) {
                    String name = productField.getText();
                    String category = categoryCbx.getSelectedItem().toString();
                    double price = Double.parseDouble(priceField.getText());
                    int unit = Integer.parseInt(unitField.getText());
                    String place = placeField.getText();
                    int barcode = Integer.parseInt(barcodeField.getText());

                    if (addProductToDatabase(name, category, price, unit, place, barcode)) {
                        // Başarılı bir şekilde ürün eklendiyse tabloya ekle
                        tableModel.addRow(new Object[]{name, category, price, unit, place});
                        JOptionPane.showMessageDialog(AdminAddForm.this,
                                "Successfully added the product!",
                                "New Product",
                                JOptionPane.INFORMATION_MESSAGE);
                        AdminAccessForm adminAccessForm = new AdminAccessForm();
                        dispose(); // Pencereyi kapat
                    } else {
                        messageLabel.setText("Failed to add the product.");
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
                    AdminAccessForm adminAccessForm = new AdminAccessForm();
                    dispose();
                }
            }
        });
    }

    private void loadCategories() {
        Connection connection = DbHelper.connectToDatabase();
        categoryCbx.removeAllItems(); // Mevcut öğeleri temizle

        try {
            String query = "SELECT * FROM Category";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                categoryCbx.addItem(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isInputValid() {
        String name = productField.getText();
        String priceText = priceField.getText();
        String unitText = unitField.getText();
        String barcodeText = barcodeField.getText();
        String place = placeField.getText();


        if (name.isEmpty() || priceText.isEmpty() || unitText.isEmpty() || place.isEmpty()) {
            messageLabel.setText("Please fill all the fields!");
            messageLabel.setForeground(Color.RED);
            return false;
        }

        try {
            Double.parseDouble(priceText);
            Integer.parseInt(unitText);
            Integer.parseInt(barcodeText);
        } catch (NumberFormatException e) {
            messageLabel.setText("Price, Unit and Barcode must be valid numbers.");
            messageLabel.setForeground(Color.RED);
            return false;
        }

        return true;
    }

    private boolean addProductToDatabase(String name, String category, double price, int unit, String place, int barcode) {
        Connection connection = DbHelper.connectToDatabase();
        try {
            String query = "INSERT INTO Products (name, category, price, unit, place, barcode) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, category);
            statement.setDouble(3, price);
            statement.setInt(4, unit);
            statement.setString(5, place);
            statement.setInt(6, barcode);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
