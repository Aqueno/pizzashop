package dao;

import model.Customer;
import java.sql.SQLException;
import java.sql.*;
import util.DBConnection;

/**
 * Data Access Object (DAO) for handling customer-related database operations.
 * Provides methods for checking if a customer exists by phone number and for
 * inserting new customers into the database.
 */
public class CustomerDAO {

    private static final String CHECK_CUSTOMER_EXISTS = "SELECT customer_id FROM customers WHERE phone = ?";
    private static final String INSERT_CUSTOMER_QUERY = "INSERT INTO customers (name, phone, address, email) VALUES (?, ?, ?, ?)";

    /**
     * Checks if a customer exists in the database based on the phone number.
     *
     * @param phone The phone number of the customer.
     * @return The customer ID if the customer exists, or -1 if no customer is
     * found.
     * @throws SQLException If a database error occurs.
     */
    public int customerExistsByPhone(String phone) throws SQLException {
        int customerId = -1; // Default value if customer does not exist

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(CHECK_CUSTOMER_EXISTS)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve customer ID
                    customerId = rs.getInt("customer_id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return customerId;
    }

    /**
     * Inserts a new customer into the database.
     *
     * @param customer The customer object containing customer details.
     * @return The generated customer ID if the insertion is successful, or -1
     * if the insertion fails.
     * @throws SQLException If a database error occurs.
     */
    public int insertCustomer(Customer customer) throws SQLException {

        int generatedId = -1; // Default value if insertion fails

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(INSERT_CUSTOMER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getEmail());

            int affectedRows = stmt.executeUpdate();
            // Check if the insertion was successful
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1); // Retrieve the generated customer_id
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return generatedId;

    }
}
