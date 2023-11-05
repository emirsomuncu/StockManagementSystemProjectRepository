package Forms.Admin;
import Core.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminAccessForm extends JFrame {
    private JPanel adminDashboardJP;
    private JComboBox categoryCbx;
    private JTextField productField;
    private JTable table;
    private JButton deleteButton;
    private JButton addButton;
    private JButton updateButton;
    private DefaultTableModel tableModel;

    public AdminAccessForm() {
        setTitle("Admin Main Page");
        setContentPane(adminDashboardJP);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addButton.addActionListener(e -> {
            AdminAddForm adminAddForm = new AdminAddForm();
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
        // Önce tüm satırları temizleyin
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

                tableModel.addRow(new Object[]{id, category, name, price, unit});
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
