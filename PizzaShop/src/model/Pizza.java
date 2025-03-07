package model;

import java.math.BigDecimal;

/**
 * Represents a pizza item in the system with various size options and prices.
 */
public class Pizza {

    private int pizzaId;
    private String name;
    private String description;
    private BigDecimal smallPrice;
    private BigDecimal mediumPrice;
    private BigDecimal largePrice;
    private BigDecimal extraLargePrice;

    // Default constructor
    public Pizza() {
    }

    public Pizza(int pizzaId, String name, String description,
            BigDecimal smallPrice, BigDecimal mediumPrice,
            BigDecimal largePrice, BigDecimal extraLargePrice) {
        this.pizzaId = pizzaId;
        this.name = name;
        this.description = description;
        this.smallPrice = smallPrice;
        this.mediumPrice = mediumPrice;
        this.largePrice = largePrice;
        this.extraLargePrice = extraLargePrice;
    }

    // Getters and setters
    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSmallPrice() {
        return smallPrice;
    }

    public void setSmallPrice(BigDecimal smallPrice) {
        this.smallPrice = smallPrice;
    }

    public BigDecimal getMediumPrice() {
        return mediumPrice;
    }

    public void setMediumPrice(BigDecimal mediumPrice) {
        this.mediumPrice = mediumPrice;
    }

    public BigDecimal getLargePrice() {
        return largePrice;
    }

    public void setLargePrice(BigDecimal largePrice) {
        this.largePrice = largePrice;
    }

    public BigDecimal getExtraLargePrice() {
        return extraLargePrice;
    }

    public void setExtraLargePrice(BigDecimal extraLargePrice) {
        this.extraLargePrice = extraLargePrice;
    }

    /**
     * Returns a string representation of the Pizza object.
     *
     * @return A string containing the pizza details.
     */
    @Override
    public String toString() {
        return "Pizza{"
                + "pizzaId=" + pizzaId
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", smallPrice=" + smallPrice
                + ", mediumPrice=" + mediumPrice
                + ", largePrice=" + largePrice
                + ", extraLargePrice=" + extraLargePrice
                + '}';
    }
}
