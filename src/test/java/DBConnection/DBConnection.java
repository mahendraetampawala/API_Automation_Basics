package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/apitestingdb"; // Replace with your database URL
    private static final String USER = "root"; // Replace with your DB username
    private static final String PASS = "root"; // Replace with your DB password

    private Connection connection;

    // Constructor to initialize connection
    public DBConnection() throws SQLException {
        try {
            // Register the JDBC driver (optional for newer versions of JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL JDBC driver
            // Create the connection to the database
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection to the database established successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            throw e; // Propagate exception
        }
    }

    // Getter for the database connection
    public Connection getConnection() {
        return connection;
    }

    // Close the connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close the connection.");
                e.printStackTrace();
            }
        }
    }
}
