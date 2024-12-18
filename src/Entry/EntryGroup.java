/*
 * NAME: EntryGroup
 * AUTHOR: J. Pisani
 * DATE: 10/15/24
 *
 * DESCRIPTION: Class to encapsulate and organize entries into separate groups.
 */

package Entry;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class EntryGroup {
    private String groupName;  // Name of the group
    private Image groupImage;  // Image for the group

    // REVIEW: after researching, this seems to be better than using/creating a custom linked list system, should look into further
    private ArrayList<Entry> entryArray = new ArrayList<>();  // Array list to hold entries in this group

    public EntryGroup(String groupName, Image groupImage) {
        this.groupName = groupName;
        this.groupImage = groupImage;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Image getGroupImage() {
        return this.groupImage;
    }

    public void setGroupImage(Image groupImage) {
        this.groupImage = groupImage;
    }

    public Entry getEntry(int entryIndex) {
        // Check that requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entryArray.size()) {
            return null;
        } else {
            return entryArray.get(entryIndex);
        }
    }

    // REVIEW: needed?
    public void addEntry(@NotNull Entry entry) {
        entryArray.add(entry);
    }

    public boolean addEntry(int entryIndex, @NotNull Entry entry) {
        // Check that the requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entryArray.size()) {
            return false;
        } else {
            entryArray.add(entryIndex, entry);
            return true;
        }
    }

    public boolean removeEntry(int entryIndex) {
        // Check that the requested index is not outside bounds of array
        if(entryIndex < 0 || entryIndex > entryArray.size()) {
            return false;
        } else {
            entryArray.remove(entryIndex);
            return true;
        }
    }

    public boolean moveEntry(int currentEntryIndex, int newEntryIndex) {
        // Check that both indices are within bounds of array
        if(currentEntryIndex < 0 || currentEntryIndex > entryArray.size() ||
           newEntryIndex < 0 || newEntryIndex > entryArray.size()) {
            return false;
        } else {
            Entry bufferEntry = entryArray.get(currentEntryIndex);
            entryArray.remove(currentEntryIndex);
            entryArray.add(newEntryIndex, bufferEntry);
            return true;
        }
    }

    public void listEntries() {
        // REVIEW: need this if? or will work same if removed? better coding practice to leave it anyways?
        if(!entryArray.isEmpty()) {
            for (Entry entry : entryArray) {
                System.out.println(entry.getEntryName());
            }
        }
    }

    public boolean isEmpty() {
        return entryArray.isEmpty();
    }
}
