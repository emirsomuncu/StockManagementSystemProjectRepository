package Forms.Admin;
import Database.DbHelper;
import Forms.Welcome.WelcomePageForm;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.*;

public class AdminAccessForm extends JFrame {
    private JPanel adminDashboardJP;
    private JComboBox<String> categoryCbx;
    private JTextField productField;
    private JTable table;
    private JButton deleteButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton infoButton;
    private JButton backButton;
    private JScrollPane scrollPane;
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
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Object value = tableModel.getValueAt(selectedRow, 0);
                if (value instanceof Integer) {
                    int productId = (int) value;
                    AdminUpdateForm adminUpdateForm = new AdminUpdateForm(productId); // Ürün ID'sini iletebilirsiniz.
                    dispose();
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Object value = tableModel.getValueAt(selectedRow, 0);
                if (value instanceof Integer) {
                    int productId = (int) value;
                    if (deleteProductFromDatabase(productId)) {
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(AdminAccessForm.this,
                                "Successfully deleted the product!",
                                "Remove Product",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {

                }
            }
        });
        tableModel = new DefaultTableModel();
        table.setModel(tableModel);
        loadCategoryData();
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
        categoryCbx.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedCategory = categoryCbx.getSelectedItem().toString();
                if ("All Categories".equals(selectedCategory)) {
                    updateProductsByCategory(null);
                } else {
                    updateProductsByCategory(selectedCategory);
                }
            }
        });
        categoryCbx.addItem("All Categories"); //

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == infoButton) {
                    JOptionPane.showMessageDialog(AdminAccessForm.this,
                            "Welcome to the Admin's page! Here is a guide on how to use our system." +
                                    "\n 1- You can check the categories by simply clicking on which category you want to see." +
                                    "\n 2- You can check each stock by typing their product names on the given field." +
                                    "\n 3- To Add a stock, you simply have to click the Add button at the bottom of the screen and enter the information of the stock." +
                                    "\n 4- To Update a stock, click on the stock you want to update from the table and then click the Update button at the bottom of the screen." +
                                    "\n 5- To Delete a stock, simply click on the stock you want to delete from the table and then click the Delete button at the bottom of the screen.",
                            "Important information about the Admin page",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(AdminAccessForm.this,
                        "Are you sure you want to sign out?",
                        "Sign out",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Successfully signed out!");
                    WelcomePageForm welcomePageForm = new WelcomePageForm();
                    dispose();
                }
                else if (result == JOptionPane.NO_OPTION) {

                }
            }
        });
    }
    private void loadCategoryData() {
        Connection connection = DbHelper.connectToDatabase();
        try {
            String query = "SELECT DISTINCT Category FROM Products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String category = resultSet.getString("Category");
                categoryCbx.addItem(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadTableData() {
        Connection connection = DbHelper.connectToDatabase();
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
        Connection connection = DbHelper.connectToDatabase();
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
                String barcode = resultSet.getString("barcode");
                Object[] row = {id, category, name, price, unit, place, barcode};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateProductsByCategory(String selectedCategory) {
        tableModel.setRowCount(0);
        Connection connection = DbHelper.connectToDatabase();
        try {
            String query;
            PreparedStatement statement;
            if (selectedCategory == null) {
                query = "SELECT * FROM Products";
                statement = connection.prepareStatement(query);
            } else {
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
                String barcode = resultSet.getString("barcode");
                Object[] row = {id, category, name, price, unit, place, barcode};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean deleteProductFromDatabase(int productId) {
        Connection connection = DbHelper.connectToDatabase();
        try {
            String query = "DELETE FROM Products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminAccessForm adminAccessForm = new AdminAccessForm();
        });
    }
}