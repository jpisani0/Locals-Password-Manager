/*
 * NAME: Group
 * AUTHOR: J. Pisani
 * DATE: 10/15/24
 *
 * DESCRIPTION: Class to encapsulate and organize entries into separate groups.
 */

package Vault;

import java.awt.*;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Group {
    // Name of the group
    private String name;

    // Color for the group
    private Color color;

    // Array list to hold entries in this group
    private ArrayList<Entry> entries = new ArrayList<>();


    // Constructor for loading a group from an existing vault file (Jackson requires an empty constructor)
    public Group() {}

    // Constructor for a new group
    public Group(String name, Color color /*Image groupImage*/) {
        this.name = name;
        this.color = color;
//        this.groupImage = groupImage;
    }

    // Get the name of this group
    public String getName() {
        return this.name;
    }

    // Set the name of this group
    public void setName(String name) {
        this.name = name;
    }

    // Get the color of the group
    public Color getColor() {
        return this.color;
    }

    // Set the color of this group
    public void setColor(Color color) {
        this.color = color;
    }

    // Get an entry from this group
    public Entry getEntry(int entryIndex) {
        // Check that requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entries.size()) {
            return null;
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
    public void listEntries() {
        // REVIEW: need this if? or will work same if removed? better coding practice to leave it anyways?
        if(!entries.isEmpty()) {
            for (Entry entry : entries) {
                System.out.println(entry.getName());
            }
        }
    }

    // Check if this group is empty
    public boolean isEmpty() {
        return entries.isEmpty();
    }
}
