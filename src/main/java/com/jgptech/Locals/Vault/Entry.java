/*
 * NAME: Entry
 * AUTHOR:  J. Pisani
 * DATE: 10/11/24
 *
 * DESCRIPTION: Class to hold data for reading/writing entry information from/to password files
 */

package com.jgptech.Locals.Vault;

public class Entry {
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
    public Entry() {}

    // Constructor for a new entry
    public Entry(String name, String username, String password, String url, String notes) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.url = url;
        this.notes = notes;
    }

    // Get the name of this entry
    public String getName() {
        return this.name;
    }

    // Set the name of this entry
    public void setName(String name) {
        this.name = name;
    }

    // Get the username for this entry
    public String getUsername() {
        return this.username;
    }

    // Set the username for this entry
    public void setUsername(String username) {
        this.username = username;
    }

    // Get the password for this entry
    public String getPassword() {
        return this.password;
    }

    // Set the password for this entry
    public void setPassword(String password) {
        this.password = password;
    }

    // Get the URL for this entry
    public String getUrl() {
        return this.url;
    }

    // Set the URL for this entry
    public void setUrl(String url) {
        this.url = url;
    }

    // Get the notes for this entry
    public String getNotes() {
        return this.notes;
    }

    // Set the notes for this entry
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
