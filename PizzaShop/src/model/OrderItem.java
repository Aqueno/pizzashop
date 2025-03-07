package model;

/**
 * Represents an item in an order, which includes details about the specific
 * pizza, its quantity, and its size in the order.
 */
public class OrderItem {

    private int orderItemId;
    private int orderId;
    private int pizzaId;
    private int quantity;
    private String size;

    // Default constructor
    public OrderItem() {
    }

    // Constructor with parameters
    public OrderItem(int orderItemId, int orderId, int pizzaId, int quantity) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.pizzaId = pizzaId;
        this.quantity = quantity;
        this.size = size;
    }

    // Getters and setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Returns a string representation of the OrderItem object.
     *
     * @return A string containing the order item details.
     */
    @Override
    public String toString() {
        return "OrderItem{"
                + "orderItemId=" + orderItemId
                + ", orderId=" + orderId
                + ", pizzaId=" + pizzaId
                + ", size=" + size
                + ", quantity=" + quantity
                + '}';
    }
}
