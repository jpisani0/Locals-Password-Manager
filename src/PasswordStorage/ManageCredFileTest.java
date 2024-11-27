package PasswordStorage;

import java.util.List;
import java.util.Scanner;

public class ManageCredFileTest {
    public static void main(String[] args) {

        //will store files in project folder
        String credFile = "credFile.txt";
        String keyFileName = "keyFile.txt";
        try {
            // Create an EncryptionManager and display the key for reference
            CredentialEncryption credentialEncryption = new CredentialEncryption(keyFileName);
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
                    manageCredentials.writeCredentials();

                } else if (choice == 2) {
                    // Read and display decrypted credentials
                    List<String> readCredentials = manageCredentials.readCredentials();
                    if (readCredentials.isEmpty()) {
                        System.out.println("No credentials found.");
                    } else {
                        System.out.println("Decrypted Credentials from file:");
                        for (String credential : readCredentials) {
                            System.out.println(credential);
                        }
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
}
