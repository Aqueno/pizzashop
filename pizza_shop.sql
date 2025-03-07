CREATE DATABASE pizza_shop;
USE pizza_shop;

-- Table: customers
-- This table stores customer details, including their name, phone number, address, and email.
-- The `phone` column is marked as UNIQUE to ensure no duplicate phone numbers exist.
-- The `email` column is also marked as UNIQUE, allowing each customer to have a unique email.
-- The `customer_id` is the primary key and is auto-incremented for uniqueness.

CREATE TABLE `customers` (
  `customer_id` INT NOT NULL AUTO_INCREMENT,  
  `name` VARCHAR(100) NOT NULL,               
  `phone` VARCHAR(20) NOT NULL,               
  `address` TEXT NOT NULL,                    
  `email` VARCHAR(100) DEFAULT NULL,          

  PRIMARY KEY (`customer_id`),                
  UNIQUE KEY `phone` (`phone`),               
  UNIQUE KEY `email` (`email`)                
);

INSERT INTO customers (customer_id, name, phone, address, email) VALUES
(1, 'Test Customer', '1234567890', '123 Test St', 'testcustomer@email.com'),
(2, 'John Doe', '9876543210', '456 Pizza St', 'john.doe@email.com'),
(3, 'Alice Brown', '5678901234', '789 Cheese Ave', 'alice.brown@email.com'),
(4, 'Michael Smith', '3456789012', '101 Tomato Ln', 'michael.smith@email.com'),
(5, 'Emma Wilson', '6543217890', '222 Maple St', 'emma.wilson@email.com'),
(6, 'Liam Johnson', '7654321890', '333 Oak St', 'liam.johnson@email.com'),
(7, 'Sophia Martinez', '8765432109', '444 Pine St', 'sophia.m@email.com'),
(8, 'Noah Lee', '9876543012', '555 Birch St', 'noah.lee@email.com'),
(9, 'Olivia Taylor', '1122334455', '666 Walnut St', 'olivia.t@email.com'),
(10, 'James Anderson', '2233445566', '777 Cedar St', 'james.a@email.com');

-- Table: order_items
-- This table represents the items in each order.
-- It links orders to pizzas, allowing multiple pizzas per order.
-- Each row corresponds to one pizza in an order, storing its size and quantity.
-- This helps maintain a many-to-many relationship between orders and pizzas.
CREATE TABLE `order_items` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `pizza_id` int NOT NULL,
  `size` enum('Small','Medium','Large','Extra Large') NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`order_item_id`),
   CONSTRAINT order_items_ibfk_1 FOREIGN KEY (order_id) 
    REFERENCES orders (order_id) 
    ON DELETE CASCADE,
  CONSTRAINT order_items_ibfk_2 FOREIGN KEY (pizza_id) 
    REFERENCES pizzas (pizza_id) 
    ON DELETE CASCADE
);

INSERT INTO order_items (order_item_id, order_id, pizza_id, size, quantity) VALUES
(1, 1, 1, 'Small', 2),
(2, 2, 2, 'Small', 1),
(3, 3, 3, 'Small', 2),
(4, 1, 1, 'Medium', 2),
(5, 5, 2, 'Large', 1),
(6, 6, 3, 'Small', 3),
(7, 7, 1, 'Extra Large', 1),
(8, 2, 2, 'Medium', 2),
(9, 3, 3, 'Large', 1),
(10, 1, 1, 'Small', 2);



-- Table: order_status_updates
-- This table keeps a history of status changes for each order.
-- It records when an order's status changes and stores the timestamp.
-- This helps track the progress of orders from placement to delivery.

