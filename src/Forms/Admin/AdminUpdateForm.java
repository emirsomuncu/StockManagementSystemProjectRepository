package Forms.Admin;

import Database.DbHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
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
    private JLabel messageLabel;
    private JTextField barcodeField;
    private JCheckBox productCheckBox;
    private JRadioButton barcodeRadio;

    private int stockIdToUpdate;

    public AdminUpdateForm(int stockId) {
        this.stockIdToUpdate = stockId;

        setTitle("Update Page");
        setContentPane(panel1);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        loadStockData(stockId);
        loadCategories();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInputValid()) {
                    String updatedProductName = productField.getText();
                    String updatedCategory = categoryCbx.getSelectedItem().toString();
                    double updatedPrice = Double.parseDouble(priceField.getText());
                    int updatedUnit = Integer.parseInt(unitField.getText());
                    String updatedPlace = placeField.getText();
                    int updatedBarcode = Integer.parseInt(barcodeField.getText());

                    if (updateProductInDatabase(stockIdToUpdate, updatedProductName, updatedCategory, updatedPrice, updatedUnit, updatedPlace, updatedBarcode)) {
                        JOptionPane.showMessageDialog(AdminUpdateForm.this,
                                "Successfully updated the stock!",
                                "Update Stock",
                                JOptionPane.INFORMATION_MESSAGE);
                        AdminAccessForm adminAccessForm = new AdminAccessForm();// Pencereyi kapat
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(AdminUpdateForm.this, "Ürün güncelleme başarısız!", "Hata", JOptionPane.ERROR_MESSAGE);
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
        productField.setEditable(false);
        barcodeField.setEditable(false);

        productCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                productField.setEditable(true);
            } else {
                productField.setEditable(false);
            }
        });


        barcodeRadio.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // productCheckBox seçili olduğunda
                barcodeField.setEditable(true);
            } else {
                // productCheckBox seçili değilse
                barcodeField.setEditable(false);
            }
        });
    }

    private void loadCategories() {
        Connection connection = DbHelper.connectToDatabase();
        categoryCbx.removeAllItems();

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
        String updatedProductName = productField.getText();
        String updatedCategory = categoryCbx.getSelectedItem().toString();
        String updatedPrice = priceField.getText();
        String updatedUnit = unitField.getText();
        String updatedPlace = placeField.getText();
        String updatedBarcode = barcodeField.getText();

        if (updatedProductName.isEmpty() || updatedCategory.isEmpty() || updatedPrice.isEmpty() || updatedUnit.isEmpty()) {
            messageLabel.setText("Please fill all the fields!");
            messageLabel.setForeground(Color.RED);
            return false;
        }

        try {
            Double.parseDouble(updatedPrice);
            Integer.parseInt(updatedUnit);
            Integer.parseInt(updatedBarcode);
        } catch (NumberFormatException e) {
            messageLabel.setText("Price, Unit and Barcode must be valid numbers.");
            messageLabel.setForeground(Color.RED);
            return false;
        }

        return true;
    }

    private void loadStockData(int stockId) {
        Connection connection = DbHelper.connectToDatabase();

        try {
            String query = "SELECT * FROM stocks WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, stockId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String productName = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int unit = resultSet.getInt("unit");
                String place = resultSet.getString("place");
                int barcode = resultSet.getInt("barcode");

                productField.setText(productName);
                categoryCbx.setSelectedItem(category);
                priceField.setText(String.valueOf(price));
                unitField.setText(String.valueOf(unit));
                placeField.setText(place);
                barcodeField.setText(String.valueOf(barcode));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean updateProductInDatabase(int stockId, String updatedProductName, String updatedCategory, double updatedPrice, int updatedUnit, String updatedPlace, int updatedBarcode) {
        Connection connection = DbHelper.connectToDatabase();

        try {
            String query = "UPDATE stocks SET name=?, category=?, price=?, unit=?, place=?, barcode=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, updatedProductName);
            statement.setString(2, updatedCategory);
            statement.setDouble(3, updatedPrice);
            statement.setInt(4, updatedUnit);
            statement.setString(5, updatedPlace);
            statement.setInt(6, updatedBarcode);
            statement.setInt(7, stockId);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminUpdateForm adminUpdateForm = new AdminUpdateForm(1);
        });
    }
}