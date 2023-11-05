package Forms.Admin;

import Core.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    public AdminAccessForm() {
        setTitle("Admin Main Page");
        setContentPane(adminDashboardJP);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == addButton) {
                    AdminAddForm adminAddForm = new AdminAddForm();
                    dispose();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == updateButton) {
                    AdminUpdateForm adminUpdateForm = new AdminUpdateForm();
                    dispose();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if(source == deleteButton) {
                    AdminDeleteForm adminDeleteForm = new AdminDeleteForm();
                    dispose();
                }
            }
        });

        loadTableData(); // Tabloyu verilerle doldur
    }

    private void loadTableData() {
        Connection connection = DatabaseConnection.connectToDatabase();
        DefaultTableModel tableModel = new DefaultTableModel();

        try {
            String query = "SELECT * FROM Products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Sütun başlıklarını alın ve tabloya ekleyin
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(resultSet.getMetaData().getColumnName(i));
            }

            // Verileri tabloya ekleyin
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(row);
            }

            // JTable'a DefaultTableModel'i bağlayın
            table.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminAccessForm adminAccessForm = new AdminAccessForm();
            }
        });
    }
}