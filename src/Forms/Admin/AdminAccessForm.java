package Forms.Admin;

import Core.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminAccessForm extends JFrame {
    private JPanel adminDashboardJP;
    private JComboBox<String> categoryCbx;
    private JTextField productField;
    private JTable table;
    private JButton deleteButton;
    private JButton addButton;
    private JButton updateButton;
    private DefaultTableModel tableModel;

    public AdminAccessForm() {
        setTitle("Admin Main Page");
        setContentPane(adminDashboardJP);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addButton.addActionListener(e -> {
            AdminAddForm adminAddForm = new AdminAddForm(tableModel);
            dispose();
        });

        updateButton.addActionListener(e -> {
            AdminUpdateForm adminUpdateForm = new AdminUpdateForm();
            dispose();
        });

        deleteButton.addActionListener(e -> {
            AdminDeleteForm adminDeleteForm = new AdminDeleteForm();
            dispose();
        });

        tableModel = new DefaultTableModel();
        table.setModel(tableModel);

        loadCategoryData(); // Kategori verilerini yükle
        loadTableData();

        productField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchProducts(productField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchProducts(productField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchProducts(productField.getText());
            }
        });

        categoryCbx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedCategory = categoryCbx.getSelectedItem().toString();
                    if ("All Categories".equals(selectedCategory)) {
                        // Eğer "All Categories" seçildiyse, tüm ürünleri göster
                        updateProductsByCategory(null); // null kategori için
                    } else {
                        // Seçilen kategoriye göre ürünleri göster
                        updateProductsByCategory(selectedCategory);
                    }
                }
            }
        });

        // "All Categories" seçeneğini ekleyin
        categoryCbx.addItem("All Categories");
    }

    private void loadCategoryData() {
        Connection connection = DatabaseConnection.connectToDatabase();
        try {
            String query = "SELECT DISTINCT category FROM Products";
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

    private void loadTableData() {
        Connection connection = DatabaseConnection.connectToDatabase();

        try {
            String query = "SELECT * FROM Products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(resultSet.getMetaData().getColumnName(i));
            }

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchProducts(String searchTerm) {
        tableModel.setRowCount(0);

        Connection connection = DatabaseConnection.connectToDatabase();
        try {
            String query = "SELECT * FROM Products WHERE name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String unit = resultSet.getString("unit");
                String place = resultSet.getString("place");

                tableModel.addRow(new Object[]{id, category, name, price, unit, place});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProductsByCategory(String selectedCategory) {
        tableModel.setRowCount(0);

        Connection connection = DatabaseConnection.connectToDatabase();
        try {
            String query;
            PreparedStatement statement;

            if (selectedCategory == null) {
                // Tüm kategoriler için
                query = "SELECT * FROM Products";
                statement = connection.prepareStatement(query);
            } else {
                // Belirli kategori için
                query = "SELECT * FROM Products WHERE category = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, selectedCategory);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String unit = resultSet.getString("unit");
                String place = resultSet.getString("place");

                tableModel.addRow(new Object[]{id, category, name, price, unit, place});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminAccessForm adminAccessForm = new AdminAccessForm();
        });
    }
}
