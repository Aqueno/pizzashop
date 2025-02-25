-- Create and use the pizza_shop database
CREATE DATABASE IF NOT EXISTS pizza_shop;
USE pizza_shop;

-- Disable Foreign Key Checks Temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Customers Table
CREATE TABLE customers (
    customer_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    email VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (customer_id),
    UNIQUE KEY phone (phone),
    UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Orders Table
CREATE TABLE orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    customer_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Pending', 'Preparing', 'Out for Delivery', 'Delivered') DEFAULT 'Pending',
    special_instructions TEXT,
    order_total DECIMAL(10,2) DEFAULT '0.00',
    PRIMARY KEY (order_id),
    CONSTRAINT orders_customer_fk FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Pizzas Table (Moved above order_items to avoid FK errors)
CREATE TABLE pizzas (
    pizza_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(5,2) NOT NULL,
    image_url VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (pizza_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Order Items Table
CREATE TABLE order_items (
    order_item_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    pizza_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_item_id),
    CONSTRAINT order_items_order_fk FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT order_items_pizza_fk FOREIGN KEY (pizza_id) REFERENCES pizzas(pizza_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Order Status Updates Table
CREATE TABLE order_status_updates (
    id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    status_update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    new_status ENUM('Pending','Preparing','Out for Delivery','Delivered') NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT order_status_updates_fk FOREIGN KEY (order_id) REFERENCES orders(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payments Table
CREATE TABLE payments (
    payment_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10,2) NOT NULL,
    method ENUM('Cash','Card','Online') NOT NULL,
    PRIMARY KEY (payment_id),
    CONSTRAINT payments_fk FOREIGN KEY (order_id) REFERENCES orders(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert Sample Customers
INSERT INTO customers (name, phone, address, email) VALUES
('Test Customer', '1234567890', '123 Test St', 'testcustomer@email.com'),
('John Doe', '9876543210', '456 Pizza St', NULL),
('Alice Brown', '5678901234', '789 Cheese Ave', NULL),
('Michael Smith', '3456789012', '101 Tomato Ln', NULL);

-- Insert Sample Pizzas (Moved above order_items to prevent FK error)
INSERT INTO pizzas (name, description, price, image_url) VALUES
('Margherita', 'Classic cheese and tomato sauce', 10.99, NULL),
('Pepperoni', 'Cheese, tomato sauce, pepperoni', 12.99, NULL),
('BBQ Chicken', 'BBQ sauce, grilled chicken, cheese', 14.99, NULL),
('Hawaiian', 'Tomato sauce, cheese, ham, pineapple', 12.99, NULL),
('Veggie Delight', 'Tomato sauce, cheese, bell peppers, onions, mushrooms, olives', 11.99, NULL),
('Meat Lovers', 'Tomato sauce, cheese, pepperoni, sausage, bacon, ham, beef', 14.99, NULL),
('Four Cheese', 'Tomato sauce, mozzarella, cheddar, parmesan, blue cheese', 13.49, NULL),
('Buffalo Chicken', 'Buffalo sauce, grilled chicken, mozzarella, ranch drizzle', 13.99, NULL),
('BBQ Bacon Cheeseburger', 'BBQ sauce, beef, bacon, cheddar cheese, pickles, onions', 14.49, NULL),
('Pesto Chicken', 'Pesto sauce, grilled chicken, mozzarella, tomatoes, basil', 13.79, NULL);

-- Insert Sample Orders
INSERT INTO orders (customer_id, order_date, status, special_instructions, order_total) VALUES
(1, NOW(), 'Pending', 'Gluten-free crust, add extra mushrooms', 21.98),
(1, NOW(), 'Pending', 'No onions, extra cheese', 12.99),
(2, NOW(), 'Pending', NULL, 29.98),
(3, NOW(), 'Preparing', NULL, NULL);

-- Insert Sample Order Items (Now has valid pizza_id values)
INSERT INTO order_items (order_id, pizza_id, quantity) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 3, 2);

-- Insert Sample Order Status Updates
INSERT INTO order_status_updates (order_id, status_update_time, new_status) VALUES
(1, NOW(), 'Preparing'),
(2, NOW(), 'Preparing'),
(3, NOW(), 'Preparing'),
(4, NOW(), 'Preparing');

-- Insert Sample Payments
INSERT INTO payments (order_id, payment_date, amount, method) VALUES
(1, NOW(), 21.98, 'Card'),
(2, NOW(), 12.99, 'Cash');

-- Trigger to update order status automatically after insertion
DELIMITER ;;
CREATE TRIGGER after_order_insert
AFTER INSERT ON orders FOR EACH ROW
BEGIN
    INSERT INTO order_status_updates (order_id, status_update_time, new_status)
    VALUES (NEW.order_id, NOW(), 'Preparing');
END ;;
DELIMITER ;

-- Stored Procedure: Get Order Details
DELIMITER ;;
CREATE PROCEDURE GetOrderDetails(IN orderID INT)
BEGIN
    SELECT o.order_id, c.name AS customer_name, c.phone, c.address, 
           o.order_date, o.status, p.name AS pizza_name, oi.quantity
    FROM orders o
    JOIN customers c ON o.customer_id = c.customer_id
    JOIN order_items oi ON o.order_id = oi.order_id
    JOIN pizzas p ON oi.pizza_id = p.pizza_id
    WHERE o.order_id = orderID;
END ;;
DELIMITER ;

-- Stored Procedure: Update Order Total
DELIMITER ;;
CREATE PROCEDURE UpdateOrderTotal()
BEGIN
    UPDATE orders o
    SET order_total = (
        SELECT COALESCE(SUM(p.price * oi.quantity), 0)
        FROM order_items oi
        JOIN pizzas p ON oi.pizza_id = p.pizza_id
        WHERE oi.order_id = o.order_id
    );
END ;;
DELIMITER ;

-- Re-enable Foreign Key Checks
SET FOREIGN_KEY_CHECKS = 1;
