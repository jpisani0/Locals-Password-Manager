/*
 * NAME: CredentialEncryption
 * AUTHOR:  D. MacCarthy
 * DATE: 11/28/24
 *
 * DESCRIPTION: Class to test Writing to and Reading from file, and ensuring all encryption and decryption works
 */

package PasswordStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageCredFileTest {
    public static void main(String[] args) {

        //will store files in project folder
        String credFile = "credFile.txt";
        //String keyFileName = "keyFile.txt";
        try {
            // Create an EncryptionManager and display the key for reference
            CredentialEncryption credentialEncryption = new CredentialEncryption();
            ManageCredentialFile manageCredentials = new ManageCredentialFile(credFile, credentialEncryption);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n--- Manage Credentials ---");
                System.out.println("1. Add Credentials");
                System.out.println("2. View Credentials");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice == 1) {
                    // Write encrypted credentials to the file
                     List<String> credentialsToWrite = getCredentialsFromUser(scanner);
                     manageCredentials.writeCredentials(credentialsToWrite);
                } else if (choice == 2) {
                    // Read and display decrypted credentials
                    List<String> readCredentials = manageCredentials.readCredentials();
                    for (String credential : readCredentials) {
                        System.out.println(credential);
                    }

                } else if (choice == 3) {
                    System.out.println("Exiting program. Goodbye!");
                    break;

                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
    // Prompts user to enter credentials and stores them in a list
    private static List<String> getCredentialsFromUser(Scanner scanner) {
        List<String> credentialsToWrite = new ArrayList<>();
        System.out.println("Enter credentials (format: title:username:password:URL:Notes:). Type 'done' to stop:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            credentialsToWrite.add(input);
        }
        return credentialsToWrite;
    }
}