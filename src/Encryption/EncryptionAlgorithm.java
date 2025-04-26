/*
 * NAME: EncryptionAlgorithms
 * DATE: 4/26/25
 * AUTHOR: J. Pisani
 *
 * DESCRIPTION: Enum for encryption algorithms used in the password manager
 */

package Encryption;

public enum EncryptionAlgorithm {
    AES_256(1),   // Assigning a number to AES_256
    CHACHA20(2),  // Assigning a number to CHACHA20
    DES(3);       // Assigning a number to DES

    private final int value;

    // Constructor for EncryptionAlgorithm
    private EncryptionAlgorithm(int value) {
        this.value = value;
    }

    // Gets the value of the EncryptionAlgorithm variable
    public int getValue() {
        return value;
    }

    // Get the correct enum based on the value
    public static EncryptionAlgorithm fromValue(int value) {
        for (EncryptionAlgorithm algorithm : EncryptionAlgorithm.values()) {
            if (algorithm.getValue() == value) {
                return algorithm;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
