package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder;
import model.Customer;
import model.OrderItem;

import model.Pizza;
import service.PizzaService;
import service.OrderService;

class PizzaOrderingApp {

    private static final PizzaService pizzaService = new PizzaService();
    private static final OrderService orderService = new OrderService();
    private static BigDecimal totalPrice = BigDecimal.ZERO;

    private static final Color SECONDARY_COLOR = new Color(80, 40, 20); // Warm brown
    private static final Color ACCENT_COLOR = new Color(255, 200, 50); // Olive green for contrast

    //Stores all the pizza
    private static List<Pizza> menu;
    // Store pizzas in a Map to optimize lookups by name
    private static Map<String, Pizza> pizzaMap;

    public static void main(String[] args) throws SQLException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        menu = pizzaService.getFullMenu();
        convertPizzaListToMap(menu);

        JFrame frame = new JFrame("Pizza Deluxe Ordering System");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BackgroundPanel backgroundPanel = new BackgroundPanel("/pizzashop.jpeg");
        frame.setContentPane(backgroundPanel);

        // Main content panel with semi-transparent overlay
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        backgroundPanel.add(contentPanel);

        // Create header panel with logo and title
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel (for tabs)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBackground(new Color(175, 35, 35));
        tabbedPane.setOpaque(true);
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? new Color(145, 20, 20) : new Color(200, 60, 60)); // Darker red for selected, lighter red for unselected
                g.fillRect(x, y, w, h);
            }
        });
        tabbedPane.addTab("Menu", createMenuPanel());
        tabbedPane.addTab("Place Order", createOrderPanel());
        tabbedPane.addTab("Track Order", createTrackingPanel());
        centerPanel.add(tabbedPane);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer with copyright
        JPanel footerPanel = createFooterPanel();
        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.add(contentPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Title and tagline with enhanced styling
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Pizza Deluxe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 42)); // More decorative font
        titleLabel.setForeground(Color.WHITE);

        JLabel taglineLabel = new JLabel("Taste the Excellence", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Georgia", Font.ITALIC, 20));
        taglineLabel.setForeground(ACCENT_COLOR);

        titlePanel.add(titleLabel);
        titlePanel.add(taglineLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Create a stylish semi-transparent footer with warmer tone
        JPanel transparentFooter = new JPanel();
        transparentFooter.setBackground(new Color(60, 30, 10, 180));
        transparentFooter.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel copyrightLabel = new JLabel("© 2025 Pizza Deluxe. All rights reserved.");
        copyrightLabel.setFont(new Font("Georgia", Font.ITALIC, 12));
        copyrightLabel.setForeground(new Color(255, 250, 240));
        transparentFooter.add(copyrightLabel);

        footerPanel.add(transparentFooter);
        return footerPanel;
    }

    private static JPanel createMenuPanel() throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBackground(new Color(255, 250, 240, 200));

        String[] columnNames = {"Pizza Type", "Description", "Small", "Medium", "Large", "Extra Large"};
        Object[][] pizzaData = convertToPizzaData(menu);

        DefaultTableModel model = new DefaultTableModel(pizzaData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable menuTable = new JTable(model);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuTable.setGridColor(new Color(200, 180, 160)); // Warmer grid color
        menuTable.setBackground(new Color(255, 250, 240, 200));
        menuTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private static void resetFields(JComboBox<String> pizzaList, JComboBox<String> sizeList,
            JSpinner quantitySpinner, JTextField instructionsField,
            DefaultListModel<String> cartModel, JLabel totalLabel) {
        pizzaList.setSelectedIndex(0);
        sizeList.setSelectedIndex(0);
        quantitySpinner.setValue(1);
        instructionsField.setText("");
        cartModel.clear();
        totalPrice = BigDecimal.ZERO;
        totalLabel.setText("Total Price: $0.00");
    }

    private static JPanel createOrderPanel() throws SQLException {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 10));
        panel.setOpaque(false);
        panel.setBackground(new Color(255, 250, 240, 200));

        Font orderFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel pizzaLabel = new JLabel("Select Pizza:");
        pizzaLabel.setForeground(Color.WHITE);
        pizzaLabel.setFont(orderFont);
        List<String> pizzaNames = pizzaService.getPizzaNames();
        JComboBox<String> pizzaList = new JComboBox<>(pizzaNames.toArray(new String[0]));
        pizzaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pizzaList.setFont(orderFont);

        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setForeground(Color.WHITE);
        sizeLabel.setFont(orderFont);
        sizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JComboBox<String> sizeList = new JComboBox<>(new String[]{"Small", "Medium", "Large", "Extra Large"});
        sizeList.setFont(orderFont);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setForeground(Color.WHITE);
        quantityLabel.setFont(orderFont);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantitySpinner.setFont(orderFont);

        JButton addButton = new JButton("Add to Cart");
        addButton.setPreferredSize(new Dimension(100, 20));
        addButton.setBackground(new Color(200, 60, 60)); // Same as unselected tab color
        addButton.setForeground(SECONDARY_COLOR);
        addButton.setFont(orderFont);

        DefaultListModel<String> cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        cartList.setFont(orderFont);

        JLabel totalLabel = new JLabel("Total Price: $0.00");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(orderFont);

        List<OrderItem> items = new ArrayList<OrderItem>();
        addButton.addActionListener(e -> {
            String pizzaName = (String) pizzaList.getSelectedItem();
            Pizza pizza = pizzaMap.get(pizzaName.toLowerCase());
            String size = (String) sizeList.getSelectedItem();
            int quantity = (int) quantitySpinner.getValue();
            BigDecimal price = getPizzaPrice(pizza, size).multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(price);
            totalLabel.setText("Total Price: $" + totalPrice);
            cartModel.addElement(pizzaName + " - " + size + " x" + quantity + " ($" + price + ")");

            OrderItem item = new OrderItem();
            item.setPizzaId(pizza.getPizzaId());
            item.setQuantity(quantity);
            item.setSize(size);
            items.add(item);
        });

        JLabel instructionsLabel = new JLabel("Special Instructions:");
        instructionsLabel.setFont(orderFont);
        instructionsLabel.setForeground(Color.WHITE);
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField instructionsField = new JTextField();
        instructionsField.setFont(orderFont);
        instructionsField.setPreferredSize(new Dimension(200, 30));

        JButton orderButton = new JButton("Place Order");
        orderButton.setPreferredSize(new Dimension(100, 20));
        orderButton.setBackground(new Color(200, 60, 60));
        orderButton.setForeground(SECONDARY_COLOR);
        orderButton.setFont(orderFont);
        orderButton.addActionListener(e -> {
            if (totalPrice.equals(BigDecimal.ZERO)) {
                JOptionPane.showMessageDialog(null, "Please add pizza to cart to place order", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField addressField = new JTextField();

            Object[] fields = {
                "Name:", nameField,
                "Phone:", phoneField,
                "Email:", emailField,
                "Address:", addressField
            };

            int result = JOptionPane.showConfirmDialog(null, fields, "Enter Customer Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION && !nameField.getText().isEmpty() && !phoneField.getText().isEmpty() && !addressField.getText().isEmpty() && !emailField.getText().isEmpty()) {
                try {
                    Customer customer = new Customer();
                    customer.setName(nameField.getText());
                    customer.setAddress(addressField.getText());
                    customer.setPhone(phoneField.getText());
                    customer.setEmail(emailField.getText());

                    int orderId = orderService.placeOrder(customer, items.toArray(new OrderItem[0]), instructionsField.getText(), totalPrice);
                    JOptionPane.showMessageDialog(null, "Order Placed! Your Order ID: " + orderId, "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    resetFields(pizzaList, sizeList, quantitySpinner, instructionsField, cartModel, totalLabel);
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(120, 30));
        resetButton.setBackground(new Color(200, 60, 60));
        resetButton.setForeground(SECONDARY_COLOR);
        resetButton.setFont(orderFont);

        // ActionListener to reset all fields when clicked
        resetButton.addActionListener(e -> resetFields(pizzaList, sizeList, quantitySpinner, instructionsField, cartModel, totalLabel));

        panel.add(pizzaLabel);
        panel.add(pizzaList);
        panel.add(sizeLabel);
        panel.add(sizeList);
        panel.add(quantityLabel);
        panel.add(quantitySpinner);
        panel.add(totalLabel);
        panel.add(new JScrollPane(cartList));
        panel.add(instructionsLabel);
        panel.add(instructionsField);
        panel.add(addButton);
        panel.add(orderButton);
        panel.add(resetButton);
        return panel;
    }

    private static JPanel createTrackingPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 10)); // Adjusted for better spacing
        panel.setOpaque(false);
        panel.setBackground(new Color(255, 250, 240, 200));

        Font trackingFont = new Font("Segoe UI", Font.PLAIN, 16);
        // Order ID Input
        JLabel orderIdLabel = new JLabel("Enter Order ID:");
        orderIdLabel.setFont(trackingFont);
        orderIdLabel.setForeground(Color.WHITE);
        orderIdLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField orderIdField = new JTextField(10);
        orderIdField.setFont(trackingFont);

        // Track Order Button
        JButton trackButton = new JButton("Track Order");
        trackButton.setPreferredSize(new Dimension(120, 30));
        trackButton.setBackground(new Color(200, 60, 60));
        trackButton.setForeground(SECONDARY_COLOR);
        trackButton.setFont(trackingFont);

        JLabel statusLabel = new JLabel("Order Status: Unknown");
        statusLabel.setFont(trackingFont);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        trackButton.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            if (!orderId.isEmpty()) {
                try {
                    HashMap<String, String> map = orderService.getOrderDetails(Integer.parseInt(orderId));
                    if (map == null) {
                        statusLabel.setText("No order found with ID: " + orderId);
                        statusLabel.setForeground(Color.YELLOW);
                    } else {
                        statusLabel.setText("Status: " + map.get("order_status"));
                        statusLabel.setForeground(new Color(0, 120, 0));
                    }
                } catch (NumberFormatException ne) {
                    statusLabel.setText("Invalid Order Id: Enter numeric Order Id");
                    statusLabel.setForeground(Color.YELLOW);
                    statusLabel.setBackground(Color.WHITE);
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Missing Order ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Customer Info Button
        JButton customerInfoButton = new JButton("Customer Info");
        customerInfoButton.setPreferredSize(new Dimension(120, 30));
        customerInfoButton.setBackground(new Color(200, 60, 60));
        customerInfoButton.setForeground(SECONDARY_COLOR);
        customerInfoButton.setFont(trackingFont);

        customerInfoButton.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            if (!orderId.isEmpty()) {
                try {
                    HashMap<String, String> map = orderService.getOrderDetails(Integer.parseInt(orderId));
                    if (map == null) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid Order ID", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Customer Name: " + map.get("customer_name") + "\nPhone: " + map.get("phone") + "\nAddress: " + map.get("address") + "\nEmail: " + map.get("email"),
                                "Customer Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid NUMERIC Order ID", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Missing Order ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Order Details Button
        JButton orderDetailsButton = new JButton("Order Details");
        orderDetailsButton.setPreferredSize(new Dimension(120, 30));
        orderDetailsButton.setBackground(new Color(200, 60, 60));
        orderDetailsButton.setForeground(SECONDARY_COLOR);
        orderDetailsButton.setFont(trackingFont);

        orderDetailsButton.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            if (!orderId.isEmpty()) {
                try {
                    HashMap<String, String> map = orderService.getOrderDetails(Integer.parseInt(orderId));
                    if (map == null) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid Order ID", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Order ID: " + map.get("order_id") + "\nItems: " + map.get("ordered_items") + "\nSpecial Instructions: " + map.get("special_instructions") + "\nTotal: $" + map.get("order_total"),
                                "Order Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid NUMERIC Order ID", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Missing Order ID", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        panel.add(orderIdLabel);
        panel.add(orderIdField);
        panel.add(trackButton);
        panel.add(statusLabel);
        panel.add(customerInfoButton);
        panel.add(orderDetailsButton);
        return panel;
    }

    private static Object[][] convertToPizzaData(List<Pizza> pizzas) {
        Object[][] pizzaData = new Object[pizzas.size()][6];
        for (int i = 0; i < pizzas.size(); i++) {
            Pizza pizza = pizzas.get(i);
            pizzaData[i][0] = pizza.getName();
            pizzaData[i][1] = pizza.getDescription();
            pizzaData[i][2] = pizza.getSmallPrice();
            pizzaData[i][3] = pizza.getMediumPrice();
            pizzaData[i][4] = pizza.getLargePrice();
            pizzaData[i][5] = pizza.getExtraLargePrice();
        }
        return pizzaData;
    }

    // Method to create pizzaMap from a List<Pizza>
    private static void convertPizzaListToMap(List<Pizza> pizzas) {
        pizzaMap = new HashMap<>();
        for (Pizza pizza : pizzas) {
            // Convert to lowercase for case-insensitive lookup
            pizzaMap.put(pizza.getName().toLowerCase(), pizza);
        }
    }

    // Method to get the price based on pizza name and size
    public static BigDecimal getPizzaPrice(Pizza pizza, String size) {
        // Return the price based on size
        switch (size.toLowerCase()) {
            case "small":
                return pizza.getSmallPrice();
            case "medium":
                return pizza.getMediumPrice();
            case "large":
                return pizza.getLargePrice();
            case "extra large":
                return pizza.getExtraLargePrice();
            default:
                throw new IllegalArgumentException("Invalid size: " + size);
        }
    }

    static class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            setLayout(new BorderLayout());

            try {
                // Load the image - first try as resource, then as file
                ImageIcon icon = new ImageIcon(PizzaOrderingApp.class.getResource(imagePath));
                if (icon.getIconWidth() == -1) {
                    // Try as file path
                    icon = new ImageIcon(imagePath);
                }
                backgroundImage = icon.getImage();

                if (backgroundImage.getWidth(null) == -1) {
                    throw new Exception("Could not load background image");
                }
            } catch (Exception e) {
                System.out.println("Error loading background image: " + e.getMessage());
                // Create a fallback gradient background
                backgroundImage = null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
