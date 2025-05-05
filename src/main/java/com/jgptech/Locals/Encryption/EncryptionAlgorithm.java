/*
 * NAME: EncryptionAlgorithms
 * DATE: 4/26/25
 * AUTHOR: J. Pisani
 *
 * DESCRIPTION: Enum for encryption algorithms used in the password manager
 */

package com.jgptech.Locals.Encryption;

public enum EncryptionAlgorithm {
    NoEncryptionAlgorithm(0),
    AES(1);

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

    // TODO: add more encryption algorithms
    // Return the string to be used in the Cipher class from the enum
    public String toCipherString() {
        return switch(this) {
            case AES -> "AES";
            default -> "";
        };
    }
}
