package model;

/**
 * Represents a customer in the system. Contains information such as customer
 * ID, name, phone, address, and email.
 */
public class Customer {

    private int customerId;
    private String name;
    private String phone;
    private String address;
    private String email;

    // Default constructor
    public Customer() {
    }

    // Constructor with parameters
    public Customer(int customerId, String name, String phone, String address, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    // Getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a string representation of the Customer object.
     *
     * @return A string containing the customer details.
     */
    @Override
    public String toString() {
        return "Customer{"
                + "customerId=" + customerId
                + ", name='" + name + '\''
                + ", phone='" + phone + '\''
                + ", address='" + address + '\''
                + ", email='" + email + '\''
                + '}';
    }
}
