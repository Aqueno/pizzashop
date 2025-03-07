/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzashop;

import util.DBConnection;
import java.sql.*;

/**
 *
 * @author shubhijain
 */
public class Main {

    public static void main(String[] args) {
        // Attempting to connect to the database
        try {
            // Load MySQL JDBC driver (optional in modern versions of Java)
//            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection conn = DBConnection.getConnection();

            if (conn != null) {
                System.out.println("Connection to AWS RDS database successful!");
                // Optionally close the connection after testing
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
