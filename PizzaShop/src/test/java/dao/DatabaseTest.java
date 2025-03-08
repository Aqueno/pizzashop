
import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private static Connection connection;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Create an H2 in-memory database connection
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");

        // Create a table for testing
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255));");
        }
    }

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users;"); // Clean table before each test
        }
    }

    @Test
    void testInsertAndRetrieveUser() throws SQLException {
        // Insert a record
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)")) {
            pstmt.setInt(1, 1);
            pstmt.setString(2, "Shubhi");
            pstmt.executeUpdate();
        }

        // Retrieve the record
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT name FROM users WHERE id = ?")) {
            pstmt.setInt(1, 1);
            ResultSet resultSet = pstmt.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("Shubhi", resultSet.getString("name"));
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }
}
