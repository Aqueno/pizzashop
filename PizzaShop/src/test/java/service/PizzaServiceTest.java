/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package service;

import java.util.List;
import model.Pizza;
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
public class PizzaServiceTest {

    public PizzaServiceTest() {
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
     * Test of getFullMenu method, of class PizzaService.
     */
    @Test
    public void testGetFullMenu() throws Exception {
        System.out.println("getFullMenu");
        PizzaService instance = new PizzaService();
        List<Pizza> expResult = null;
        List<Pizza> result = instance.getFullMenu();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPizzaNames method, of class PizzaService.
     */
    @Test
    public void testGetPizzaNames() throws Exception {
        System.out.println("getPizzaNames");
        PizzaService instance = new PizzaService();
        List<String> expResult = null;
        List<String> result = instance.getPizzaNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
