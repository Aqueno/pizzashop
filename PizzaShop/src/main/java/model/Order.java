package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents an order placed by a customer. Contains information about the
 * order, such as order ID, customer ID, order date, status, special
 * instructions, order total, and the last status update time.
 */
public class Order {

    private int orderId;
    private int customerId;
    private Timestamp orderDate;
    private String status;
    private String specialInstructions;
    private BigDecimal orderTotal;
    private Timestamp lastStatusUpdate;

    // Default constructor
    public Order() {
    }

    // Constructor with parameters
    public Order(int orderId, int customerId, Timestamp orderDate, String status,
            String specialInstructions, BigDecimal orderTotal, Timestamp lastStatusUpdate, String size) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.status = status;
        this.specialInstructions = specialInstructions;
        this.orderTotal = orderTotal;
        this.lastStatusUpdate = lastStatusUpdate;
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Timestamp getLastStatusUpdate() {
        return lastStatusUpdate;
    }

    public void setLastStatusUpdate(Timestamp lastStatusUpdate) {
        this.lastStatusUpdate = lastStatusUpdate;
    }

    /**
     * Returns a string representation of the Order object.
     *
     * @return A string containing the order details.
     */
    @Override
    public String toString() {
        return "Order{"
                + "orderId=" + orderId
                + ", customerId=" + customerId
                + ", orderDate=" + orderDate
                + ", status='" + status + '\''
                + ", specialInstructions='" + specialInstructions + '\''
                + ", orderTotal=" + orderTotal
                + ", lastStatusUpdate=" + lastStatusUpdate
                + '}';
    }
}
