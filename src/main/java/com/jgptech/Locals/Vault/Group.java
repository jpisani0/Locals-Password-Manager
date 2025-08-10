/*
 * NAME: Group
 * AUTHOR: J. Pisani
 * DATE: 10/15/24
 *
 * DESCRIPTION: Class to encapsulate and organize entries into separate groups.
 */

package com.jgptech.Locals.Vault;

import com.jgptech.Locals.Encryption.VaultEncryptor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Group {
    @JsonIgnore
    // Shows that a given index is invalid
    private final int INVALID_INDEX = -1; // TODO: need to make this something "global" for the project

    // Name of the group
    private String name;

    // The initialization vector of this group. Base64 encoded.
    private String iv;

//    // Color for the group
//    private Color color;

    // TODO: add predefined list of icons to identify the group as well

    // Array list to hold entries in this group
    private ArrayList<Entry> entries = new ArrayList<>();


    // Constructor for loading a group from an existing vault file (Jackson requires an empty constructor)
    Group() {}

    // Constructor for a new group
    public Group(String name, Color color, byte[] key /*Image groupImage*/) {
        this.iv = Base64.getEncoder().encodeToString(VaultEncryptor.generateIV());
        this.name = VaultEncryptor.encrypt(name, key, getIV());
//        this.color = color;
    }

    // Get the name of this group
    public String getName(byte[] key) {
        return VaultEncryptor.decrypt(name, key, getIV());
    }

    // Set the name of this group
    public void setName(String name, byte[] key) {
        this.name = VaultEncryptor.encrypt(name, key, getIV());
    }

    // Get the IV of this group
    public byte[] getIV() {
        return Base64.getDecoder().decode(iv);
    }

    // Set the IV of this group
    public void setIV(byte[] iv) {
        this.iv = Base64.getEncoder().encodeToString(iv);
    }

//    // Get the color of the group
//    public Color getColor() {
//        return this.color;
//    }

//    // Set the color of this group
//    public void setColor(Color color) {
//        this.color = color;
//    }

    // Get the amount of entries in this group
    public int size() {
        return entries.size();
    }

    @JsonIgnore
    // Check if this group is empty
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /****************************************************************************************************************/
    /***************************************************** ENTRY ****************************************************/
    /****************************************************************************************************************/

    // Get the entries array (for Jackson)
    public ArrayList<Entry> getEntries() {
        return entries;
    }

    // Set the entries array (for Jackson)
    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    @JsonIgnore
    // Get an entry from this group
    public Entry getEntry(int entryIndex) throws IndexOutOfBoundsException {
        if(entryIndex < 0 || entryIndex > entries.size()) {
            throw new IndexOutOfBoundsException("Invalid entry index: " + entryIndex);
        }

        return entries.get(entryIndex);
    }

    // REVIEW: needed?
    // Add an entry to the end of this group
    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    // Add an entry at a specific index of this group
    public boolean addEntry(Entry entry, int entryIndex) {
        // Check that the requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entries.size()) {
            return false;
        }

        entries.add(entryIndex, entry);
        return true;
    }

    // Remove an entry from this group
    public boolean removeEntry(int entryIndex) {
        // Check that the requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entries.size()) {
            return false;
        }

        entries.remove(entryIndex);
        return true;
    }

    // Move an existing entry to another index of this group
    public boolean moveEntry(int currentEntryIndex, int newEntryIndex) {
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
    public void listEntries(byte[] key) {
        // REVIEW: need this if? or will work same if removed? better coding practice to leave it anyways?
        if(!entries.isEmpty()) {
            System.out.println();

            for (int index = 0; index < entries.size(); index++) {
                System.out.println((index + 1) + ". " + entries.get(index).getName(key));
            }

            System.out.println();
        }
    }

    // Check if a given entry index is valid for this group
    public int isValidEntryIndex(String entryWord, byte[] key) {
        int entryIndex = -1; // REVIEW: make INVALID INDEX accessible from somewhere across this package

        // Check if the user entered the entry number
        try {
            entryIndex = Integer.parseInt(entryWord) - 1;

            // Check that this is a valid entry index
            if(entryIndex < 0 || entryIndex > size() - 1) {
                entryIndex = INVALID_INDEX;
            }
        } catch (NumberFormatException e) {
            // Check if the user entered the entry name
            for(int index = 0; index < size(); index++) {
                if(entryWord.equals(getEntry(index).getName(key).toLowerCase())) {
                    entryIndex = index;
                    break;
                }
            }
        }

        return entryIndex;
    }
}
