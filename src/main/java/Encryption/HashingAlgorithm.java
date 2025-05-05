/*
 * NAME: HashingAlgorithms
 * DATE: 4/26/25
 * AUTHOR: J. Pisani
 *
 * DESCRIPTION: Enum for hashing algorithms used in the password manager
 */

package Encryption;

public enum HashingAlgorithm {
    NoHashingAlgorithm(0),
    PBKDF2(1),
    Argon2(2);

    // Value of the enum
    private final int value;

    // Construct for HashingAlgorithm
    private HashingAlgorithm(int value) {
        this.value = value;
    }

    // Get the value of a HashingAlgorithm variable
    public int getValue() {
        return value;
    }

    // Get the correct enum based on the value
    public static HashingAlgorithm fromValue(int value) throws IllegalArgumentException {
        for(HashingAlgorithm algorithm : HashingAlgorithm.values()) {
            if(algorithm.getValue() == value) {
                return algorithm;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }

    // TODO: add more hashing algorithms
    // Return the string to be used in the hasher classes from the enum
    public String toHasherString() {
        return switch (this) {
            case PBKDF2 -> "PBKDF2WithHmacSHA256";
            case Argon2 -> ""; // TODO
            default -> "";
        };
    }
}
