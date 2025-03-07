package service;

import dao.PizzaDAO;
import java.sql.*;
import java.util.List;
import model.Pizza;

/**
 * Service class responsible for handling pizza-related operations, including
 * retrieving pizza data.
 */
public class PizzaService {

    private PizzaDAO pizzaDAO;

    /**
     * Default constructor that initializes the PizzaDAO object.
     */
    public PizzaService() {
        pizzaDAO = new PizzaDAO();
    }

    /**
     * Retrieves the full menu of pizzas.
     *
     * @return A list of all pizzas available in the menu.
     * @throws SQLException If there is an issue with the database interaction.
     */
    public List<Pizza> getFullMenu() throws SQLException {
        return pizzaDAO.getAllPizzas();
    }

    /**
     * Retrieves the names of all pizzas.
     *
     * @return A list of pizza names.
     * @throws SQLException If there is an issue with the database interaction.
     */
    public List<String> getPizzaNames() throws SQLException {
        return pizzaDAO.getPizzaNames();
    }
}
