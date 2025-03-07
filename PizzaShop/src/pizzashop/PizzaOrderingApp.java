package pizzashop;

import service.OrderService;
import service.PizzaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.Pizza;

public class PizzaOrderingApp {

    private static Map<String, String> orders = new HashMap<>(); // Simulated order tracking
    private static int orderCounter = 1001;

    // Modified color scheme to better match the rustic wooden background with food elements
    private static final Color PRIMARY_COLOR = new Color(175, 35, 35); // Deeper red
    private static final Color SECONDARY_COLOR = new Color(80, 40, 20); // Warm brown
    private static final Color ACCENT_COLOR = new Color(255, 200, 50); // Warm gold
    private static final Color HIGHLIGHT_COLOR = new Color(90, 140, 40); // Olive green for contrast

    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private static final PizzaService pizzaService = new PizzaService();
    private static final OrderService orderService = new OrderService();

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
        // Create and configure the main frame
        JFrame frame = new JFrame("Pizza Deluxe Ordering System");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a BackgroundPanel with the stylish image
        BackgroundPanel backgroundPanel = new BackgroundPanel("/pizza.jpg");
        frame.setContentPane(backgroundPanel);

        // Main content panel with semi-transparent overlay
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        backgroundPanel.add(contentPanel);

        // Create header panel with logo and title
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create main content panel with two sections
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setOpaque(false);

