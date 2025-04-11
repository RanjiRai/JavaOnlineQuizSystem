package BackEnd.User;

import BackEnd.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static boolean createUser(String username, String password) {
        String checkUserQuery = "SELECT 1 FROM users WHERE username = ?";
        String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery)) {

            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    System.err.println("User already exists: " + username);
                    return false;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User registered successfully: " + username);
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in createUser: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static String getUserPassword(String username) {
        String query = "SELECT password FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                } else {
                    System.err.println("User not found: " + username);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in getUserPassword: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static boolean validateUser(String username, String enteredPassword) {
        String storedPassword = getUserPassword(username);

        if (storedPassword == null) {
            System.out.println("User authentication failed: " + username);
            return false;
        }

        boolean isMatch = storedPassword.equals(enteredPassword);
        System.out.println("Password match for " + username + ": " + isMatch);
        return isMatch;
    }
}