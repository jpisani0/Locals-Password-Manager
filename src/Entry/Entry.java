/*
 * NAME: Entry
 * AUTHOR:  J. Pisani
 * DATE: 10/11/24
 *
 * DESCRIPTION: Class to hold data for reading/writing entry information from/to password files
 */

package Entry;

public class Entry {
    // Entry Data from password file
    private String entryName = "";
    private String username = "";
    private String password = "";

    // Previous and next entries in list
    private Entry previousEntry = null;
    private Entry nextEntry = null;

    // Entry specific data
    private int entryNum = 0;
    private static int totalNumEntries = 0;

    // Constructor for setting up the list of entries, starting with the first in the password file
    public Entry() {
        // TODO: set up data from file methods when implemented

        this.entryNum = 1;
        totalNumEntries++;

        // TODO: add way to check if this is the last entry before adding another, prob method from file class checking EOF or some other indicator
        this.nextEntry = new Entry(this);
    }

    // Constructor for the next entry in the list until the last entry in the list
    private Entry(Entry previousEntry) {
        // TODO: setup data from file methods when implemented

        this.entryNum = ++totalNumEntries;
        this.previousEntry = previousEntry;

        // TODO: add way to check if this is the last entry before adding another, prob method from file class checking EOF or some other indicator
        this.nextEntry = new Entry(this);
    }

    public String EntryName() {
        return this.entryName;
    }

    public String Username() {
        return this.username;
    }

    public String Password() {
        return this.password;
    }

    public Entry PreviousEntry() {
        return this.previousEntry;
    }

    public Entry NextEntry() {
        return this.nextEntry;
    }

    public int EntryNum() {
        return this.entryNum;
    }

    public static int TotalNumEntries() {
        return totalNumEntries;
    }

    public boolean isFirstEntry() {
        return this.entryNum == 1;
    }

    public boolean isLastEntry() {
        return this.entryNum == totalNumEntries;
    }
}
