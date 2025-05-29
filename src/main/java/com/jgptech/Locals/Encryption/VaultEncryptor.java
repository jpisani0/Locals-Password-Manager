/*
 * NAME: VaultEncryptor
 * AUTHOR: J. Pisani
 * DATE: 4/26/25
 *
 * DESCRIPTION: Support for encrypting/decrypting vault data
 */

package com.jgptech.Locals.Encryption;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// REVIEW: these may be able to be moved to the vault class as private methods and remove this class entirely

public abstract class VaultEncryptor {
    // TODO: add support for other encryption algorithms, likely using a private method that converts the EncryptionAlgorithm enum to the string for Cipher

    // Encrypt the data to be stored in the vault
    public static String encrypt(String data, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        String encryptedString = "";

        try {
            String algorithmName = encryptionAlgorithm.toString();
            Cipher cipher = Cipher.getInstance(algorithmName);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData); // Encode with Base64 here as the resulting bytes from encryption may not correspond to actual characters
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            // TODO: better handling of exceptions
            System.out.println("ERROR: VaultEncryptor.encrypt(): " + e.toString());
        }

        return encryptedString;
    }

    // Decrypt the data from the vault
    public static String decrypt(String data, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        String decryptedString = "";

        try {
            String algorithmName = encryptionAlgorithm.toString();
            Cipher cipher = Cipher.getInstance(algorithmName);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(decryptedData); // Return as a string cast here as we know the decrypted data will be able to be represented this way
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            // TODO: better handling of exceptions
            System.out.println("ERROR: VaultEncryptor.decrypt(): " + e.toString() + ": " + e.getMessage());
        }

        return decryptedString;
    }
}
