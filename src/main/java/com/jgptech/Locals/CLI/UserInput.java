/*
 * NAME: UserInput
 * AUTHOR: J. Pisani
 * DATE: 4/28/25
 *
 * DESCRIPTION: Handles getting user input for commands on the command line and passing them to the appropriate methods
 */

package com.jgptech.Locals.CLI;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.HashingAlgorithm;
import com.jgptech.Locals.Encryption.KeyHasher;
import com.jgptech.Locals.Encryption.PasswordGenerator;
import com.jgptech.Locals.Vault.Vault;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.SecretKey;

public abstract class  UserInput {
    // Attempts for unlocking a vault before process is aborted
    private final static int MAX_ATTEMPTS = 3;

    // Handle to the console
    private final static Console console = System.console();

    // Scanner for input
    private final static Scanner scanner = new Scanner(System.in);

    // String to hold the input
    private static String input;


    // Get user information for creating a new vault
    public static void createNewVault(String vaultName) {
        HashingAlgorithm hashingAlgorithm = HashingAlgorithm.NoHashingAlgorithm;
        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.NoEncryptionAlgorithm;
        int iterations = 0;
        String masterPassword = "";
        byte[] masterHash;

        // If no name for the new vault is provided, get it from the user
        while(vaultName.isEmpty()) {
            System.out.print("Name of new vault: ");
            vaultName = scanner.nextLine();
        }

        // Loop until we get a valid hashing algorithm from the user
        while(hashingAlgorithm == HashingAlgorithm.NoHashingAlgorithm) {
            System.out.println("1. PBKDF2");
            System.out.println("More to come soon....");
            System.out.println();
            System.out.print("Choose a hashing algorithm (default=PBKDF2): ");

            String response = scanner.nextLine();

            if(response.isEmpty()) {
                hashingAlgorithm = HashingAlgorithm.PBKDF2;
            } else {
                try {
                    hashingAlgorithm = HashingAlgorithm.fromValue(Integer.parseInt(response));
                } catch (IllegalArgumentException e) {
                    // Value entered was either not a number (NumberFormatException) or not a value from the list (IllegalArgumentException)
                    System.out.println("Error: enter a number from the list");
                    System.out.println();
                }
            }
        }

        // Loop until we get a valid number of iterations from the user
        while(iterations == 0) {
            System.out.print("PIM (blank for default): ");

            String response = scanner.nextLine();

            // Set the default iterations if the user enters blank
            if(response.isEmpty()) {
                iterations = KeyHasher.DEFAULT_ITERATIONS;
            } else {
                try {
                    iterations = Integer.parseInt(response);

                    // Check if the given number of iterations is small and warn the user
                    if(iterations < KeyHasher.DEFAULT_ITERATIONS) {
                        System.out.println("The number of iterations you chose, " + iterations + ", is unusually low for proper security.");
                        System.out.println("Most systems call for at least 600,000 iterations to be secure.");
                        System.out.print(" Do you want to continue with " + iterations +  " iterations anyway? (y/N): ");

                        // Set iterations back to zero for another loop if the user requests to change their PIM
                        if(input.equals("n") || input.equals("N")) {
                            iterations = 0;
                        }
                    }
                } catch(NumberFormatException e) {
                    System.out.println("Error: enter a valid PIM");
                }
            }
        }

        // TODO: add keyfile support

        // Loop until we get a valid encryption algorithm from the user
        while(encryptionAlgorithm == EncryptionAlgorithm.NoEncryptionAlgorithm) {
            System.out.println("1. AES");
            System.out.println("More to come soon...");
            System.out.println();
            System.out.print("Choose an encryption algorithm (default=AES): ");

            String response = scanner.nextLine();

            if(response.isEmpty()) {
                encryptionAlgorithm = EncryptionAlgorithm.AES;
            } else {
                try {
                    encryptionAlgorithm = EncryptionAlgorithm.fromValue(Integer.parseInt(scanner.nextLine()));
                } catch(IllegalArgumentException e) {
                    // Value entered was either not a number (NumberFormatException) or not a value from the list (IllegalArgumentException)
                    System.out.println("Error: enter a number from the list");
                }
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

                String response = scanner.nextLine();

                if(response.isEmpty()) {
                    length = 20;
                } else {
                    try {
                        length = Integer.parseInt(scanner.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("Enter a positive integer for the length");
                        System.out.println();
                    }
                }
            }

            // Loop until the user accepts a randomly generated password
            while(masterPassword.isEmpty()) {
                masterPassword = PasswordGenerator.generatePassword(length);
                System.out.println("Your randomly generated password is: " + masterPassword);
                System.out.print("Do you want to regenerate the password? [Y/n]: ");
                input = scanner.nextLine();

                if(input.equals("y") || input.equals("Y") || input.isEmpty()) {
                    masterPassword = "";
                }
            }
        } else {
            // Loop until a valid password is entered and then re-entered correctly
            while(masterPassword.isEmpty()) {
                // TODO: add try catch block for this line in the case that we cant get a handle on the console (java.lang.NullPointerException)
                // Get the password for this vault from the user while hiding it in the terminal by disabling echo
                masterPassword = Arrays.toString(console.readPassword("Enter the password for this vault: "));

                // Verify the password from the user
                if(!masterPassword.equals(Arrays.toString(console.readPassword("Verify the password for this vault: ")))) {
                    System.out.println("Passwords do not match, please try again");
                    masterPassword = "";
                }
            }
        }

        // Create the key hasher object
        byte[] salt = KeyHasher.generateSalt();
        KeyHasher hasher = new KeyHasher(masterPassword, salt, hashingAlgorithm, iterations);

        // Hash the master password using the chosen KDF to get a secure cryptographic key
        SecretKey key = hasher.deriveSecretKey();
        masterHash = key.getEncoded();

        Vault vault = new Vault(vaultName, hashingAlgorithm, encryptionAlgorithm, iterations, salt, hasher.hashKey(masterHash), key);

        if(vault.write()) {
            System.out.println("New vault " + vaultName + " created successfully! Use locals " + vaultName + " to open it and start adding passwords.");
        } else {
            System.out.println("ERROR: unable to create the new vault at " + vaultName + "!");
        }
    }

    // Open a vault file and a shell for the user to access it
    public static void openVault(String vaultName) {
        Vault vault;
        String userPassword;
        int attempts = 0;
        SecretKey key = null;

        // Try loading the file
        vault = Vault.load(vaultName);

        if(vault == null) {
            System.out.println("ERROR: could not load vault " + vaultName);
        } else {
            // Allow the user to try entering the correct password 3 times before aborting
            while(attempts < MAX_ATTEMPTS) {
                userPassword = Arrays.toString(console.readPassword("Enter password for the vault: "));
                KeyHasher hasher = new KeyHasher(userPassword, vault.getSalt(), vault.getHashingAlgorithm(), vault.getIterations());
                key = hasher.deriveSecretKey();
                byte[] hashedUserPassword = hasher.hashKey(key.getEncoded());

                // Break out of the loop if the passwords match
                if(Arrays.equals(hashedUserPassword, vault.getMasterHash())) {
                    break;
                }

                System.out.println("That password was incorrect, please try again");
                attempts++;
            }

            if(attempts >= MAX_ATTEMPTS) {
                System.out.println("Max attempts reached.");
                System.exit(-1); // REVIEW: exit code ?
            }

            // Set the name (path) of the vault
            vault.setName(vaultName);

            // REVIEW: can change shell to open an alternate terminal so that the information is not readable after its closed?
            // Password is correct, open the shell to allow the user to access the vault
            Shell shell = new Shell(vault, key);
            shell.start();

            // Write the data back to the file in case anything changed
            vault.write();
        }
    }
}
