/*
 * NAME: Group
 * AUTHOR: J. Pisani
 * DATE: 10/15/24
 *
 * DESCRIPTION: Class to encapsulate and organize entries into separate groups.
 */

package Vault;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class Group {
    private String groupName;  // Name of the group
    private Image groupImage;  // Image for the group
    private ArrayList<Entry> entries = new ArrayList<>();  // Array list to hold entries in this group

    // Constructor for a group
    public Group(String groupName, Image groupImage) {
        this.groupName = groupName;
        this.groupImage = groupImage;
    }

    // Get the name of this group
    public String getGroupName() {
        return this.groupName;
    }

    // Set the name of this group
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // Get the image of this group
    public Image getGroupImage() {
        return this.groupImage;
    }

    // Set the image of this group
    public void setGroupImage(Image groupImage) {
        this.groupImage = groupImage;
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
    public void addEntry(@NotNull Entry entry) {
        entries.add(entry);
    }

    // Add an entry at a specific index of this group
    public boolean addEntry(@NotNull Entry entry, int entryIndex) {
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
                System.out.println(entry.getEntryName());
            }
        }
    }

    // Check if this group is empty
    public boolean isEmpty() {
        return entries.isEmpty();
    }
}
