package BackEnd.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.sql.Driver;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/online_quiz_db3?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; //  MySQL username
    private static final String PASSWORD = "dfamily13245"; //  MySQL password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found! Make sure it's in the classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed! Check URL, username, password, and server status.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        getConnection();
    }
}