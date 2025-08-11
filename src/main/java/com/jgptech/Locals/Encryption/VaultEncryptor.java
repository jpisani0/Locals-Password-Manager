/*
 * NAME: VaultEncryptor
 * AUTHOR: J. Pisani
 * DATE: 4/26/25
 *
 * DESCRIPTION: Support for encrypting/decrypting vault data
 */

package com.jgptech.Locals.Encryption;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public final class VaultEncryptor {
    // Length (in bits) of the tag for the GCM parameter
    private final static int GCM_TAG_LENGTH = 128;

    // Size (in bytes) of generated IVs
    private final static int IV_LENGTH = 12; // REVIEW could bring this to 16 bytes


    // Prevent instantiation
    private VaultEncryptor() {}

    // TODO: add support for other encryption algorithms, likely using a private method that converts the EncryptionAlgorithm enum to the string for Cipher
    // REVIEW: possibly add AAD (Additional Authenticated Data) to the project based off the file name or other meta data

    // Encrypt the data to be stored in the vault
    public static String encrypt(String data, byte[] key) {
        try {
            // Generate a new unique IV
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom rand = new SecureRandom();
            rand.nextBytes(iv);

            // Create the spec objects for the key and GCM parameters
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            // Get the cipher object in encrypt mode using AES with GCM and no padding
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            // Encrypt the data
            byte[] cipherText = cipher.doFinal(data.getBytes());

            // Create the array to hold the resulting encrypted string in
            byte[] encrypted = new byte[iv.length + cipherText.length];

            // Copy the ciphertext with the IV prepended into the array
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);

            // Encode with Base64 here as the resulting bytes from encryption may not correspond to actual characters
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            // TODO: better handling of exceptions
            System.out.println("ERROR: VaultEncryptor.encrypt(): " + e.toString());
            return "";
        }
    }

    // Decrypt the data from the vault
    public static String decrypt(String data, byte[] key) {
        try {
            // Decode the base64 encoded string to bytes
            byte[] encrypted = Base64.getDecoder().decode(data);

            // Extract the IV from the encrypted array
            byte[] iv = Arrays.copyOfRange(encrypted, 0, IV_LENGTH);

            // Extract the ciphertext from the rest of the array
            byte[] cipherText = Arrays.copyOfRange(encrypted, IV_LENGTH, encrypted.length);

            // Get the spec objects for the key and GCM parameters
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            // Get the cipher object in decrypt mode using AES with GCM and no padding
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            // Decrypt the cipher text
            byte[] decryptedData = cipher.doFinal(cipherText);

            // Return as a string cast here as we know the decrypted data will be able to be represented this way
            return new String(decryptedData);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            // TODO: better handling of exceptions
            System.out.println("ERROR: VaultEncryptor.decrypt(): " + e.toString() + ": " + e.getMessage());
            return "";
        }
    }
}
