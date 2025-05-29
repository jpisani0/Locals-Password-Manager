/*
 * NAME: Shell
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Shell for viewing and modifying a vault file
 */

package com.jgptech.Locals.CLI;

import com.jgptech.Locals.Encryption.PasswordGenerator;
import com.jgptech.Locals.Vault.Vault;

import javax.crypto.SecretKey;
import java.awt.*;
import java.io.Console;
import java.util.Scanner;

public class Shell {
    // To show that a given index is invalid
    private final int INVALID_INDEX = -1;

    // Flag for when the user wants to exit the program
    private boolean exit = false;

    // User input on command line
    private String input = "";

    // Scanner for user input on command line
    private final Scanner scanner = new Scanner(System.in);

    // Handle for the console
    private final Console console = System.console();

    // The vault that is open
    private final Vault vault;

    // The group that is currently selected
    private int groupIndex;

    // Key for decryption of vault data
    private final SecretKey key;


    // Constructor for the shell class
    public Shell(Vault vault, SecretKey key) {
        this.vault = vault;
        this.key = key;
        this.groupIndex = 0;
    }

    // Start the shell
    public void start() {
        runShell(); // Runs until the user requests to exit
    }

    // Runs the shell
    private void runShell() {
        while(!exit) {
            System.out.print("(" + vault.getName() + " | " + vault.getGroupName(groupIndex, key) + ") >> ");
            input = scanner.nextLine().toLowerCase(); // Wait for the user's input
            String[] words = input.trim().split("\\s+"); // Split the input per word

            try {
                runCommand(words);
            } catch(IndexOutOfBoundsException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // Determine the command the user entered for what to do next
    private void runCommand(String[] words) throws IndexOutOfBoundsException {
        switch(words[0]) {
            case "exit":
            case "quit":
            case "q":
                exit = true;
                break;

            case "":
                // Do nothing if the user entered nothing
                break;

            case "h":
            case "help":
                printHelp();
                break;

            case "l":
            case "list":
                list(words);
                break;

            case "o":
            case "open":
                open(words);
                break;

            case "s":
            case "show":
                show(words);
                break;

            case "a":
            case "add":
                add(words);
                break;

            case "d":
            case "delete":
                delete(words);
                break;

            case "e":
            case "edit":
                edit(words);
                break;

            case "m":
            case "move":
                move(words);
                break;

            default:
                // Unknown command
                System.out.println("ERROR: Unknown command: " + input + ".\n");
                printHelp();
                break;
        }
    }

    // Manual type print out when requested or an invalid command is entered
    private void printHelp() {
        System.out.println(
                """
                
                exit
                    close and exit the vault.
                        [exit|quit]
                h, help
                    print these commands and their function.
                        help
                l, list
                    list all groups in the vault or entries in the group
                        list [groups|entries]
                o, open
                    open a group in the vault
                        open [group]
                s, show
                    show the saved data in an entry
                        show [entry]
                a, add
                    add a new entry or group to the vault
                        add [entry|group]
                d, delete
                    delete an entry or group from the vault
                        delete [entry|group]
                e, edit
                    edit the data in an entry or group
                        edit entry [entry] [name|username|password|URL|notes]
                        edit group [group] [name|color]
                m, move
                    move an entry to another group
                        move [entry] [new-group]
                """
        );
    }

    // Prints out an error message along with the help list
    private void printErrorMsg(String errorMsg) {
        System.out.println(errorMsg);
        printHelp();
    }

    // List the groups in the vault or entries in the vault
    private void list(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 1) {
            // Check if the user wants to list groups or entries
            if(isGroupSelected(words[1])) {
                vault.listGroups(key);
            } else if(isEntrySelected(words[1])) {
                vault.listEntries(groupIndex, key);
            } else {
                printErrorMsg("ERROR: use 'list groups' or 'list entries'");
            }
        } else {
            printErrorMsg("ERROR: use 'list groups' or 'list entries'");
        }
    }

    // Open a group in the vault
    private void open(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 1) {
            openGroup(words[1]);
        } else {
            printErrorMsg("ERROR: add the group name or number that you wish to open: 'open <group>'");
        }
    }

    // Show an entry's data
    private void show(String[] words) {
        // Check that the user supplied the second argument
        if(words.length > 1) {
            showEntry(words[1]);
        } else {
            printErrorMsg("ERROR: add the entry name or number you wish to show: 'show <entry>'");
        }
    }

    // Add a group or entry to the vault
    private void add(String[] words) {
        // Check tha the user supplied the second argument
        if(words.length > 1) {
            // Check if the user wants to add a group or entry
            if(isGroupSelected(words[1])) {
                addGroup();
            } else if(isEntrySelected(words[1])) {
                addEntry();
            } else {
                printErrorMsg("ERROR: use 'add group' or 'add entry'");
            }

            // Write this to the vault to avoid data loss in the event the shell does not close properly
            vault.write();
        } else {
            printErrorMsg("ERROR: use 'add group' or 'add entry'");
        }
    }

    // Remove a group or entry from the vault
    private void delete(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 2) {
            // Check if a user wants to remove a group or entry
            if(isGroupSelected(words[1])) {
                deleteGroup(words[2]);
            } else if(isEntrySelected(words[1])) {
                deleteEntry(words[2]);
            } else {
                printErrorMsg("ERROR: use 'delete group <group-number>' or 'delete entry <entry-number>'");
            }

            // Write this to the vault to avoid data loss in the event the shell does not close properly
            vault.write();
        } else {
            printErrorMsg("ERROR:  use 'delete group <group-number>' or 'delete entry <entry-number>'");
        }
    }

