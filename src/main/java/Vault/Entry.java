/*
 * NAME: Entry
 * AUTHOR:  J. Pisani
 * DATE: 10/11/24
 *
 * DESCRIPTION: Class to hold data for reading/writing entry information from/to password files
 */

package Vault;

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

    // REVIEW: needed?
    // Constructor for an empty entry
    public Entry() {
        this.entryName = null;
        this.username = null;
        this.password = null;
        this.url = null;
        this.notes = null;
    }

    // Get the name of this entry
    public String getEntryName() {
        return this.entryName;
    }

    // Set the name of this entry
    public void setEntryName(String entryName) {
        this.entryName = entryName;
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
