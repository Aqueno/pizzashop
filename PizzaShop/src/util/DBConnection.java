package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections. Provides a method to
 * establish a connection to the database.
 */
public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://pizza-shop-db.cxsiso6sojyh.us-east-2.rds.amazonaws.com:3306/pizza_shop";
    private static final String DB_USER = "app_user";
    private static final String DB_PASSWORD = "securepassword123";

    /**
     * Establishes a connection to the database.
     *
     * @return A Connection object that represents the established connection to
     * the database.
     * @throws SQLException If there is an issue with establishing the database
     * connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unable to establish a database connection.");
        }
    }
}
