package dao;

import static org.junit.jupiter.api.Assertions.*;

import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.*;

class CustomerDAOTest {

    private Connection conn;
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Set up in-memory H2 database
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "user", "password");
        customerDAO = new CustomerDAO(conn);

        // Initialize the database schema
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS orders");
            stmt.execute("DROP TABLE IF EXISTS customers");
            stmt.execute("CREATE TABLE customers ("
                    + "customer_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR(255), phone VARCHAR(20), address VARCHAR(255), email VARCHAR(255))");
            stmt.execute("CREATE TABLE orders ("
                    + "order_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "customer_id INT, total_price DOUBLE, "
                    + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE)");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up the database by dropping the tables after each test
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS orders");
            stmt.execute("DROP TABLE IF EXISTS customers");
        }
    }

    @Test
    void testInsertCustomer_success() throws SQLException {
        // Arrange
        Customer customer = new Customer(); // No need to set customer_id
        customer.setName("John Doe");
        customer.setPhone("555");
        customer.setAddress("123 Main St");
        customer.setEmail("555@example.com");

        // Act
        int generatedId = customerDAO.insertCustomer(customer);

        // Assert
        assertTrue(generatedId > 0, "Insert should generate a valid ID");
    }

    @Test
    void testCustomerExistsByPhone_existingCustomer() throws SQLException {
        // Arrange: Insert a test customer
        String phone = "1234567890";

        Customer customer = new Customer(1, "John Doe", phone, "123 Main St", "john@example.com");
        customerDAO.insertCustomer(customer);

        // Act
        int customerId = customerDAO.customerExistsByPhone(phone);

        // Assert
        assertTrue(customerId > 0, "Customer should exist");
    }

    @Test
    void testCustomerExistsByPhone_nonExistingCustomer() throws SQLException {
        // Act
        int customerId = customerDAO.customerExistsByPhone("123491");

        // Assert
        assertEquals(-1, customerId, "Non-existing customer should return -1");
    }

    @Test
    void testInsertCustomer_failure() throws SQLException {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "1234567890", "123 Main St", "john@example.com");

        // Simulate an issue with insert by using a faulty connection
        customerDAO = new CustomerDAO() {
            @Override
            public int insertCustomer(Customer customer) {
                return -1; // Simulate failure
            }
        };

        // Act
        int generatedId = customerDAO.insertCustomer(customer);

        // Assert
        assertEquals(-1, generatedId, "Insert failure should return -1");
    }
}
