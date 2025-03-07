package dao;

import java.math.BigDecimal;
import model.Order;
import model.OrderItem;
import java.sql.SQLException;

import java.sql.*;
import java.util.HashMap;
import util.DBConnection;

/**
 * Data Access Object (DAO) for handling order-related database operations.
 * Provides methods for placing orders, inserting order items, updating order
 * totals, and retrieving order details from the database.
 */
public class OrderDAO {

    private static final String PLACE_ORDER_QUERY = "INSERT INTO orders (customer_id, order_date, status, special_instructions) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ORDER_TOTAL_QUERY = "UPDATE orders SET order_total = ? WHERE order_id = ?";
    private static final String GET_ORDER_DETAILS_PROCEDURE = "{CALL GetOrderDetails(?)}";
    private static final String INSERT_ORDER_ITEM_QUERY = "INSERT INTO order_items (order_id, pizza_id, size, quantity) VALUES (?, ?, ?, ?)";

    /**
     * Places a new order in the database and returns the generated order ID.
     *
     * @param order The order object containing the details of the order.
     * @return The generated order ID if the order is successfully placed, or -1
     * if the insertion fails.
     * @throws SQLException If a database error occurs.
     */
    public int placeOrder(Order order) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(PLACE_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setString(3, order.getStatus());
            stmt.setString(4, order.getSpecialInstructions());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setOrderId(rs.getInt(1));
                    return order.getOrderId();
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Inserts multiple order items into the database for a specific order.
     *
     * @param orderId The order ID to which the items belong.
     * @param orderItems An array of OrderItem objects containing the details of
     * the items to insert.
     * @throws SQLException If a database error occurs.
     */
    public void insertOrderItems(int orderId, OrderItem[] orderItems) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(INSERT_ORDER_ITEM_QUERY)) {
            for (OrderItem item : orderItems) {
                ps.setInt(1, orderId);
                ps.setInt(2, item.getPizzaId());
                ps.setString(3, item.getSize());
                ps.setInt(4, item.getQuantity());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Updates the total amount for a specific order in the database.
     *
     * @param orderId The ID of the order to update.
     * @param total The new total amount for the order.
     * @throws SQLException If a database error occurs.
     */
    public void updateOrderTotal(int orderId, BigDecimal total) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(UPDATE_ORDER_TOTAL_QUERY)) {
            stmt.setBigDecimal(1, total);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the details of an order, including customer and order
     * information.
     *
     * @param orderId The ID of the order to retrieve details for.
     * @return A HashMap containing the order details, or null if no order is
     * found.
     * @throws SQLException If a database error occurs.
     */
    public HashMap<String, String> getOrderDetails(int orderId) throws SQLException {
        HashMap<String, String> map = new HashMap<String, String>();
        try (Connection conn = DBConnection.getConnection(); CallableStatement stmt = conn.prepareCall(GET_ORDER_DETAILS_PROCEDURE)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    // No results found, return null
                    return null;
                }

                // Process the order details result
                do {
                    map.put("order_id", String.valueOf(rs.getInt("order_id")));
                    map.put("order_date", String.valueOf(rs.getTimestamp("order_date")));
                    map.put("order_status", rs.getString("status"));
                    map.put("order_total", String.valueOf(rs.getBigDecimal("order_total")));
                    map.put("ordered_items", rs.getString("ordered_items"));
                    map.put("customer_id", String.valueOf(rs.getInt("customer_id")));
                    map.put("customer_name", rs.getString("customer_name"));
                    map.put("phone", rs.getString("phone"));
                    map.put("address", rs.getString("address"));
                } while (rs.next()); // If there are multiple rows, process all of them.

                return map;
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }
}
