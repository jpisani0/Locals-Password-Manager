/*
 * NAME: com.jgptech.locals.Entry
 * AUTHOR:  J. Pisani
 * DATE: 10/11/24
 *
 * DESCRIPTION: Class to hold data for reading/writing entry information from/to password files
 */

package com.jgptech.locals.Entry;

public class Entry {
    private String entryName;  // Name or title of the entry
    private String username;  // Username for the account login
    private String password;  // Password for the account login
    private String url;  // URL or website for this entry
    private String notes;  // Notes written by the user for the entry

    // Constructor for setting up the list of entries, starting with the first in the password file
    public Entry(String entryName, String username, String password, String url, String notes) {
        // TODO: set up data from file methods when implemented
        this.entryName = entryName;
        this.username = username;
        this.password = password;
        this.url = url;
        this.notes = notes;
    }

    // REVIEW: setters might not be needed
    public String getEntryName() {
        return this.entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
