/*
 * NAME: CredentialEncryption
 * AUTHOR:  D. MacCarthy
 * DATE: 11/14/24
 *
 * DESCRIPTION: Class to encrypt all data written to files/decrypt all data read from files
 */

package PasswordStorage;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

public class CredentialEncryption {
    private final SecretKey secretKey;

    // Constructor to generate a new AES key
    public CredentialEncryption() throws Exception {
        String keyFilePath = "keyFile.txt";
        this.secretKey = loadOrGenerateKey(keyFilePath);
    }

    // Method to generate a new AES key and save it
    private SecretKey generateKey(String keyFilePath) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        // Get valid key size from user
        int keySize = getValidKeySize();

        keyGen.init(keySize); // Initialize key generator
        SecretKey key = keyGen.generateKey();

        // Save the key to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(keyFilePath))) {
            writer.write(Base64.getEncoder().encodeToString(key.getEncoded()));
        }
        System.out.println("Generated AES Key with size: " + keySize);
        return key;
    }

    // Prompt user until a valid AES key size is entered
    private int getValidKeySize() {
        Scanner scanner = new Scanner(System.in);
        int keySize;
        while (true) {
            System.out.print("Enter AES key size (128, 192, or 256): ");
            if (scanner.hasNextInt()) {
                keySize = scanner.nextInt();
                if (keySize == 128 || keySize == 192 || keySize == 256) {
                    return keySize;
                }
            }
            System.out.println("Invalid key size. Please enter 128, 192, or 256.");
            scanner.nextLine(); // Clear invalid input
        }

    }

    // Method to load an existing key or generate a new one if not found
    private SecretKey loadOrGenerateKey(String keyFilePath) throws Exception {
        File keyFile = new File(keyFilePath);
        if (keyFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(keyFilePath))) {
                String encodedKey = reader.readLine();
                return reconstructKey(encodedKey);
            }
        } else {
            return generateKey(keyFilePath);
        }
    }

    // Method to encrypt text
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Encode for safe storage
    }

    // Method to decrypt text
    public String decrypt(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    // Static method to reconstruct a SecretKey from an encoded string
    private static SecretKey reconstructKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
