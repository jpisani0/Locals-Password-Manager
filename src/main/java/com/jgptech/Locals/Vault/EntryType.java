/*
 * NAME: EntryType
 * AUTHOR: J. Pisani
 * DATE: 7/15/25
 *
 * DESCRIPTION: Enum to denote which type each of child of the Entry Interface is for saving to and loading from vault
 */

package com.jgptech.Locals.Vault;

enum EntryType {
    Login(0),
    sshToken(1),
    secureNote(2),
    paymentCard(3);

    private final int value;

    // Constructor for EntryType
    private EntryType(int value) {
        this.value = value;
    }

    // Gets the value of the EntryType
    public int getValue() {
        return value;
    }

    // Get the correct entry type based on the value
    public static EntryType fromValue(int value) {
        for (EntryType type: EntryType.values()) {
            if(type.getValue() == value) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }

    /* --------------------------------------------------------------------------------------------------------- */
    /* These strings MUST be kept in line with the @JsonTypeName annotations at the top of each child class file */
    /* --------------------------------------------------------------------------------------------------------- */

    // Return the entry type in string format
    public String toString() {
        return switch(this) {
            case Login -> "Login";
            case sshToken -> "sshKey";
            case secureNote -> "secureNote";
            case paymentCard -> "paymentCard";
        };
    }

    // Get the enum from a string
    public EntryType fromString(String type) {
        return switch(type) {
            case "Login" -> Login;
            case "sshKey" -> sshToken;
            case "secureNote" -> secureNote;
            case "paymentCard" -> paymentCard;
            default -> throw new IllegalArgumentException("Unknown entry type: " + type);
        };
    }
}
