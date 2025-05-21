/*
 * NAME: Group
 * AUTHOR: J. Pisani
 * DATE: 10/15/24
 *
 * DESCRIPTION: Class to encapsulate and organize entries into separate groups.
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.VaultEncryptor;

import javax.crypto.SecretKey;
import java.awt.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

class Group {
    // Name of the group
    private String name;

    // Color for the group
    private Color color;

    // TODO: add predefined list of icons to identify the group as well

    // Array list to hold entries in this group
    private ArrayList<Entry> entries = new ArrayList<>();


    // Constructor for loading a group from an existing vault file (Jackson requires an empty constructor)
    Group() {}

    // Constructor for a new group
    Group(String name, Color color, SecretKey key, EncryptionAlgorithm encryptionAlgorithm /*Image groupImage*/) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
        this.color = color;
    }

    // Get the name of this group
    String getName(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(name, key, encryptionAlgorithm);
    }

    // Set the name of this group
    void setName(String name, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
    }

    // Get the color of the group
    Color getColor() {
        return this.color;
    }

    // Set the color of this group
    void setColor(Color color) {
        this.color = color;
    }

    // Check if this group is empty
    boolean isEmpty() {
        return entries.isEmpty();
    }

    @JsonIgnore
    // Get the name of an entry
    String getEntryName(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) throws IndexOutOfBoundsException {
        // Check that requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        return entries.get(entryIndex).getName(key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the name of an entry in this group
    void setEntryName(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm, String name) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        entries.get(entryIndex).setName(name, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Get the username of an entry in this group
    String getEntryUsername(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        return entries.get(entryIndex).getUsername(key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the username of an entry in this group
    void setEntryUsername(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm, String username) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        entries.get(entryIndex).setUsername(username, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Get the password of an entry in this group
    String getEntryPassword(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        return entries.get(entryIndex).getPassword(key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the password if an entry in this group
    void setEntryPassword(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm, String password) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        entries.get(entryIndex).setPassword(password, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Get the URL of an entry in this group
    String getEntryUrl(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        return entries.get(entryIndex).getUrl(key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the URL of an entry in this group
    void setEntryUrl(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm, String url) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        entries.get(entryIndex).setUrl(url, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Get the notes of an entry in this group
    String getEntryNotes(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        return entries.get(entryIndex).getNotes(key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the notes of an entry in this group
    void setEntryNotes(int entryIndex, SecretKey key, EncryptionAlgorithm encryptionAlgorithm, String notes) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        entries.get(entryIndex).setNotes(notes, key, encryptionAlgorithm);
    }

    // REVIEW: needed?
    // Add an entry to the end of this group
    void addEntry(Entry entry) {
        entries.add(entry);
    }

    // Add an entry at a specific index of this group
    boolean addEntry(Entry entry, int entryIndex) {
        // Check that the requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entries.size()) {
            return false;
        }

        entries.add(entryIndex, entry);
        return true;
    }

    // Remove an entry from this group
    boolean removeEntry(int entryIndex) {
        // Check that the requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entries.size()) {
            return false;
        }

        entries.remove(entryIndex);
        return true;
    }

    // Move an existing entry to another index of this group
    boolean moveEntry(int currentEntryIndex, int newEntryIndex) {
        // Check that both indices are within bounds of array
        if(currentEntryIndex < 0 || currentEntryIndex > entries.size() ||
           newEntryIndex < 0 || newEntryIndex > entries.size()) {
            return false;
        }

        Entry bufferEntry = entries.get(currentEntryIndex);
        entries.remove(currentEntryIndex);
        entries.add(newEntryIndex, bufferEntry);
        return true;
    }

    // List all the entries in this group
    void listEntries(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        // REVIEW: need this if? or will work same if removed? better coding practice to leave it anyways?
        if(!entries.isEmpty()) {
            for (Entry entry : entries) {
                System.out.println(entry.getName(key, encryptionAlgorithm));
            }
        }
    }
}
