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

    // Constructor for a new entry
    public Login(String name, byte[] iv, String username, String password, String url, String notes, byte[] key) {
        this.name = VaultEncryptor.encrypt(name, key, iv);
        this.iv = Base64.getEncoder().encodeToString(iv);
        this.username = VaultEncryptor.encrypt(username, key, iv);
        this.password = VaultEncryptor.encrypt(password, key, iv);
        this.url = VaultEncryptor.encrypt(url, key, iv);
        this.notes = VaultEncryptor.encrypt(notes, key, iv);
    }

    // Get the username for this entry
    public String getUsername(byte[] key) {
        return VaultEncryptor.decrypt(username, key, getIV());
    }

    // Set the username for this entry
    public void setUsername(String username, byte[] key) {
        this.username = VaultEncryptor.encrypt(username, key, getIV());
    }

    // Get the password for this login
    public String getPassword(byte[] key) {
        return VaultEncryptor.decrypt(password, key, getIV());
    }

    // Set the password for this entry
    public void setPassword(String password, byte[] key) {
        this.password = VaultEncryptor.encrypt(password, key, getIV());
    }

    // Get the URL for this entry
    public String getUrl(byte[] key) {
        return VaultEncryptor.decrypt(url, key, getIV());
    }

    // Set the URL for this entry
    public void setUrl(String url, byte[] key) {
        this.url = VaultEncryptor.encrypt(url, key, getIV());
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
