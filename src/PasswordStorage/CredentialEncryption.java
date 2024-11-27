package PasswordStorage;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;

public class CredentialEncryption {
    private final SecretKey secretKey;

    // Constructor to generate a new AES key
    public CredentialEncryption(String keyFilePath) throws Exception {
        this.secretKey = loadOrGenerateKey(keyFilePath);
    }

    // Method to generate a new AES key and save it
    private SecretKey generateKey(String keyFilePath) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES key size (128, 192, or 256 bits)
        SecretKey key = keyGen.generateKey();

        // Save the key to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(keyFilePath))) {
            writer.write(Base64.getEncoder().encodeToString(key.getEncoded()));
        }
        return key;
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
