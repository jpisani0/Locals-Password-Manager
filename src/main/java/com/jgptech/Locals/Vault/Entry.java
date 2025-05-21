/*
 * NAME: Entry
 * AUTHOR:  J. Pisani
 * DATE: 10/11/24
 *
 * DESCRIPTION: Class to hold data for reading/writing entry information from/to password files
 */

package com.jgptech.Locals.Vault;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.VaultEncryptor;

import javax.crypto.SecretKey;

class Entry {
    // Name or title of the entry
    private String name;

    // Username for the account login
    private String username;

    // Password for the account login
    private String password;

    // URL or website for this entry
    private String url;

    // Notes written by the user for the entry
    private String notes;


    // Constructor for loading an existing entry from a vault file (Jackson requires an empty constructor)
    Entry() {}

    // Constructor for a new entry
    Entry(String name, String username, String password, String url, String notes, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
        this.username = VaultEncryptor.encrypt(username, key, encryptionAlgorithm);
        this.password = VaultEncryptor.encrypt(password, key, encryptionAlgorithm);
        this.url = VaultEncryptor.encrypt(url, key, encryptionAlgorithm);
        this.notes = VaultEncryptor.encrypt(notes, key, encryptionAlgorithm);
    }

    // Get the name of this entry
    String getName(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(name, key, encryptionAlgorithm);
    }

    // Set the name of this entry
    void setName(String name, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
    }

    // Get the username for this entry
    String getUsername(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(username, key, encryptionAlgorithm);
    }

    // Set the username for this entry
    void setUsername(String username, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.username = VaultEncryptor.encrypt(username, key, encryptionAlgorithm);
    }

    // Get the password for this entry
    String getPassword(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(password, key, encryptionAlgorithm);
    }

    // Set the password for this entry
    void setPassword(String password, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.password = VaultEncryptor.encrypt(password, key, encryptionAlgorithm);
    }

    // Get the URL for this entry
    String getUrl(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(url, key, encryptionAlgorithm);
    }

    // Set the URL for this entry
    void setUrl(String url, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.url = VaultEncryptor.encrypt(url, key, encryptionAlgorithm);
    }

    // Get the notes for this entry
    String getNotes(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(notes, key, encryptionAlgorithm);
    }

    // Set the notes for this entry
    void setNotes(String notes, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.notes = VaultEncryptor.encrypt(notes, key, encryptionAlgorithm);
    }
}
