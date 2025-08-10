/*
 * NAME: SecureNote
 * AUTHOR: J. Pisani
 * DATE: 7/15/25
 *
 * DESCRIPTION: Class to hold data for a secure note entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.jgptech.Locals.Encryption.VaultEncryptor;

@JsonTypeName("secureNote")
public class SecureNote extends Entry {
    // The SecureNote class is almost empty as it only contains what the Entry class has: a name, salt, and note.
    // It is still given its own class and file since Entry is meant to be an abstract class that cannot be called
    // directly and for better organization of our data.

    // Empty constructor for Jackson
    SecureNote() {}

    public SecureNote(String name, String notes, byte[] key) {
        this.name = VaultEncryptor.encrypt(name, key, getIV());
        this.notes = VaultEncryptor.encrypt(notes, key, getIV());
    }

    // Print the relevant details for this secure note
    public void print() {
        System.out.println();
        System.out.println("Name: " + name);
        System.out.println("Notes: " + notes);
        System.out.println();
    }
}
