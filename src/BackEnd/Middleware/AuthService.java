package BackEnd.Middleware;

import BackEnd.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public static boolean validateUser(String username, String enteredPassword, String role) {
        String query = "SELECT password FROM users WHERE username=? AND role=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, role);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return enteredPassword.equals(storedPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}