/*
 * NAME: UserInput
 * AUTHOR: J. Pisani
 * DATE: 4/28/25
 *
 * DESCRIPTION: Handles getting user input for commands on the command line and passing them to the appropriate methods
 */

package CLI;

import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Scanner;

import Encryption.EncryptionAlgorithm;
import Encryption.HashingAlgorithm;
import Encryption.KeyHasher;
import Encryption.PasswordGenerator;

public abstract class UserInput {
    private final static Console console = System.console();
    private final static Scanner scanner = new Scanner(System.in);
    private static String input;

    // Get user information for creating a new vault
    public static void createNewVault(String vaultName) {
        HashingAlgorithm hashingAlgorithm = HashingAlgorithm.NoHashingAlgorithm;
        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.NoEncryptionAlgorithm;
        int iterations = 0;
        String masterPassword = "";
        String masterHash = "";

        // If no name for the new vault is provided, get it from the user
        while(vaultName.isEmpty()) {
            System.out.print("Name of new vault: ");
            vaultName = scanner.nextLine();
        }

        // TODO: add a default option where the user can just hit enter to choose it

        // Loop until we get a valid hashing algorithm from the user
        while(hashingAlgorithm == HashingAlgorithm.NoHashingAlgorithm) {
            System.out.println("1. PBKDF2");
            System.out.println("More to come soon....");
            System.out.println();
            System.out.print("Choose a hashing algorithm: ");

            try {
                hashingAlgorithm = HashingAlgorithm.fromValue(Integer.parseInt(scanner.nextLine()));
            } catch(IllegalArgumentException e) {
                // Value entered was either not a number (NumberFormatException) or not a value from the list (IllegalArgumentException)
                System.out.println("Error: enter a number from the list");
            }
        }

        // Loop until we get a valid number of iterations from the user
        while(iterations == 0) {
            System.out.print("PIM (blank for default): ");

            try {
                iterations = Integer.parseInt(scanner.nextLine());

                // Check if the given number of iterations is small and warn the user
                if(iterations < KeyHasher.DEFAULT_ITERATIONS) {
                    System.out.println("The number of iterations you chose, " + iterations + ", is unusually low for proper security.");
                    System.out.println("Most systems call for at least 600,000 iterations to be secure.");
                    System.out.print(" Do you want to continue anyway? (y/N): ");

                    String input = scanner.nextLine();

                    // Set iterations back to zero for another loop if the user requests to change their PIM
                    if(input.equals("n") || input.equals("N") || input.isEmpty()) {
                        iterations = 0;
                    }
                }
            } catch(NumberFormatException e) {
                System.out.println("Error: enter a valid PIM");
            }
        }

        // TODO: add keyfile support

        // Loop until we get a valid encryption algorithm from the user
        while(encryptionAlgorithm == EncryptionAlgorithm.NoEncryptionAlgorithm) {
            System.out.println("1. AES");
            System.out.println("More to come soon...");
            System.out.println();
            System.out.print("Choose an encryption algorithm: ");

            try {
                encryptionAlgorithm = EncryptionAlgorithm.fromValue(Integer.parseInt(scanner.nextLine()));
            } catch(IllegalArgumentException e) {
                // Value entered was either not a number (NumberFormatException) or not a value from the list (IllegalArgumentException)
                System.out.println("Error: enter a number from the list");
            }
        }

        // Ask the user if they want a password to be generated for them
        System.out.print("Do you want a randomly generated password? [y/N]: ");
        input = scanner.nextLine();

        if(input.equals("y") || input.equals("Y")) {
            int length = -1;

            // Loop until a valid length for the password is entered
            while(length < 0) {
                System.out.print("Length of password (default=20): ");

                try {
                    length = Integer.parseInt(scanner.nextLine());
                } catch(NumberFormatException e) {
                    System.out.println("Enter a positive integer for the length");
                    System.out.println();
                }
            }

            // Loop until the user accepts a randomly generated password
            while(masterPassword.isEmpty()) {
                masterPassword = PasswordGenerator.generatePassword(length);
                System.out.println("Your randomly generated password is: " + masterPassword);
                System.out.print("Do you want to regenerate the password? [Y/n]: ");
                input = scanner.nextLine();

                if(input == "y" || input == "Y" || input.isEmpty()) {
                    masterPassword = "";
                }
            }
        } else {
            // Loop until a valid password is entered and then re-entered correctly
            while(masterPassword.isEmpty()) {
                // Get the password for this vault from the user while hiding it in the terminal by disabling echo
                masterPassword = Arrays.toString(console.readPassword("Enter the password for this vault: "));

                // Verify the password from the user
                if(!masterPassword.equals(Arrays.toString(console.readPassword("Verify the password for this vault")))) {
                    System.out.println("Passwords do not match, please try again");
                    masterPassword = "";
                }
            }
        }

        // Create the key hasher object
        KeyHasher hasher = new KeyHasher(masterPassword, KeyHasher.generateSalt(), hashingAlgorithm, iterations);

        // Hash the master password using the chosen KDF to get a secure cryptographic key
        try {
            masterHash = hasher.hashMasterPassword();
        } catch(NoSuchAlgorithmException e) {
            System.out.println("Error: hashing algorithm " + hasher.algorithm + " is not a valid option");
        } catch (InvalidKeySpecException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // TODO: need to finish vault file changes and merge them into master then this branch
    }
}
