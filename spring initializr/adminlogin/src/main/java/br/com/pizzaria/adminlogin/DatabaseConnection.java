package br.com.pizzaria.adminlogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bancopizzaria",
                "root",
                "GustavoZlb@123"
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }

    public static boolean checkCredentials(String username, String password) {
        boolean authenticated = false;
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            authenticated = rs.next(); 
        } catch (Exception e) {
            System.out.println("Error checking credentials: " + e.getMessage());
        }
        return authenticated;
    }

    public static String findEmailByUsername(String username) {
        String email = null;
        try (Connection conn = connect()) {
            String sql = "SELECT email FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (Exception e) {
            System.out.println("Error fetching email: " + e.getMessage());
        }
        return email;
    }
}
