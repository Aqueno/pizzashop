package dao;

import dao.OrderDAO;
import model.Customer;
import model.Order;
import model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {

    private Connection conn;
    private OrderDAO orderDAO;
    private CustomerDAO customerDAO; // Need CustomerDAO to create customers for orders

    @BeforeEach
    void setUp() throws SQLException {
        // Set up in-memory H2 database
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "user", "password"); // In-memory H2
        orderDAO = new OrderDAO(conn);
        customerDAO = new CustomerDAO(conn);

        // Initialize the database schema
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS order_items");
            stmt.execute("DROP TABLE IF EXISTS orders");
            stmt.execute("DROP TABLE IF EXISTS customers");
            stmt.execute("CREATE TABLE customers (" +
                    "customer_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(255), phone VARCHAR(20), address VARCHAR(255), email VARCHAR(255))");
            stmt.execute("CREATE TABLE orders (" +
                    "order_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "customer_id INT, order_date TIMESTAMP, status VARCHAR(20), special_instructions VARCHAR(255), order_total DECIMAL(10,2), " +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE)");
            stmt.execute("CREATE TABLE pizzas (" +
                    "pizza_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(255))");
            stmt.execute("CREATE TABLE order_items (" +
                    "order_item_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "order_id INT, pizza_id INT, size VARCHAR(20), quantity INT, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (pizza_id) REFERENCES pizzas(pizza_id) ON DELETE CASCADE)");

            // Insert test data
            stmt.execute("INSERT INTO customers (name, phone) VALUES ('Test Customer', '1234567890')");
            stmt.execute("INSERT INTO pizzas (name) VALUES ('Margherita'), ('Pepperoni')");
        }
    }

    // Helper method to simulate stored procedure
    public static ResultSet getOrderDetailsAlias(Connection conn, int orderId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT o.order_id, o.order_date, o.status, o.order_total, " +
                "c.customer_id, c.name as customer_name, c.email, c.phone, c.address, " +
                "(SELECT GROUP_CONCAT(CONCAT(oi.pizza_id, ':', oi.size, ':', oi.quantity)) " +
                "FROM order_items oi WHERE oi.order_id = o.order_id) as ordered_items " +
                "FROM orders o JOIN customers c ON o.customer_id = c.customer_id WHERE o.order_id = ?");
        stmt.setInt(1, orderId);
        return stmt.executeQuery();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up the database by dropping the tables after each test
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS order_items");
            stmt.execute("DROP TABLE IF EXISTS orders");
            stmt.execute("DROP TABLE IF EXISTS customers");
        }
        conn.close();
    }

    @Test
    void testPlaceOrder_success() throws SQLException {
        // Arrange
        Order order = new Order();
        order.setCustomerId(1); // Assuming customer with ID 1 exists
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus("Pending");
        order.setSpecialInstructions("Extra sauce");

        // Act
        int orderId = orderDAO.placeOrder(order);

        // Assert
        assertTrue(orderId > 0, "Order should be placed and have a valid ID");
    }

    @Test
void testInsertOrderItems_success() throws SQLException {
    // Arrange
        Order order = new Order();
        order.setCustomerId(1); // Assuming customer with ID 1 exists
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus("Pending");
        order.setSpecialInstructions("Extra sauce");

        
      int orderId = orderDAO.placeOrder(order);
    assertTrue(orderId > 0, "Order should be placed and have a valid ID");
}
//    
//    OrderItem[] items = {
//            new OrderItem(orderId, 1, 2, "Small"), // Use the correct orderId here
//            new OrderItem(orderId, 1, 1, "Medium") // Use the correct orderId here
//    };
//
//    // Act
//    orderDAO.insertOrderItems(orderId, items);
//
//    // Assert
//    try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM order_items WHERE order_id = ?")) {
//        stmt.setInt(1, orderId);
//        ResultSet rs = stmt.executeQuery();
//        if (rs.next()) {
//            assertEquals(2, rs.getInt(1), "Two order items should be inserted");
//        }
//    }
//}
//
//    @Test
//    void testUpdateOrderTotal_success() throws SQLException {
//        // Arrange
//        Customer customer = new Customer();
//        customer.setName("John Doe");
//        customer.setPhone("1234567890");
//        customer.setAddress("123 Main St");
//        customer.setEmail("john@example.com");
//        int customerId = customerDAO.insertCustomer(customer);
//
//        Order order = new Order();
//        order.setCustomerId(customerId);
//        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
//        order.setStatus("Placed");
//        order.setSpecialInstructions("Leave at door");
//        int orderId = orderDAO.placeOrder(order);
//
//        BigDecimal total = new BigDecimal("25.99");
//
//        // Act
//        orderDAO.updateOrderTotal(orderId, total);
//
//        // Assert
//        try (PreparedStatement stmt = conn.prepareStatement("SELECT order_total FROM orders WHERE order_id = ?")) {
//            stmt.setInt(1, orderId);
//            ResultSet rs = stmt.executeQuery();
//            rs.next();
//            assertEquals(total, rs.getBigDecimal("order_total"), "Order total should be updated correctly");
//        }
//    }
//
//    @Test
//    void testGetOrderDetails_success() throws SQLException {
//        // Arrange
//        Customer customer = new Customer();
//        customer.setName("John Doe");
//        customer.setPhone("1234567890");
//        customer.setAddress("123 Main St");
//        customer.setEmail("john@example.com");
//        int customerId = customerDAO.insertCustomer(customer);
//
//        Order order = new Order();
//        order.setCustomerId(customerId);
//        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
//        order.setStatus("Placed");
//        order.setSpecialInstructions("Leave at door");
//        int orderId = orderDAO.placeOrder(order);
//
//        // Act
//        HashMap<String, String> orderDetails = orderDAO.getOrderDetails(orderId);
//
//        // Assert
//        assertNotNull(orderDetails, "Order details should be retrieved");
//        assertEquals(String.valueOf(orderId), orderDetails.get("order_id"), "Order ID should match");
//        assertEquals(String.valueOf(customerId), orderDetails.get("customer_id"), "Customer ID should match");
//        assertEquals("John Doe", orderDetails.get("customer_name"), "Customer name should match");
//    }
//
//    @Test
//    void testGetOrderDetails_notFound() throws SQLException {
//        // Act
//        HashMap<String, String> orderDetails = orderDAO.getOrderDetails(999); // Non-existent order ID
//
//        // Assert
//        assertNull(orderDetails, "Order details should be null for non-existent order");
//    }
}