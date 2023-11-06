package Forms.Admin;

import Core.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminUpdateForm extends JFrame {
    private JPanel panel1;
    private JTextField productField;
    private JComboBox<String> categoryCbx;
    private JTextField priceField;
    private JTextField unitField;
    private JTextField placeField;
    private JButton updateButton;
    private JButton backButton;

    private int productIdToUpdate; // Güncellenecek ürünün ID'sini saklamak için

    public AdminUpdateForm(int productId) {
        this.productIdToUpdate = productId; // Güncellenecek ürünün ID'sini all

        setTitle("Update Page");
        setContentPane(panel1);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Ürünü veritabanından alın ve alanlara yerleştirin
        loadProductData(productId);
        loadCategories();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Formdaki bilgileri al
                String updatedProductName = productField.getText();
                String updatedCategory = categoryCbx.getSelectedItem().toString();
                double updatedPrice = Double.parseDouble(priceField.getText());
                String updatedUnit = unitField.getText();
                String updatedPlace = placeField.getText();

                // Veritabanında güncelleme yap
                if (updateProductInDatabase(productIdToUpdate, updatedProductName, updatedCategory, updatedPrice, updatedUnit, updatedPlace)) {
                    // Güncelleme işlemi başarılı, formu kapatın
                    dispose();
                } else {
                    // Güncelleme işlemi başarısız, hata mesajı verilebilir
                    JOptionPane.showMessageDialog(AdminUpdateForm.this, "Ürün güncelleme başarısız!", "Hata", JOptionPane.ERROR_MESSAGE);
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
        Connection connection = DatabaseConnection.connectToDatabase();
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

    private void loadProductData(int productId) {
        Connection connection = DatabaseConnection.connectToDatabase(); // Veritabanı bağlantısını oluşturun

        try {
            String query = "SELECT * FROM Products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Veritabanından ürün bilgilerini çekin ve alanlara yerleştirin
                String productName = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                String unit = resultSet.getString("unit");
                String place = resultSet.getString("place");

                productField.setText(productName);
                categoryCbx.setSelectedItem(category);
                priceField.setText(String.valueOf(price));
                unitField.setText(unit);
                placeField.setText(place);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean updateProductInDatabase(int productId, String updatedProductName, String updatedCategory, double updatedPrice, String updatedUnit, String updatedPlace) {
        Connection connection = DatabaseConnection.connectToDatabase(); // Veritabanı bağlantısını oluşturun

        try {
            String query = "UPDATE Products SET name=?, category=?, price=?, unit=?, place=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, updatedProductName);
            statement.setString(2, updatedCategory);
            statement.setDouble(3, updatedPrice);
            statement.setString(4, updatedUnit);
            statement.setString(5, updatedPlace);
            statement.setInt(6, productId);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminUpdateForm adminUpdateForm = new AdminUpdateForm(1); // 1, güncellenecek ürünün ID'si
        });
    }
}
