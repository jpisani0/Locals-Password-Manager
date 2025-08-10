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
    public static String encrypt(String data, byte[] key, byte[] iv) {
        String encryptedString = "";

        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] cipherText = cipher.doFinal(data.getBytes());

            byte[] encrypted = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(cipherText); // Encode with Base64 here as the resulting bytes from encryption may not correspond to actual characters
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            // TODO: better handling of exceptions
            System.out.println("ERROR: VaultEncryptor.encrypt(): " + e.toString());
        }

        return encryptedString;
    }

    // Decrypt the data from the vault
    public static String decrypt(String data, byte[] key, byte[] iv) {
        String decryptedString = "";

        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            byte[] encrypted = Base64.getDecoder().decode(data);

            byte[] ciphertext = new byte[encrypted.length - iv.length];
            System.arraycopy(encrypted, iv.length, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            byte[] decryptedData = cipher.doFinal(encrypted);
            return new String(decryptedData); // Return as a string cast here as we know the decrypted data will be able to be represented this way
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            // TODO: better handling of exceptions
            System.out.println("ERROR: VaultEncryptor.decrypt(): " + e.toString() + ": " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        return decryptedString;
    }

    // Generate a unique IV for encryption
    public static byte[] generateIV() {
        SecureRandom rand = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH]; // 12 byte IV recommended for GCM
        rand.nextBytes(iv);
        return iv;
    }
}