        // Left panel for ordering
        JPanel orderPanel = createOrderPanel();
        orderPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ),
                "Order Pizza", TitledBorder.LEFT, TitledBorder.TOP, HEADER_FONT, PRIMARY_COLOR));

        // Right panel for menu and tracking
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        infoPanel.setOpaque(false);

        JPanel menuPanel = createMenuPanel();
        menuPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ),
                "Menu", TitledBorder.LEFT, TitledBorder.TOP, HEADER_FONT, PRIMARY_COLOR));

        JPanel trackingPanel = createTrackingPanel(frame);
        trackingPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ),
                "Track Your Order", TitledBorder.LEFT, TitledBorder.TOP, HEADER_FONT, PRIMARY_COLOR));

        infoPanel.add(menuPanel);
        infoPanel.add(trackingPanel);

        mainPanel.add(orderPanel);
        mainPanel.add(infoPanel);
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Footer with copyright
        JPanel footerPanel = createFooterPanel();
        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Custom JPanel class that paints a background image with improved overlay
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
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                // Draw the background image scaled to fit the panel
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                // Add a semi-transparent overlay for better text readability
                // More brownish tint to complement the wooden background
                g2d.setColor(new Color(30, 15, 0, 120));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            } else {
                // Fallback gradient background with more natural food colors
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(80, 30, 10),
                        0, getHeight(), new Color(180, 40, 20)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        }
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Logo image
        try {
            ImageIcon logoIcon = new ImageIcon(PizzaOrderingApp.class.getResource("/pizza_logo.png"));
            if (logoIcon.getIconWidth() != -1) {
                Image scaledLogo = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
                headerPanel.add(logoLabel, BorderLayout.WEST);
            }
        } catch (Exception e) {
            System.out.println("Logo image not found");
        }

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

    private static JPanel createOrderPanel() throws SQLException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Create a semi-transparent background panel
        JPanel transparentPanel = new JPanel();
        transparentPanel.setLayout(new BoxLayout(transparentPanel, BoxLayout.Y_AXIS));
        transparentPanel.setBackground(new Color(255, 255, 255, 140)); // Slightly more transparent
        transparentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Pizza Selection Section
        JPanel selectionPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        selectionPanel.setOpaque(false);

        // Pizza Type
        JLabel pizzaLabel = new JLabel("Select Pizza:");
        pizzaLabel.setFont(REGULAR_FONT);
        pizzaLabel.setForeground(SECONDARY_COLOR);
        selectionPanel.add(pizzaLabel);

        String[] pizzaNames = pizzaService.getPizzaNames().toArray(new String[0]);
        JComboBox<String> pizzaList = new JComboBox<>(pizzaNames);
        pizzaList.setFont(REGULAR_FONT);
        pizzaList.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        pizzaList.setBackground(new Color(255, 250, 240)); // Warm ivory background
        selectionPanel.add(pizzaList);

        // Size Selection
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setFont(REGULAR_FONT);
        sizeLabel.setForeground(SECONDARY_COLOR);
        selectionPanel.add(sizeLabel);

        JComboBox<String> sizeList = new JComboBox<>(new String[]{"Small", "Medium", "Large", "Extra Large"});
        sizeList.setFont(REGULAR_FONT);
        sizeList.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        sizeList.setBackground(new Color(255, 250, 240));
        selectionPanel.add(sizeList);

        // Quantity
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(REGULAR_FONT);
        quantityLabel.setForeground(SECONDARY_COLOR);
        selectionPanel.add(quantityLabel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantitySpinner.setFont(REGULAR_FONT);
        quantitySpinner.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setBackground(new Color(255, 250, 240));
        selectionPanel.add(quantitySpinner);

        // Additional Notes
        JLabel notesLabel = new JLabel("Special Instructions:");
        notesLabel.setFont(REGULAR_FONT);
        notesLabel.setForeground(SECONDARY_COLOR);
        selectionPanel.add(notesLabel);

        JTextField notesField = new JTextField();
        notesField.setFont(REGULAR_FONT);
        notesField.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        notesField.setBackground(new Color(255, 250, 240));
        selectionPanel.add(notesField);

        // Customer Details Section with improved section header
        JPanel customerPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        customerPanel.setOpaque(false);
        customerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(SECONDARY_COLOR),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ),
                "Customer Information", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), SECONDARY_COLOR));

        // Name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(REGULAR_FONT);
        nameLabel.setForeground(SECONDARY_COLOR);
        customerPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setFont(REGULAR_FONT);
        nameField.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        nameField.setBackground(new Color(255, 250, 240));
        customerPanel.add(nameField);

        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(REGULAR_FONT);
        phoneLabel.setForeground(SECONDARY_COLOR);
        customerPanel.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setFont(REGULAR_FONT);
        phoneField.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        phoneField.setBackground(new Color(255, 250, 240));
        customerPanel.add(phoneField);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(REGULAR_FONT);
        addressLabel.setForeground(SECONDARY_COLOR);
        customerPanel.add(addressLabel);

        JTextField addressField = new JTextField();
        addressField.setFont(REGULAR_FONT);
        addressField.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        addressField.setBackground(new Color(255, 250, 240));
        customerPanel.add(addressField);

        // Email - New field added
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(REGULAR_FONT);
        emailLabel.setForeground(SECONDARY_COLOR);
        customerPanel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setFont(REGULAR_FONT);
        emailField.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
        emailField.setBackground(new Color(255, 250, 240));
        customerPanel.add(emailField);

        // Order Button - warmer, more appetizing style
        JButton orderButton = createStyledButton("Place Order", PRIMARY_COLOR, ACCENT_COLOR);
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all required customer information fields.",
                                "Missing Information", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String pizzaName = pizzaList.getSelectedItem().toString();
                    // Lookup pizza by name (case-insensitive)
                    Pizza pizza = pizzaMap.get(pizzaName.toLowerCase());

                    if (pizza == null) {
                        throw new IllegalArgumentException("Pizza not found: " + pizzaName);
                    }

                    String pizzaSize = (String) sizeList.getSelectedItem();
                    int pizzaQuantity = (int) quantitySpinner.getValue();
                    BigDecimal price = PizzaOrderingApp.getPizzaPrice(pizza, pizzaSize);
                    BigDecimal totalValue = price.multiply(BigDecimal.valueOf(pizzaQuantity));

                    OrderItem item = new OrderItem();
                    item.setPizzaId(pizza.getPizzaId());
                    item.setQuantity(pizzaQuantity);
                    item.setSize(pizzaSize);

                    // Here you would save the customer information to the database
                    // customer_id (AUTO_INCREMENT), name, phone, address, email
                    String customerEmail = emailField.getText().isEmpty() ? "Not provided" : emailField.getText();

                    Customer customer = new Customer();
                    customer.setName(nameField.getText());
                    customer.setAddress(addressField.getText());
                    customer.setPhone(phoneField.getText());
                    customer.setEmail(customerEmail);

                    Order order = new Order();
                    order.setSpecialInstructions(notesField.getText());
                    order.setOrderTotal(totalValue);

                    int orderId = orderService.placeOrder(customer, new OrderItem[]{item}, notesField.getText(), totalValue);
                    String orderIdString = "ORD" + orderId;
