package PasswordStorage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageCredentialFile {
    private final String credFile;
    private final CredentialEncryption credentialEncryption;

    // Constructor to initialize file path and encryption manager
    public ManageCredentialFile(String credFile, CredentialEncryption credentialEncryption) {
        this.credFile = credFile;
        this.credentialEncryption = credentialEncryption;
    }

    // Method to write encrypted credentials to the file
    public void writeCredentials() throws IOException, Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> credentialsToWrite = new ArrayList<>();

        System.out.println("Enter credentials (format: username:password). Type 'done' to stop:");
        while (true) {
            String credData = scanner.nextLine();
            if (credData.equalsIgnoreCase("done")) {
                break;
            }
            credentialsToWrite.add(credData);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(credFile, true))) { // Append mode
            for (String credential : credentialsToWrite) {
                String encryptedCredential = credentialEncryption.encrypt(credential);
                writer.write(encryptedCredential);
                writer.newLine(); // Add a newline after each encrypted credential
            }
            System.out.println("Credentials encrypted and written to file successfully.");
        }
    }

    // Method to read and decrypt credentials from the file
    public List<String> readCredentials() throws IOException, Exception {
        List<String> credentials = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(credFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String decryptedCredential = credentialEncryption.decrypt(line); // Decrypt each line
                credentials.add(decryptedCredential);
            }
        }
        System.out.println("Encrypted credentials read and decrypted successfully.");
        return credentials;
    }
}