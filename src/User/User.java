package User;

import java.util.Scanner;

public class User {
    private boolean success = false; // Flag for successful login/user creation
    private byte usernameSalt; // Salt added to the username before hashing
    private String usernameHash; // Hash of the username
    private byte passwordSalt; // Salt added to the password before hashing
    private String passwordHash; // Hash of the password
    private String userFile; // File that holds the user's
    Scanner scanner = new Scanner(System.in);

    // Constructor for creating a new user
    public User() {

    }

    // Constructor for logging into an existing user
    public User(String username, String password) {
        /* HASH THE USERNAME AND PASSWORD HERE */
        // this.usernameHash = Hash.hash(username)
        // this.passwordHash = Hash.hash(password)

        // Try logging into this account
        success = logIn(username, password);
    }

    // Check is a user exists already
    private boolean exists(String username) {
        boolean status = true;

        return status;
    }

    // Log in to an existing user
    private boolean logIn(String username, String password) {
        boolean status = true;

        return status;
    }

    // Create a new user
    private boolean createNewUser(String username) {
        boolean status = true;

        return status;
    }

    // Get the success flag
    public boolean getSuccess() {
        return success;
    }

    // Create a new user
    public boolean createNewUser() {
        boolean status = true;

        return status;
    }
}