    // Edit data in an entry
    private void edit(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 3) {
            if(isGroupSelected(words[1])) {
                editGroup(words[2], words[3]);
            } else if(isEntrySelected(words[1])) {
                editEntry(words[2], words[3]);
            } else {
                printErrorMsg("ERROR: Use 'edit group <group> <field>' or 'edit entry <entry> <field>'.");
            }
        } else {
            printErrorMsg("ERROR: Use 'edit group <group> <field>' or 'edit entry <entry> <field>'.");
        }
    }

    // Move an entry from one group to another
    private void move(String[] words) {
        // TODO: add method to move groups in vault
        // Check that the user supplied all needed arguments
        if(words.length > 3) {
            if(isGroupSelected(words[1])) {
                moveGroup(words[2], words[3]);
            } else if(isEntrySelected(words[1])) {
                moveEntry(words[2], words[3]);
            } else {
                printErrorMsg("ERROR: use 'move group <group> <new-index>' or 'move entry <entry> <new-group>");
            }
        } else {
            printErrorMsg("ERROR: use 'move group <group> <new-index>' or 'move entry <entry> <new-index>' or 'move entry <entry> <new-group>");
        }
    }

    // Open a group in the vault
    private void openGroup(String word) {
        int newGroupIndex = INVALID_INDEX;

        // Check if the user entered the group number
        try {
            newGroupIndex = Integer.parseInt(word) - 1;

            // Check that this is a valid group index
            if(newGroupIndex < 0 || newGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid group number. Use 'list groups' to see all group names and numbers.");
                newGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the name of a group
            for(int index = 0; index < vault.size(); index++) {
                if(word.equals(vault.getGroupName(index, key).toLowerCase())) {
                    newGroupIndex = index;
                    break;
                }
            }

            // Check if the name was found in this vault
            if(newGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not the name of a group in this vault. Use 'list groups' to see all group names and numbers.");
            }
        }

        // Only update the group index if the entered one was valid
        if(newGroupIndex != INVALID_INDEX) {
            groupIndex = newGroupIndex;
        }
    }

    // Display information from an entry
    private void showEntry(String word) {
        int entryIndex = INVALID_INDEX;

        // Check if the user entered an entry number
        try {
            entryIndex = Integer.parseInt(word) - 1;

            if(entryIndex < 0 || entryIndex > vault.getGroupSize(groupIndex) - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid entry number. Use 'list entries' to see all entry names and numbers.");
                entryIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the name of an entry in this group
            for(int index = 0; index < vault.getGroupSize(groupIndex); index++) {
                if(word.equals(vault.getEntryName(groupIndex, index, key).toLowerCase())) {
                    entryIndex = index;
                    break;
                }
            }

            // Check if the name was found in this group
            if(entryIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not the name of an entry in this group. Use 'list entries' to see all entry names and numbers.");
            }
        }

        // Check that a valid entry index was given
        if(entryIndex != INVALID_INDEX) {
            System.out.println();
            System.out.println("Name: " + vault.getEntryName(groupIndex, entryIndex, key));
            System.out.println("Username: " + vault.getEntryUsername(groupIndex, entryIndex, key));
            System.out.println("Password: " + vault.getEntryPassword(groupIndex,entryIndex, key));
            System.out.println("URL: " + vault.getEntryUrl(groupIndex, entryIndex, key));
            System.out.println("Notes: " + vault.getEntryNotes(groupIndex, entryIndex, key));
            System.out.println();
        }
    }

    // Add a new group to the vault
    private void addGroup() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

//                System.out.print("Color: ");
        Color color = Color.RED; // TODO: put switch statement to get color from user, just using red for all for now

        vault.addGroup(name, color, key);
    }

    // Add an entry to the group
    private void addEntry() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        // See if the user wants to enter their own password or generate a random one
        System.out.print("Do you want to generate a random password? [y/N]: ");
        String input = scanner.nextLine();
        String password = "";

        if(input.equals("n") || input.equals("N") || input.isEmpty()) {
            while(password.isEmpty()) {
                password = new String(console.readPassword("Password: "));

                String verifiedPassword = new String(console.readPassword("Verify password: "));

                if(!password.equals(verifiedPassword)) {
                    System.out.println("Passwords did not match, please try again");
                    password = "";
                }
            }
        } else {
            int length = 0;

            while(length < 1) {
                System.out.print("Length of randomly generated password: ");

                try {
                    length = Integer.parseInt(scanner.nextLine());

                    // REVIEW: 512 good max for password length?
                    // Check that this is a valid length
                    if(length < 1 || length > 512) {
                        throw new NumberFormatException("Out of range");
                    }
                } catch(NumberFormatException e) {
                    printErrorMsg("Please enter a valid length for the password");
                }
            }

            while(password.isEmpty()) {
                password = PasswordGenerator.generatePassword(length);
                System.out.println("Your randomly generated password is: " + password);
                System.out.print("Do you want to regenerate this password? [Y/n]: ");

                input = scanner.nextLine();

                if(input.equals("Y") || input.equals("y") || input.isEmpty()) {
                    password = "";
                }
            }
        }

        System.out.print("URL: ");
        String url = scanner.nextLine();

        System.out.print("Notes: ");
        String notes = scanner.nextLine();

        vault.addEntry(groupIndex, key, name, username, password, url, notes);
    }

    // Delete a group from the vault
    private void deleteGroup(String word) {
        int removeGroupIndex = INVALID_INDEX;

        // Check if the user entered the group number
        try {
            removeGroupIndex = Integer.parseInt(word) - 1;

            // Check that it is a valid group index
            if(removeGroupIndex < 0 || removeGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid group number. Use 'list groups' to show all group names and numbers.");
                removeGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the group name
            for(int index = 0; index < vault.size(); index++) {
                if(word.equals(vault.getGroupName(index, key).toLowerCase())) {
                    removeGroupIndex = index;
                    break;
                }
            }

            // Check that the name was found in this vault
            if(removeGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not a valid group name. Use 'list groups' to show all group names and numbers.");
            }
        }

        // See if we found a valid group index
        if(removeGroupIndex != INVALID_INDEX) {
            vault.removeGroup(removeGroupIndex);
        }
    }

    // Delete an entry from the group
    private void deleteEntry(String word) {
        int removeEntryIndex = INVALID_INDEX;

        // Check if the user entered the entry number
        try {
            removeEntryIndex = Integer.parseInt(word) - 1;

            if(removeEntryIndex < 0 || removeEntryIndex > vault.getGroupSize(groupIndex) - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid entry number. Use 'list entries' to show all entry names and numbers.");
                removeEntryIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered an entry name
            for(int index = 0; index < vault.getGroupSize(groupIndex); index++) {
                if(word.equals(vault.getEntryName(groupIndex, index, key).toLowerCase())) {
                    removeEntryIndex = index;
                    break;
                }
            }

            // Check if the entry name was found
            if(removeEntryIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not a valid entry name. Use 'list entries' to show all entry names and numbers.");
            }
        }

        // Check if a valid entry index was found
        if(removeEntryIndex != INVALID_INDEX) {
            vault.removeEntry(groupIndex, removeEntryIndex);
        }
    }

    // Edit a group in the vault
    private void editGroup(String groupWord, String fieldWord) {
        int editGroupIndex = INVALID_INDEX;

        // Check if the user entered the group number
        try {
            editGroupIndex = Integer.parseInt(groupWord) - 1;

            // Check that this is a valid group index
            if(editGroupIndex < 0 || editGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group index. Use 'list groups' to show all group names and numbers.");
                editGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the group number
            for(int index = 0; index < vault.size(); index++) {
                if(groupWord.equals(vault.getGroupName(index, key).toLowerCase())) {
                    editGroupIndex = index;
                    break;
                }
            }

            // Check if the group index was found
            if(editGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group name. Use 'list groups' to see all group names and numbers.");
            }
        }

        // Only continue if the group index is valid
        if(editGroupIndex != INVALID_INDEX) {
            switch(fieldWord) {
                case "name":
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    vault.setGroupName(editGroupIndex, key, name);
                    break;

                case "color":
                    System.out.println("Color not supported yet!");
                    break;

                default:
                    printErrorMsg("ERROR: " + fieldWord + " is not a valid field.");
                    break;
            }

            vault.write();
        }
    }

    // Edit an entry in the vault
    private void editEntry(String entryWord, String fieldWord) {
        int entryIndex = INVALID_INDEX;

        // Check if the user entered the entry number
        try {
            entryIndex = Integer.parseInt(entryWord) - 1;

            // Check that this is a valid entry index
            if(entryIndex < 0 || entryIndex > vault.getGroupSize(groupIndex) - 1) {
                printErrorMsg("ERROR: " + entryWord + " is not a valid entry index. Use 'list entries' to show all entry names and numbers.");
                entryIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the entry name
            for(int index = 0; index < vault.getGroupSize(groupIndex); index++) {
                if(entryWord.equals(vault.getEntryName(groupIndex, index, key).toLowerCase())) {
                    entryIndex = index;
                    break;
                }
            }

            // Check if the entry index was found
            if(entryIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + entryWord + " is not a valid entry name. Use 'list entries' to show all entry names and numbers.");
            }
        }

        // Only continue if the entry index is valid
        if(entryIndex != INVALID_INDEX) {
            switch(fieldWord) {
                case "name":
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    vault.setEntryName(groupIndex, entryIndex, key, name);
                    break;

                case "username":
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    vault.setEntryUsername(groupIndex, entryIndex, key, username);
                    break;

                case "password":
                    String password = new String(console.readPassword("Password: "));
                    vault.setEntryPassword(groupIndex, entryIndex, key, password);
                    break;

                case "url":
                    System.out.print("URL: ");
                    String url = scanner.nextLine();
                    vault.setEntryUrl(groupIndex, entryIndex, key, url);
                    break;

                case "notes":
                    System.out.print("Notes: ");
                    String notes = scanner.nextLine();
                    vault.setEntryNotes(groupIndex, entryIndex, key, notes);
                    break;

                default:
                    printErrorMsg("ERROR: " + fieldWord + " is not a valid element of an entry to edit.");
                    break;
            }

            vault.write();
        }
    }

    // Move a group to a new index in the vault
    private void moveGroup(String groupWord, String indexWord) {
        int selectedGroupIndex = INVALID_INDEX;
        int newIndex = INVALID_INDEX;

        // Check if the user entered a group number
        try {
            selectedGroupIndex = Integer.parseInt(groupWord) - 1;

            // Check that this is a valid group index
            if(selectedGroupIndex < 0 || selectedGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group number. Use 'list groups' to show all group names and numbers.");
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the group name
            for(int index = 0; index < vault.size(); index++) {
                if(groupWord.equals(vault.getGroupName(index, key).toLowerCase())) {
                    selectedGroupIndex = groupIndex;
                    break;
                }
            }

            // Check if the group name was found
            if(selectedGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group name. Use 'list groups' to show all group names and numbers.");
            }
        }

        // Only continue if a valid group index was entered
        if(selectedGroupIndex != INVALID_INDEX) {
            // Get the new index to move it to
            try {
                newIndex = Integer.parseInt(indexWord);

                // Check that this is a valid group index
                if(newIndex < 0 || newIndex > vault.size()) {
                    printErrorMsg("ERROR: " + indexWord + " is not a valid index to place this group.");
                    newIndex = INVALID_INDEX;
                }
            } catch(NumberFormatException e) {
                printErrorMsg("ERROR: " + indexWord + " is not a valid index to place this group.");
            }

            // Only continue if a valid new index was found
            if(newIndex != INVALID_INDEX) {
                vault.moveGroup(selectedGroupIndex, newIndex);

                // If the move group was the one we are currently in, also change to that group index
                if(selectedGroupIndex == groupIndex) {
                    groupIndex = newIndex;
                }
            }
        }
    }

    // Move an entry from one group to another
    private void moveEntry(String entryWord, String groupWord) {
        int entryIndex = INVALID_INDEX;
        int toGroupIndex = INVALID_INDEX;

        try {
            entryIndex = Integer.parseInt(entryWord) - 1;

            // Check that this is a valid entry index
            if(entryIndex < 0 || entryIndex > vault.getGroupSize(groupIndex) - 1) {
                printErrorMsg("ERROR: " + entryWord + " is not a valid entry number. Use 'list entries' to show all entry names and numbers.");
                entryIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered an entry name
            for(int index = 0; index < vault.getGroupSize(groupIndex); index++) {
                if(entryWord.equals(vault.getEntryName(groupIndex, index, key).toLowerCase())) {
                    entryIndex = index;
                    break;
                }
            }

            // Check if the name was found
            if(entryIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + entryWord + " is not a valid entry name. Use 'list entries' to show all entry names and numbers.");
            }
        }

        try {
            toGroupIndex = Integer.parseInt(groupWord) - 1;

            // Check it is a valid group number
            if(toGroupIndex < 0 || toGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group number. Use 'list group' to show all group names and numbers.");
                toGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered a group name
            for(int index = 0; index < vault.size(); index++) {
                if(groupWord.equals(vault.getGroupName(index, key).toLowerCase())) {
                    toGroupIndex = index;
                    break;
                }
            }
        }

        if(entryIndex != INVALID_INDEX && toGroupIndex != INVALID_INDEX) {
            vault.moveEntry(groupIndex, toGroupIndex, entryIndex);
        }
    }

    // Returns true if the given word is one of the aliases for selecting a group
    private boolean isGroupSelected(String word) {
        return (word.equals("group") || word.equals("groups") || word.equals("g"));
    }

    // Returns true if the given word is one of the aliases for selecting an entry
    private boolean isEntrySelected(String word) {
        return (word.equals("entry") || word.equals("entries") || word.equals("e"));
    }
}