CREATE TABLE `order_status_updates` (
  `id` INT NOT NULL AUTO_INCREMENT,  
  `order_id` INT NOT NULL,           
  `status_update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,  
  `new_status` ENUM('Pending','Preparing','Out for Delivery','Delivered') NOT NULL,  

  PRIMARY KEY (`id`),                
  CONSTRAINT order_status_updates_fk FOREIGN KEY (order_id) 
    REFERENCES orders (order_id) 
    ON DELETE CASCADE  
);

INSERT INTO `order_status_updates` VALUES (1,1,'2025-02-24 23:09:24','Preparing'),(2,2,'2025-02-25 19:25:09','Preparing'),(3,3,'2025-02-25 19:28:44','Preparing'),(4,4,'2025-02-25 19:28:44','Preparing'),(5,5,'2025-03-05 05:51:11','Preparing'),(6,1,'2025-03-05 05:51:36','Preparing'),(7,1,'2025-03-05 05:51:36','Preparing'),(8,1,'2025-03-05 05:51:57','Out for Delivery'),(9,1,'2025-03-05 05:51:57','Out for Delivery'),(10,1,'2025-03-05 05:52:16','Delivered'),(11,1,'2025-03-05 05:52:16','Delivered'),(12,1,'2025-03-05 06:03:47','Preparing'),(13,1,'2025-03-05 06:03:47','Out for Delivery'),(14,1,'2025-03-05 06:03:47','Delivered'),(15,2,'2025-03-05 06:13:23','Preparing'),(16,2,'2025-03-05 06:13:23','Preparing'),(17,2,'2025-03-05 06:13:23','Out for Delivery'),(18,2,'2025-03-05 06:13:23','Out for Delivery'),(19,2,'2025-03-05 06:13:23','Delivered'),(20,2,'2025-03-05 06:13:23','Delivered'),(21,3,'2025-03-05 18:54:54','Preparing'),(22,5,'2025-03-05 18:54:54','Preparing'),(23,3,'2025-03-05 18:54:54','Out for Delivery'),(24,3,'2025-03-05 18:54:54','Out for Delivery'),(25,4,'2025-03-05 18:54:54','Out for Delivery'),(26,4,'2025-03-05 18:54:54','Out for Delivery'),(27,5,'2025-03-05 18:54:54','Out for Delivery'),(28,5,'2025-03-05 18:54:54','Out for Delivery'),(29,3,'2025-03-05 18:54:54','Delivered'),(30,3,'2025-03-05 18:54:54','Delivered'),(31,4,'2025-03-05 18:54:54','Delivered'),(32,4,'2025-03-05 18:54:54','Delivered'),(33,5,'2025-03-05 18:54:54','Delivered'),(34,5,'2025-03-05 18:54:54','Delivered'),(35,6,'2025-03-05 19:26:50','Preparing'),(36,6,'2025-03-05 19:40:24','Preparing'),(37,6,'2025-03-05 19:44:02','Out for Delivery'),(38,6,'2025-03-05 19:44:02','Out for Delivery'),(39,6,'2025-03-05 19:46:59','Delivered'),(40,6,'2025-03-05 19:46:59','Delivered'),(41,7,'2025-03-05 19:56:56','Preparing'),(42,7,'2025-03-05 23:25:56','Preparing'),(43,7,'2025-03-05 23:26:06','Out for Delivery'),(44,7,'2025-03-05 23:26:06','Out for Delivery'),(45,7,'2025-03-05 23:26:16','Delivered'),(46,7,'2025-03-05 23:26:16','Delivered'),(47,8,'2025-03-05 23:29:15','Preparing'),(48,9,'2025-03-05 23:29:15','Preparing'),(49,10,'2025-03-05 23:29:15','Preparing'),(50,8,'2025-03-05 23:29:16','Preparing'),(51,8,'2025-03-05 23:29:26','Out for Delivery'),(52,8,'2025-03-05 23:29:26','Out for Delivery'),(53,9,'2025-03-05 23:29:26','Out for Delivery'),(54,9,'2025-03-05 23:29:26','Out for Delivery'),(55,10,'2025-03-05 23:29:26','Delivered'),(56,10,'2025-03-05 23:29:26','Delivered'),(57,8,'2025-03-05 23:29:36','Delivered'),(58,8,'2025-03-05 23:29:36','Delivered'),(59,9,'2025-03-05 23:29:36','Delivered'),(60,9,'2025-03-05 23:29:36','Delivered'),(61,11,'2025-03-05 23:32:20','Preparing'),(62,11,'2025-03-05 23:32:26','Preparing'),(63,11,'2025-03-05 23:32:36','Out for Delivery'),(64,11,'2025-03-05 23:32:36','Out for Delivery'),(65,11,'2025-03-05 23:32:46','Delivered'),(66,11,'2025-03-05 23:32:46','Delivered');


-- Table: orders
-- This table stores order details, including customer ID, order date, status, and total amount.
-- It also tracks special instructions and the last status update.

CREATE TABLE `orders` (
  `order_id` INT NOT NULL AUTO_INCREMENT,  
  `customer_id` INT NOT NULL,              
  `order_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,  
  `status` ENUM('Pending','Preparing','Out for Delivery','Delivered') DEFAULT 'Pending',  
  `special_instructions` TEXT,             
  `order_total` DECIMAL(10,2) DEFAULT '0.00',  
  `last_status_update` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  

  PRIMARY KEY (`order_id`),                
  CONSTRAINT orders_customer_fk FOREIGN KEY (customer_id) 
    REFERENCES customers (customer_id) 
    ON DELETE CASCADE  
);

INSERT INTO orders (customer_id, order_date, status, special_instructions, order_total, last_status_update) 
VALUES 
(1, NOW(), 'Delivered', 'Gluten-free crust, add extra mushrooms', 21.98, NOW()),
(1, NOW(), 'Delivered', 'No onions, extra cheese', 12.99, NOW()),
(2, NOW(), 'Delivered', NULL, 0.00, NOW()),
(3, NOW(), 'Delivered', NULL, 15.99, NOW()),
(1, NOW(), 'Delivered', 'Extra cheese', 15.99, NOW()),
(1, NOW(), 'Delivered', NULL, 15.99, NOW()),
(1, NOW(), 'Delivered', NULL, 15.99, NOW()),
(2, NOW(), 'Delivered', NULL, 0.00, NOW()),
(3, NOW(), 'Delivered', NULL, 0.00, NOW()),
(1, NOW(), 'Delivered', 'Extra sauce on all pizzas', 20.00, NOW());

    
-- Table: pizzas
-- This table stores different pizza options available in the menu.
-- It includes pizza name, description, and pricing for various sizes.

