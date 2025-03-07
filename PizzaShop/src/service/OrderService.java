package service;

import dao.OrderDAO;
import dao.CustomerDAO;
import model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;

/**
 * Service class responsible for handling operations related to placing and
 * managing orders.
 */
public class OrderService {

    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    /**
     * Default constructor that initializes the CustomerDAO and OrderDAO
     * objects.
     */
    public OrderService() {
        this.customerDAO = new CustomerDAO();
        this.orderDAO = new OrderDAO();
    }

    /**
     * Places an order for a customer and processes the order items.
     *
     * @param customer The customer placing the order.
     * @param orderItems The items being ordered.
     * @param specialInstructions Any special instructions provided by the
     * customer.
     * @param totalValue The total value of the order.
     * @return The unique order ID of the placed order.
     * @throws SQLException If there is an issue with the database interaction.
     */
    public int placeOrder(Customer customer, OrderItem[] orderItems, String specialInstructions, BigDecimal totalValue) throws SQLException {
        int customerId = customerDAO.customerExistsByPhone(customer.getPhone());
        System.out.println("---" + customerId);

        // Check if customer exists
        if (customerId == -1) {
            customerId = customerDAO.insertCustomer(customer);
        }

        // Create the order
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus("Pending");
        order.setSpecialInstructions(specialInstructions);
        order.setOrderTotal(totalValue);

        // Insert order into the database
        orderDAO.placeOrder(order);

        // Insert order items into the order_items table
        orderDAO.insertOrderItems(order.getOrderId(), orderItems);

        // Update the total price in the orders table
        orderDAO.updateOrderTotal(order.getOrderId(), totalValue);

        return order.getOrderId();
    }

    /**
     * Retrieves the details of an order based on the order ID.
     *
     * @param orderId The unique identifier of the order.
     * @return A map containing the details of the order.
     * @throws SQLException If there is an issue with the database interaction.
     */
    public HashMap<String, String> getOrderDetails(int orderId) throws SQLException {
        return orderDAO.getOrderDetails(orderId);
    }
}
