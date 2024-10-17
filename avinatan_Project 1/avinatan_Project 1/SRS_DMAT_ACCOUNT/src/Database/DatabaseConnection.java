package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection connection;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=Amazon@123;trustServerCertificate=true";

    public DatabaseConnection() throws ClassNotFoundException {
        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Database connection closed");
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
