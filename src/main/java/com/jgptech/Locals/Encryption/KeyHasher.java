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
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;
import java.util.Base64;

public class KeyHasher {
    private static final int SALT_LENGTH = 16; // Length of salts generated (bytes)
    private static final int KEY_LENGTH = 256; // Length of derived keys (bytes)
    public static final int DEFAULT_ITERATIONS = 600000; // Default number of hash iterations

    public String password; // The user's master password as a String
    public byte[] salt; // The salt used with the master password before hashing
    public HashingAlgorithm algorithm; // The hashing algorithm chosen
    public int iterations; // Number of times to hash the master password

    // Constructor for the KeyHasher class
    public KeyHasher(String password, byte[] salt, HashingAlgorithm algorithm, int iterations) {
        this.password = password;
        this.salt = salt;
        this.algorithm = algorithm;
        this.iterations = iterations;
    }

    // Hash the master password into a cryptographic key
    public String hashMasterPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // TODO: add support for other hashing algorithms, likely using a private method that converts the HashingAlgorithm enum to the string for SecretKeyFactory
        String algorithmName = "PBKDF2WithHmacSHA256";
        byte[] hash;
        SecretKeyFactory skf;
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);

        skf = SecretKeyFactory.getInstance(algorithmName);
        hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    // Hash the derived key to safely store in the vault file
    public String hashKey(final byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(key);
        return Base64.getEncoder().encodeToString(hash);
    }

    // Generate a random salt to be used in the KDF
    public static byte[] generateSalt() {
        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        rand.nextBytes(salt);
        return salt;
    }
}
