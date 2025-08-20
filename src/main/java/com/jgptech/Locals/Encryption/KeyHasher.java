/*
 * NAME: KeyHasher
 * AUTHOR: J. Pisani
 * DATE: 4/26/25
 *
 * DESCRIPTION: Handles generating a cryptographic key from the user's master password
 */

package com.jgptech.Locals.Encryption;

import java.security.SecureRandom;
import javax.crypto.spec.*;

import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;

// REVIEW: should these methods be moved to VaultEncryptor and this class be removed?
public final class KeyHasher {
    // Memory for the Argon2 algorithm to use (KB)
    private static final int MEMORY = 65536;

    // Iterations for the Argon2 algorithm
    private static final int ITERATIONS = 3;

    // Parallelism for the Argon2 algorithm
    private static final int PARALLELISM = 4;

    // Derived key length (bytes)
    private static final int KEY_LENGTH = 32; // 256 bits

    // Length of salts generated (bytes)
    private static final int SALT_LENGTH = 16;


    // Prevent instantiation
    private KeyHasher() {}

    // Hash the master password. Used to derive the symmetric cryptographic key as well as the authentication hash
    public static byte[] deriveKey(String password, byte[] salt) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt).withMemoryAsKB(MEMORY).withIterations(ITERATIONS).withParallelism(PARALLELISM); // REVIEW: might add PIM support in future, if so withIterations() will be 3*PIM

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] key = new byte[KEY_LENGTH];
        generator.generateBytes(password.toCharArray(), key, 0, key.length);
        return key;
    }

    // Generate a random salt to be used in the KDF
    public static byte[] generateSalt() {
        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        rand.nextBytes(salt);
        return salt;
    }
}
