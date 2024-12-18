/*
 * NAME: ManageCredentialFile
 * AUTHOR:  D. MacCarthy
 * DATE: 11/7/24
 *
 * DESCRIPTION: Class to Write to and Read from different Credential Files
 */

package PasswordStorage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageCredentialFile {
    private final String credFile;
    private final CredentialEncryption credentialEncryption;

    // Constructor to initialize file path and encryption manager
    public ManageCredentialFile(String credFile, CredentialEncryption credentialEncryption) throws IOException {
        this.credFile = credFile;
        this.credentialEncryption = credentialEncryption;
        ensureFileExists(credFile);
    }

    // Ensures the credentials file exists; creates it if not
    private void ensureFileExists(String credFile) throws IOException {
        File file = new File(credFile);
        if (!file.exists()) {
            System.out.println("Credentials file not found. Creating a new one...");
            if (file.createNewFile()) {
                System.out.println("File created successfully: " + credFile);
            } else {
                throw new IOException("Failed to create the credentials file: " + credFile);
            }
        }
    }

    // Method to write encrypted credentials to the file
    public void writeCredentials(List<String> credentialsToWrite) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(credFile, true))) { // Append mode
            for (String credential : credentialsToWrite) {
                String encryptedCredential = credentialEncryption.encrypt(credential);
                writer.write(encryptedCredential);
                writer.newLine(); // Add a newline after each encrypted credential
            }
            writer.newLine();
            System.out.println("Credentials encrypted and written to file successfully.");
        }
    }

    // Method to read and decrypt credentials from the file
    public List<String> readCredentials() throws Exception {
        List<String> credentials = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(credFile))) {
            String line;
            if (credFile.isEmpty()){
                System.out.println("No credentials found");
            } else {
                while ((line = reader.readLine()) != null) {
                    String decryptedCredential = credentialEncryption.decrypt(line); // Decrypt each line
                    credentials.add(decryptedCredential);
                }
            }
        }
        System.out.println("Encrypted credentials read and decrypted successfully.");
        return credentials;
    }
}