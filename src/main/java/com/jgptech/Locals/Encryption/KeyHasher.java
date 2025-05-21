/*
 * NAME: KeyHasher
 * AUTHOR: J. Pisani
 * DATE: 4/26/25
 *
 * DESCRIPTION: Handles generating a cryptographic key from the user's master password
 */

package com.jgptech.Locals.Encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;
import java.util.Base64;

public class KeyHasher {
    // Length of salts generated (bytes)
    private static final int SALT_LENGTH = 16;

    // Length of derived keys (bytes)
    private static final int KEY_LENGTH = 256;

    // Default number of hash iterations
    public static final int DEFAULT_ITERATIONS = 600000;

    // The user's master password as a String
    public String password;

    // The salt used with the master password before hashing
    public byte[] salt;

    // The hashing algorithm chosen
    public HashingAlgorithm algorithm;

    // Number of times to hash the master password
    public int iterations;


    // Constructor for the KeyHasher class
    public KeyHasher(String password, byte[] salt, HashingAlgorithm algorithm, int iterations) {
        this.password = password;
        this.salt = salt;
        this.algorithm = algorithm;
        this.iterations = iterations;
    }

    // Hash the master password into a cryptographic key
    public SecretKey deriveSecretKey() {
        try {
            // TODO: add support for other hashing algorithms, likely using a private method that converts the HashingAlgorithm enum to the string for SecretKeyFactory
            String algorithmName = "PBKDF2WithHmacSHA256";
            byte[] hash;
            SecretKeyFactory skf;
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);

            skf = SecretKeyFactory.getInstance(algorithmName);
            return skf.generateSecret(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }
    }

    // Hash the derived key to safely store in the vault file
    public byte[] hashKey(final byte[] key) {
        byte[] hash = new byte[KEY_LENGTH];

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(key);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return hash;
    }

    // Generate a random salt to be used in the KDF
    public static byte[] generateSalt() {
        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        rand.nextBytes(salt);
        return salt;
    }
}
