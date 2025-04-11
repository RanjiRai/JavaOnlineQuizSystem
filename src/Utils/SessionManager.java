package Utils;

public class SessionManager {

    private static String loggedInUser = null;
      //Logs in a user by storing their username in the session.
      // @param username The username of the logged-in user.
     
    public static void loginUser(String username) {
        loggedInUser = username;
        System.out.println("User logged in: " + username);
    }

    // Logs out the current user. 
    public static void logoutUser() {
        System.out.println("User logged out: " + loggedInUser);
        loggedInUser = null;
    }

    //  Checks if a user is currently logged in. 
     // @return True if a user is logged in, otherwise false.   
    public static boolean isUserLoggedIn() {
        return loggedInUser != null;
    }
   
     // Gets the username of the currently logged-in user.   
      //@return The username of the logged-in user, or null if no user is logged in     
    public static String getLoggedInUser() {
        return loggedInUser;
    }
}