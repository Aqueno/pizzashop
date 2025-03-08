package dao;

import model.Pizza;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

/**
 * Data Access Object (DAO) for handling pizza-related database operations.
 * Provides methods to retrieve all pizzas and pizza names from the database.
 */
public class PizzaDAO {

    private static final String GET_ALL_PIZZAS_QUERY = "SELECT * FROM pizzas";
    private static final String GET_PIZZA_NAMES_QUERY = "SELECT name FROM pizzas";

    /**
     * Retrieves a list of all pizzas with their details from the database.
     *
     * @return A list of Pizza objects representing all pizzas in the database.
     * @throws SQLException If a database error occurs.
     */
    public List<Pizza> getAllPizzas() throws SQLException {
        List<Pizza> pizzas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(GET_ALL_PIZZAS_QUERY)) {
            while (rs.next()) {
                Pizza pizza = new Pizza();
                pizza.setPizzaId(rs.getInt("pizza_id"));
                pizza.setName(rs.getString("name"));
                pizza.setDescription(rs.getString("description"));
                pizza.setSmallPrice(rs.getBigDecimal("small_price"));
                pizza.setMediumPrice(rs.getBigDecimal("medium_price"));
                pizza.setLargePrice(rs.getBigDecimal("large_price"));
                pizza.setExtraLargePrice(rs.getBigDecimal("extra_large_price"));
                pizzas.add(pizza);
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return pizzas;
    }

    /**
     * Retrieves a list of all pizza names from the database.
     *
     * @return A list of pizza names.
     * @throws SQLException If a database error occurs.
     */
    public List<String> getPizzaNames() throws SQLException {
        List<String> pizzaNames = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(GET_PIZZA_NAMES_QUERY)) {
            while (rs.next()) {
                pizzaNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return pizzaNames;
    }
}
