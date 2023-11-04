package Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static String USERNAME = "sql12658917";
    public static String PASSWORD = "gqSrjLqaQq";
    public static String DB_CONNECTION_URL = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12658917";

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