CREATE TABLE `pizzas` (
  `pizza_id` INT NOT NULL AUTO_INCREMENT,  
  `name` VARCHAR(100) NOT NULL,            
  `description` TEXT,                       
  `small_price` DECIMAL(5,2) NOT NULL DEFAULT '0.00',   
  `medium_price` DECIMAL(5,2) NOT NULL DEFAULT '0.00',  
  `large_price` DECIMAL(5,2) NOT NULL DEFAULT '0.00',   
  `extra_large_price` DECIMAL(5,2) NOT NULL DEFAULT '0.00',  

  PRIMARY KEY (`pizza_id`)                 
);

INSERT INTO pizzas (pizza_id, name, description, small_price, medium_price, large_price, extra_large_price) VALUES
(1, 'Margherita', 'Classic cheese and tomato sauce', 10.00, 12.00, 14.00, 16.00),
(2, 'Pepperoni', 'Cheese, tomato sauce, pepperoni', 11.00, 13.00, 15.00, 17.00),
(3, 'BBQ Chicken', 'BBQ sauce, grilled chicken, cheese', 12.00, 14.00, 16.00, 18.00),
(5, 'Veggie Delight', 'Tomato sauce, cheese, bell peppers, onions, mushrooms, olives', 9.00, 11.00, 13.00, 15.00),
(6, 'Meat Lovers', 'Tomato sauce, cheese, pepperoni, sausage, bacon, ham, beef', 13.00, 15.00, 17.00, 19.00),
(24, 'Hawaiian', 'Tomato sauce, cheese, ham, pineapple', 11.00, 13.00, 15.00, 17.00),
(25, 'Buffalo Chicken', 'Buffalo sauce, grilled chicken, cheese', 12.00, 14.00, 16.00, 18.00),
(26, 'Mushroom Delight', 'Tomato sauce, cheese, mushrooms, garlic', 10.00, 12.00, 14.00, 16.00),
(27, 'Supreme', 'Tomato sauce, cheese, pepperoni, sausage, onions, mushrooms, bell peppers', 13.00, 15.00, 17.00, 19.00),
(28, 'White Pizza', 'Olive oil, garlic, ricotta, mozzarella, parmesan', 10.00, 12.00, 14.00, 16.00);


-- TRIGGER: after_order_insert
CREATE TRIGGER after_order_insert
AFTER INSERT ON orders
FOR EACH ROW
BEGIN
    INSERT INTO order_status_updates (order_id, status_update_time, new_status)
    INSERT INTO order_status_updates (order_id, status_update_time, new_status)
    VALUES (NEW.order_id, NOW(), 'Preparing');
END ;;
DELIMITER ;
DELIMITER ;;

-- TRIGGER: after_status_update
CREATE TRIGGER after_status_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.status <> NEW.status THEN
        INSERT INTO order_status_updates (order_id, status_update_time, new_status)
        INSERT INTO order_status_updates (order_id, status_update_time, new_status)
        VALUES (NEW.order_id, NOW(), NEW.status);
    END IF;
END ;;
DELIMITER ;
DELIMITER ;;
-- EVENT: auto_update_order_status
DELIMITER ;;
CREATE EVENT auto_update_order_status
ON SCHEDULE EVERY 30 SECOND
DO
BEGIN
    -- Update Pending to Preparing
    UPDATE orders
    SET status = 'Preparing', last_status_update = NOW()
    WHERE status = 'Pending'
    AND TIMESTAMPDIFF(SECOND, order_date, NOW()) >= 30;

    -- Update Preparing to Out for Delivery
    UPDATE orders
    SET status = 'Out for Delivery', last_status_update = NOW()
    WHERE status = 'Preparing'
    AND TIMESTAMPDIFF(SECOND, last_status_update, NOW()) >= 30;

    -- Update Out for Delivery to Delivered
    UPDATE orders
    SET status = 'Delivered', last_status_update = NOW()
    WHERE status = 'Out for Delivery'
    AND TIMESTAMPDIFF(SECOND, last_status_update, NOW()) >= 30;
END;;
DELIMITER ;


--  NEW WORKING STORED PROCEDURE FOR GetOrderDetails
DELIMITER ;;
CREATE PROCEDURE GetOrderDetails(IN orderID INT)
BEGIN
    SELECT
        c.customer_id,
        c.name AS customer_name,
        c.phone,
        c.address,
        c.email,
        o.order_id,
        o.order_date,
        o.special_instructions,
        o.status,
        o.order_total,
        GROUP_CONCAT(p.name, ' (quantity - ', oi.quantity, ', size - ', oi.size, ') ') AS ordered_items
    FROM
        orders o
    JOIN
        customers c ON o.customer_id = c.customer_id
    LEFT JOIN
        order_items oi ON o.order_id = oi.order_id
    LEFT JOIN
        pizzas p ON oi.pizza_id = p.pizza_id
    WHERE
        o.order_id = orderID
    GROUP BY
        o.order_id, c.customer_id;
END ;;
DELIMITER ;
