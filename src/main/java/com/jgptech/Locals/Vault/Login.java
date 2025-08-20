/*
 * NAME: Entry
 * AUTHOR:  J. Pisani
 * DATE: 10/11/24
 *
 * DESCRIPTION: Class to hold data for a login entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jgptech.Locals.Encryption.VaultEncryptor;

import java.util.Base64;

@JsonTypeName("login")
public class Login extends Entry {
    // Username for the account login
    private String username;

    // Password for the account login
    private String password;

    // URL or website for this entry
    private String url;

    // REVIEW: should fill any variable that has the password in it with garbage data or 0's after they are done being used to clear them from memory

    // Empty constructor for Jackson
    Login() {}

    // Constructor for a new entry
    public Login(String name, String username, String password, String url, String notes, byte[] key) {
        this.name = VaultEncryptor.encrypt(name, key);
        this.username = VaultEncryptor.encrypt(username, key);
        this.password = VaultEncryptor.encrypt(password, key);
        this.url = VaultEncryptor.encrypt(url, key);
        this.notes = VaultEncryptor.encrypt(notes, key);
    }

    // Get the username for this entry
    public String getUsername(byte[] key) {
        return VaultEncryptor.decrypt(username, key);
    }

    // Set the username for this entry
    public void setUsername(String username, byte[] key) {
        this.username = VaultEncryptor.encrypt(username, key);
    }

    // Get the password for this login
    public String getPassword(byte[] key) {
        return VaultEncryptor.decrypt(password, key);
    }

    // Set the password for this entry
    public void setPassword(String password, byte[] key) {
        this.password = VaultEncryptor.encrypt(password, key);
    }

    // Get the URL for this entry
    public String getUrl(byte[] key) {
        return VaultEncryptor.decrypt(url, key);
    }

    // Set the URL for this entry
    public void setUrl(String url, byte[] key) {
        this.url = VaultEncryptor.encrypt(url, key);
    }

    // Print the relevant details for this entry
    public void print(byte[] key) {
        System.out.println();
        System.out.println("Name: " + getName(key));
        System.out.println("Username: " + getUsername(key));
        System.out.println("Password: " + getPassword(key)); // REVIEW: add way to print without the password showing, like '******'
        System.out.println("URL: " + getUrl(key));
        System.out.println("Notes: " + getNotes(key));
        System.out.println();
    }
}
