package Utils;

public class EncryptionUtil {

    // Returns the password as-is (no hashing)
    public static String hashPassword(String password) {
        return password; // No encryption or hashing
    }

    // Verifies password using simple string comparison
    public static boolean verifyPassword(String enteredPassword, String storedPassword) {
        return enteredPassword.equals(storedPassword); // Direct comparison
    }
}
