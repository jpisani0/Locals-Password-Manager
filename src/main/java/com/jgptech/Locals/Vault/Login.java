/*
 * NAME: Entry
 * AUTHOR:  J. Pisani
 * DATE: 10/11/24
 *
 * DESCRIPTION: Class to hold data for a login entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.VaultEncryptor;

import javax.crypto.SecretKey;

@JsonTypeName("login")
public class Login extends Entry {
    // Username for the account login
    private String username;

    // Password for the account login
    private String password;

    // URL or website for this entry
    private String url;

    // REVIEW: should fill any variable that has the password in it with garbage data or 0's after they are done being used to clear them from memory

    // Constructor for a new entry
    public Login(String name, String username, String password, String url, String notes, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
        this.username = VaultEncryptor.encrypt(username, key, encryptionAlgorithm);
        this.password = VaultEncryptor.encrypt(password, key, encryptionAlgorithm);
        this.url = VaultEncryptor.encrypt(url, key, encryptionAlgorithm);
        this.notes = VaultEncryptor.encrypt(notes, key, encryptionAlgorithm);
    }

    // Get the username for this entry
    public String getUsername(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(username, key, encryptionAlgorithm);
    }

    // Set the username for this entry
    public void setUsername(String username, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.username = VaultEncryptor.encrypt(username, key, encryptionAlgorithm);
    }

    // Get the password for this login
    public String getPassword(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(password, key, encryptionAlgorithm);
    }

    // Set the password for this entry
    public void setPassword(String password, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.password = VaultEncryptor.encrypt(password, key, encryptionAlgorithm);
    }

    // Get the URL for this entry
    public String getUrl(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(url, key, encryptionAlgorithm);
    }

    // Set the URL for this entry
    public void setUrl(String url, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.url = VaultEncryptor.encrypt(url, key, encryptionAlgorithm);
    }

    // Print the relevant details for this entry
    public void print() {
        System.out.println();
        System.out.println("Name: " + name);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password); // REVIEW: add way to print without the password showing, like '******'
        System.out.println("URL: " + url);
        System.out.println("Notes: " + notes);
        System.out.println();
    }
}
