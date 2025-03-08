package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Connection;
import java.sql.SQLException;
import util.DBConnection;

public class DBConnectionTest {

    @Test
    public void testGetConnection() throws SQLException {
        // Act
        Connection connection = DBConnection.getConnection();

        // Assert
        assertNotNull(connection, "Connection should not be null.");
        connection.close();  // Close the connection after test
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        assertThrows(SQLException.class, () -> {
            DBConnection.getConnection("jdbc:mysql://invalid-url", "invalid-user", "invalid-password");
        });
    }

}
