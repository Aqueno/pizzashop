package model;

import java.sql.Timestamp;

/**
 * Represents an update to the status of an order, including the time of the
 * update and the new status associated with the order.
 */
public class OrderStatusUpdate {

    private int id;
    private int orderId;
    private Timestamp statusUpdateTime;
    private String newStatus;

    // Default constructor
    public OrderStatusUpdate() {
    }

    // Constructor with parameters
    public OrderStatusUpdate(int id, int orderId, Timestamp statusUpdateTime, String newStatus) {
        this.id = id;
        this.orderId = orderId;
        this.statusUpdateTime = statusUpdateTime;
        this.newStatus = newStatus;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getStatusUpdateTime() {
        return statusUpdateTime;
    }

    public void setStatusUpdateTime(Timestamp statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    /**
     * Returns a string representation of the OrderStatusUpdate object.
     *
     * @return A string containing the status update details.
     */
    @Override
    public String toString() {
        return "OrderStatusUpdate{"
                + "id=" + id
                + ", orderId=" + orderId
                + ", statusUpdateTime=" + statusUpdateTime
                + ", newStatus='" + newStatus + '\''
                + '}';
    }
}
