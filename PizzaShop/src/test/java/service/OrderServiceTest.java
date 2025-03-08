/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package service;

import java.math.BigDecimal;
import java.util.HashMap;
import model.Customer;
import model.OrderItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author shubhijain
 */
public class OrderServiceTest {

    public OrderServiceTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of placeOrder method, of class OrderService.
     */
    @Test
    public void testPlaceOrder() throws Exception {
        System.out.println("placeOrder");
        Customer customer = null;
        OrderItem[] orderItems = null;
        String specialInstructions = "";
        BigDecimal totalValue = null;
        OrderService instance = new OrderService();
        int expResult = 0;
        int result = instance.placeOrder(customer, orderItems, specialInstructions, totalValue);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOrderDetails method, of class OrderService.
     */
    @Test
    public void testGetOrderDetails() throws Exception {
        System.out.println("getOrderDetails");
        int orderId = 0;
        OrderService instance = new OrderService();
        HashMap<String, String> expResult = null;
        HashMap<String, String> result = instance.getOrderDetails(orderId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