//                  String orderIdString = "ORD" + orderCounter++;

                    String orderDetails = "Pizza: " + pizzaName
                            + ", Size: " + pizzaSize
                            + ", Quantity: " + pizzaQuantity;
                    orders.put(orderIdString, "Processing");

                    JOptionPane.showMessageDialog(null,
                            "<html><h2 style='color:#A42323;'>Order Placed Successfully!</h2>"
                            + "<p>Your Order ID: <b>" + orderId + "</b></p>"
                            + "<p>Order Details: " + orderDetails + "</p>"
                            + "<p>We'll deliver to: " + addressField.getText() + "</p>"
                            + "<p>Confirmation " + (customerEmail.equals("Not provided") ? "will not be sent (no email provided)"
                            : "will be sent to: " + customerEmail) + "</p>"
                            + "<p>Estimated delivery time: 30-45 minutes</p></html>",
                            "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Reset Button - more subtle but still coordinated
        JButton resetButton = createStyledButton("Reset", SECONDARY_COLOR, Color.WHITE);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pizzaList.setSelectedIndex(0);
                sizeList.setSelectedIndex(0);
                quantitySpinner.setValue(1);
                notesField.setText("");
                nameField.setText("");
                phoneField.setText("");
                addressField.setText("");
                emailField.setText(""); // Reset email field
            }
        });

        // Button Panel with improved spacing
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(resetButton);
        buttonPanel.add(orderButton);

        // Add components to transparent panel
        transparentPanel.add(selectionPanel);
        transparentPanel.add(Box.createVerticalStrut(20));
        transparentPanel.add(customerPanel);
        transparentPanel.add(Box.createVerticalStrut(20));
        transparentPanel.add(buttonPanel);

        // Add transparent panel to main panel
        panel.add(transparentPanel);

        return panel;
    }

    private static JPanel createMenuPanel() throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Create a semi-transparent background with warmer tint
        JPanel transparentPanel = new JPanel(new BorderLayout());
        transparentPanel.setBackground(new Color(255, 250, 240, 180));
        transparentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model with more detailed menu
        String[] columnNames = {"Pizza Type", "Description", "Small", "Medium", "Large"};
        List<Pizza> menu = pizzaService.getFullMenu();
        Object[][] pizzaData = {
            {"Margherita", "Classic tomato, mozzarella, basil", "$10", "$12", "$14"},
            {"Pepperoni", "Pepperoni, cheese, tomato sauce", "$12", "$14", "$16"},
            {"Veggie", "Bell peppers, onions, mushrooms, olives", "$11", "$13", "$15"},
            {"Supreme", "Pepperoni, sausage, bell peppers, onions", "$13", "$15", "$17"},
            {"Hawaiian", "Ham, pineapple, extra cheese", "$12", "$14", "$16"},
            {"Buffalo Chicken", "Spicy chicken, blue cheese, hot sauce", "$13", "$15", "$17"}
        };

        pizzaData = convertToPizzaData(menu);

        DefaultTableModel model = new DefaultTableModel(pizzaData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and configure table with improved styling
        JTable menuTable = new JTable(model);
        menuTable.setFont(REGULAR_FONT);
        menuTable.setRowHeight(30);
        menuTable.setShowGrid(true);
        menuTable.setGridColor(new Color(200, 180, 160)); // Warmer grid color
        menuTable.setBackground(new Color(255, 250, 240, 200));
        menuTable.setSelectionBackground(new Color(255, 230, 210));
        menuTable.setOpaque(false);

        // Style the header with a more appetizing look
        JTableHeader header = menuTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 220));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        transparentPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(transparentPanel, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createTrackingPanel(JFrame parentFrame) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        // Create a semi-transparent background with warmer tint
        JPanel transparentPanel = new JPanel(new BorderLayout(0, 15));
        transparentPanel.setBackground(new Color(255, 250, 240, 180));
        transparentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tracking input panel with improved styling
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);

        JLabel orderIdLabel = new JLabel("Enter Order ID: ");
        orderIdLabel.setFont(REGULAR_FONT);
        orderIdLabel.setForeground(SECONDARY_COLOR);

        JTextField orderIdField = new JTextField();
        orderIdField.setFont(REGULAR_FONT);
        orderIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        orderIdField.setBackground(new Color(255, 250, 240));

        // Track button with coordinated styling
        JButton trackButton = createStyledButton("Track Order", HIGHLIGHT_COLOR, Color.WHITE);

        inputPanel.add(orderIdLabel, BorderLayout.WEST);
        inputPanel.add(orderIdField, BorderLayout.CENTER);
        inputPanel.add(trackButton, BorderLayout.EAST);

        // Status display panel with warmer colors
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel statusTitleLabel = new JLabel("Order Status:");
        statusTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusTitleLabel.setForeground(SECONDARY_COLOR);

        JLabel statusLabel = new JLabel("No order selected");
        statusLabel.setFont(REGULAR_FONT);
        statusLabel.setForeground(SECONDARY_COLOR);

        JPanel statusInfoPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        statusInfoPanel.setOpaque(false);
        statusInfoPanel.add(statusTitleLabel);
        statusInfoPanel.add(statusLabel);

        JLabel estimatedTimeLabel = new JLabel("");
        estimatedTimeLabel.setFont(REGULAR_FONT);
        estimatedTimeLabel.setForeground(SECONDARY_COLOR);
        statusInfoPanel.add(estimatedTimeLabel);

        JLabel deliveryInfoLabel = new JLabel("");
        deliveryInfoLabel.setFont(REGULAR_FONT);
        deliveryInfoLabel.setForeground(SECONDARY_COLOR);
        statusInfoPanel.add(deliveryInfoLabel);

        statusPanel.add(statusInfoPanel, BorderLayout.CENTER);

        //code for customer and status info
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        linksPanel.setOpaque(false);

        JLabel customerInfoLink = new JLabel("<html><u>Customer Info</u></html>");
        customerInfoLink.setFont(REGULAR_FONT);
        customerInfoLink.setForeground(HIGHLIGHT_COLOR);
        customerInfoLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerInfoLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String orderId = orderIdField.getText().trim();
                if (orderId.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter an order ID",
                            "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                try {
                    HashMap<String, String> map = orderService.getOrderDetails(Integer.parseInt(orderId));
                    if (map == null) {
                        sb.append("Invalid Order Id: Order ID not found");
                    } else {
                        sb.append("<html><h3>Customer Information</h3>"
                                + "<p><b>Order ID:</b> " + map.get("order_id") + "</p>"
                                + "<p><b>Name:</b> " + map.get("customer_name") + "</p>"
                                + "<p><b>Phone:</b> " + map.get("phone") + "</p>"
                                + "<p><b>Address:</b> " + map.get("address") + "</p>"
                                + "<p><b>Email:</b> " + map.get("email") + "</p></html>");
                    }
                } catch (NumberFormatException e) {
                    sb.append("Invalid Order Id: Enter numeric Order Id");  // It's not numeric
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Display customer information dialog
                JOptionPane.showMessageDialog(parentFrame,
                        sb.toString(),
                        "Customer Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel orderInfoLink = new JLabel("<html><u>Order Details</u></html>");
        orderInfoLink.setFont(REGULAR_FONT);
        orderInfoLink.setForeground(HIGHLIGHT_COLOR);
        orderInfoLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderInfoLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                String orderId = orderIdField.getText().trim();
                if (orderId.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter an order ID",
                            "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                try {
                    HashMap<String, String> map = orderService.getOrderDetails(Integer.parseInt(orderId));
                    if (map == null) {
                        sb.append("Invalid Order Id: Order ID not found");
                    } else {
                        sb.append("<html><h3>Order Details</h3>"
                                + "<p><b>Order ID:</b> " + map.get("order_id") + "</p>"
                                + "<p><b>Ordered Pizza:</b> " + map.get("ordered_items") + "</p>"
                                + "<p><b>Special Instructions:</b> " + map.get("special_instructions") + "</p>"
                                + "<p><b>Total:</b> $" + map.get("order_total") + "</p></html>");
                    }
                } catch (NumberFormatException e) {
                    sb.append("Invalid Order Id: Enter numeric Order Id");  // It's not numeric
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Display order details dialog
                JOptionPane.showMessageDialog(parentFrame, sb.toString(),
                        //                        "<html><h3>Order Details</h3>"
                        //                        + "<p><b>Order ID:</b> " + "order123" + "</p>"
                        //                        + "<p><b>Pizza:</b> Pepperoni</p>"
                        //                        + // hardcoded ,need to come from db
                        //                        "<p><b>Size:</b> Large</p>"
                        //                        + "<p><b>Quantity:</b> 2</p>"
                        //                        + "<p><b>Special Instructions:</b> Extra cheese</p>"
                        //                        + "<p><b>Total:</b> $32.00</p></html>",
                        "Order Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        linksPanel.add(customerInfoLink);
        linksPanel.add(orderInfoLink);

        // Add linksPanel to statusInfoPanel
        statusInfoPanel.add(linksPanel);

        // Track button action listener with better status colors
        trackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderId = orderIdField.getText().trim();
                if (orderId.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter an order ID",
                            "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    HashMap<String, String> map = orderService.getOrderDetails(Integer.parseInt(orderId));
                    if (map == null) {
                        statusLabel.setText("No order found with ID: " + orderId);
                        statusLabel.setForeground(new Color(180, 0, 0));
                        estimatedTimeLabel.setText("");
                        deliveryInfoLabel.setText("");
                    } else {
                        statusLabel.setText("Status: " + map.get("order_status"));
                        statusLabel.setForeground(new Color(0, 120, 0));
                    }
                } catch (NumberFormatException ne) {
                    statusLabel.setText("Invalid Order Id: Enter numeric Order Id");
                    statusLabel.setForeground(new Color(180, 0, 0));
                } catch (SQLException ex) {
                    Logger.getLogger(PizzaOrderingApp.class.getName()).log(Level.SEVERE, null, ex);
                }

//                String status = orders.getOrDefault(orderId, "Invalid Order ID");
//
//                if (status.equals("Invalid Order ID")) {
//                    statusLabel.setText("No order found with ID: " + orderId);
//                    statusLabel.setForeground(new Color(180, 0, 0));
//                    estimatedTimeLabel.setText("");
//                    deliveryInfoLabel.setText("");
//                } else {
//                    // Simulate different statuses
//                    if (Math.random() > 0.7) {
//                        status = "Out for delivery";
//                    } else if (Math.random() > 0.4) {
//                        status = "In the oven";
//                    }
//
//                    statusLabel.setText("Status: " + status);
//                    statusLabel.setForeground(new Color(0, 120, 0));
//
//                    estimatedTimeLabel.setText("Estimated delivery: 30-45 minutes");
//                    deliveryInfoLabel.setText("Our delivery person will call you upon arrival.");
//
//                    // Update the order status
//                    orders.put(orderId, status);
//                }
            }
        });

        // Add components to transparent panel
        transparentPanel.add(inputPanel, BorderLayout.NORTH);
        transparentPanel.add(statusPanel, BorderLayout.CENTER);

        // Add transparent panel to main panel
        panel.add(transparentPanel, BorderLayout.CENTER);

        return panel;
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

    // Enhanced button styling with hover effects and optional text color
    private static JButton createStyledButton(String text, Color baseColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(baseColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // Add enhanced hover effect with smoother transition
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Mix the base color with white for a brighter effect
                Color brighterColor = new Color(
                        Math.min(baseColor.getRed() + 30, 255),
                        Math.min(baseColor.getGreen() + 30, 255),
                        Math.min(baseColor.getBlue() + 30, 255)
                );
                button.setBackground(brighterColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                // Slightly darker when pressed
                button.setBackground(baseColor.darker());
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                // Return to hover color when released
                if (button.contains(evt.getPoint())) {
                    Color brighterColor = new Color(
                            Math.min(baseColor.getRed() + 30, 255),
                            Math.min(baseColor.getGreen() + 30, 255),
                            Math.min(baseColor.getBlue() + 30, 255)
                    );
                    button.setBackground(brighterColor);
                } else {
                    button.setBackground(baseColor);
                }
            }
        });

        return button;
    }

    public static Object[][] convertToPizzaData(List<Pizza> pizzas) {
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
            case "extralarge":
                return pizza.getExtraLargePrice();
            default:
                throw new IllegalArgumentException("Invalid size: " + size);
        }
    }
}
