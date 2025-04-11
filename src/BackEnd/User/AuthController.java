package BackEnd.User;

import Utils.EncryptionUtil;
import Utils.SessionManager;

public class AuthController {

    public static boolean authenticateUser(String username, String password) {
        String storedHash = UserDAO.getUserPassword(username);
        if (storedHash == null) {
            System.err.println("User not found: " + username);
            return false;
        }
        boolean isValid = EncryptionUtil.verifyPassword(password, storedHash);

        if (isValid) {
            SessionManager.loginUser(username);
            System.out.println("User authenticated successfully: " + username);
            return true;
        } else {
            System.err.println("Authentication failed for user: " + username);
            return false;
        }}
    
    public static boolean registerUser(String username, String password) {
        if (UserDAO.getUserPassword(username) != null) {
            System.err.println("Username already exists: " + username);
            return false;
        }
        String hashedPassword = EncryptionUtil.hashPassword(password);
        if (hashedPassword == null) {
            System.err.println("Password hashing failed for user: " + username);
            return false;
        }
        boolean success = UserDAO.createUser(username, hashedPassword);
        if (success) {
            System.out.println("User registered successfully: " + username);
        } else {
            System.err.println("User registration failed: " + username);
        }
        return success;
    }
}