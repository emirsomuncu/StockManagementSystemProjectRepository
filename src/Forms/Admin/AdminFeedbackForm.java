package Forms.Admin;

import Database.DbHelper;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.sql.*;

public class AdminFeedbackForm extends JFrame {
    private JTable table;
    private JButton feedbackButton;
    private JPanel adminFeedbackJP;
    private JTree tree1;
    private DefaultTableModel tableModel;

    public AdminFeedbackForm() {
        setTitle("Admin Feedback Page");
        setContentPane(adminFeedbackJP);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Admin Forms");
        DefaultMutableTreeNode feedback = new DefaultMutableTreeNode("adminAccess");
        root.add(feedback);
        tree1.setModel(new JTree(root).getModel());

        // JTable'da dosyalara tıklandığında formun açılmasını sağla
        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                if (selectedNode != null) {
                    String selectedFile = selectedNode.toString();
                    if ("adminAccess".equals(selectedFile)) {
                        AdminAccessForm accessForm = new AdminAccessForm();
                        dispose();
                    }
                }
            }
        });

        // JTable için model oluştur
        tableModel = new DefaultTableModel();
        table.setModel(tableModel);
        loadTableData();

        // Feedback Button
        feedbackButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Object messageValue = tableModel.getValueAt(selectedRow, tableModel.findColumn("Message"));
                if (messageValue != null) {
                    String message = messageValue.toString();

                    JTextArea textArea = new JTextArea(message);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(800, 600));  // İstenilen boyutu ayarlayabilirsiniz

                    JOptionPane.showMessageDialog(this, scrollPane, "Feedback Message", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row!", "Feedback Message", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void loadTableData() {
        Connection connection = DbHelper.connectToDatabase();
        try {
            String query = "SELECT * FROM Feedback";
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminFeedbackForm adminFeedbackForm = new AdminFeedbackForm();
        });
    }
}
