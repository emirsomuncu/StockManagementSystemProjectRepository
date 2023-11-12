package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {
    public static String USERNAME = "root";  // These infos for our computers . You need to configure it for yourself
    public static String PASSWORD = "";  // These infos for our computers . You need to configure it for yourself

    public static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/stock_management_db";

    public static Connection connectToDatabase() {
        Connection connection = null;
        try {
            String url = DB_CONNECTION_URL;
            String username = USERNAME;
            String dbPassword = PASSWORD;
            connection = DriverManager.getConnection(url, username, dbPassword);
        } catch (SQLException ex) {
            System.err.println("Database error : " + ex.getMessage());
            ex.printStackTrace();
        }

        return connection;
    }
}

