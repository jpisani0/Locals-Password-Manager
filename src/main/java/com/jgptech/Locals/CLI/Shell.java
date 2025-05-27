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
                "exit\n" +
                        "\tclose and exit the vault.\n" +
                "h, help\n" +
                        "\tprint these commands and their function.\n" +
                "l, list\n" +
                        "\tlist all groups in the vault or entries in the group\n" +
                "o, open\n" +
                        "\topen a group in the vault" +
                "s, show\n" +
                        "\tshow the saved data in an entry\n" +
                "a, add\n" +
                        "\tadd a new entry or group to the vault\n" +
                "d, delete\n" +
                        "\tdelete an entry or group from the vault\n" +
                "m, move\n" +
                        "\tmove an entry to another group\n"
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
            // TODO: also allow the user to pass the name of the group to select it
            int newGroupIndex = INVALID_INDEX;

            // Check if the user entered the group number
            try {
                newGroupIndex = Integer.parseInt(words[1]) - 1;

                // Check that this is a valid group index
                if(newGroupIndex < 0 || newGroupIndex > vault.size() - 1) {
                    printErrorMsg("ERROR: " + words[1] + " is not a valid group number. Use 'list groups' to see all group names and numbers.");
                    newGroupIndex = INVALID_INDEX;
                }
            } catch(NumberFormatException e) {
                // Check if the user entered the name of a group
                for(int index = 0; index < vault.size(); index++) {
                    if(words[1].equals(vault.getGroupName(index, key))) {
                        newGroupIndex = index;
                        break;
                    }
                }

                // Check if the name was found in this vault
                if(newGroupIndex == INVALID_INDEX) {
                    printErrorMsg("ERROR: " + words[1] + " is not the name of a group in this vault. Use 'list groups' to see all group names and numbers.");
                }
            }

            // Only update the group index if the entered one was valid
            if(newGroupIndex != INVALID_INDEX) {
                groupIndex = newGroupIndex;
            }
        } else {
            printErrorMsg("ERROR: add the group name or number that you wish to open: 'open <group>'");
        }
    }

    // Show an entry's data
    private void show(String[] words) {
        // Check that the user supplied the second argument
        if(words.length > 1) {
            int entryIndex = INVALID_INDEX;

            // Check if the user entered an entry number
            try {
                entryIndex = Integer.parseInt(words[1]) - 1;

                if(entryIndex < 0 || entryIndex > vault.getGroupSize(groupIndex) - 1) {
                    printErrorMsg("ERROR: " + words[1] + " is not a valid entry number. Use 'list entries' to see all entry names and numbers.");
                    entryIndex = INVALID_INDEX;
                }
            } catch(NumberFormatException e) {
                // Check if the user entered the name of an entry in this group
                for(int index = 0; index < vault.getGroupSize(groupIndex); index++) {
                    if(words[1].equals(vault.getEntryName(groupIndex, index, key))) {
                        entryIndex = index;
                        break;
                    }
                }

                // Check if the name was found in this group
                if(entryIndex == INVALID_INDEX) {
                    printErrorMsg("ERROR: " + words[1] + " is not the name of an entry in this group. Use 'list entries' to see all entry names and numbers.");
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
                System.out.print("Name: ");
                String name = scanner.nextLine();

//                System.out.print("Color: ");
                Color color = Color.RED; // TODO: put switch statement to get color from user, just using red for all for now

                vault.addGroup(name, color, key);
            } else if(isEntrySelected(words[1])) {
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
                int removeGroupIndex = INVALID_INDEX;

                // Check if the user entered the group number
                try {
                    removeGroupIndex = Integer.parseInt(words[2]) - 1;

                    // Check that it is a valid group index
                    if(removeGroupIndex < 0 || removeGroupIndex > vault.size() - 1) {
                        printErrorMsg("ERROR: " + words[2] + " is not a valid group number. Use 'list groups' to show all group names and numbers.");
                        removeGroupIndex = INVALID_INDEX;
                    }
                } catch(NumberFormatException e) {
                    // Check if the user entered the group name
                    for(int index = 0; index < vault.size(); index++) {
                        if(words[2].equals(vault.getGroupName(index, key))) {
                            removeGroupIndex = index;
                            break;
                        }
                    }

                    // Check that the name was found in this vault
                    if(removeGroupIndex == INVALID_INDEX) {
                        printErrorMsg("ERROR: " + words[2] + " is not a valid group name. Use 'list groups' to show all group names and numbers.");
                    }
                }

                // See if we found a valid group index
                if(removeGroupIndex != INVALID_INDEX) {
                    vault.removeGroup(removeGroupIndex);
                }
            } else if(isEntrySelected(words[1])) {
                int removeEntryIndex = INVALID_INDEX;

                // Check if the user entered the entry number
                try {
                    removeEntryIndex = Integer.parseInt(words[2]) - 1;

                    if(removeEntryIndex < 0 || removeEntryIndex > vault.getGroupSize(groupIndex) - 1) {
                        printErrorMsg("ERROR: " + words[2] + " is not a valid entry number. Use 'list entries' to show all entry names and numbers.");
                        removeEntryIndex = INVALID_INDEX;
                    }
                } catch(NumberFormatException e) {
                    // Check if the user entered an entry name
                    for(int index = 0; index < vault.getGroupSize(groupIndex); index++) {
                        if(words[2].equals(vault.getEntryName(groupIndex, index, key))) {
                            removeEntryIndex = index;
                            break;
                        }
                    }

                    // Check if the entry name was found
                    if(removeEntryIndex == INVALID_INDEX) {
                        printErrorMsg("ERROR: " + words[2] + " is not a valid entry name. Use 'list entries' to show all entry names and numbers.");
                    }
                }

                // Check if a valid entry index was found
                if(removeEntryIndex != INVALID_INDEX) {
                    vault.removeEntry(groupIndex, removeEntryIndex);
                }
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
        // TODO
    }

    // Move an entry from one group to another
    private void move(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 3) {
            int entryIndex = INVALID_INDEX;
            int fromGroupIndex = INVALID_INDEX;
            int toGroupIndex = INVALID_INDEX;

            // TODO: add method to move groups in vault
            try {
                entryIndex = Integer.parseInt(words[1]) - 1;

                // Check that this is a valid entry index
                if(entryIndex < 0 || entryIndex > vault.getGroupSize(groupIndex) - 1) {
                    printErrorMsg("ERROR: " + words[1] + " is not a valid entry number. Use 'list entries' to show all entry names and numbers.");
                    entryIndex = INVALID_INDEX;
                }
            } catch(NumberFormatException e) {
                // Check if the user entered an entry name
                for(int index = 0; index < vault.getGroupSize(groupIndex); index++) {
                    if(words[1].equals(vault.getEntryName(groupIndex, index, key))) {
                        entryIndex = index;
                        break;
                    }
                }

                // Check if the name was found
                if(entryIndex == INVALID_INDEX) {
                    printErrorMsg("ERROR: " + words[1] + " is not a valid entry name. Use 'list entries' to show all entry names and numbers.");
                }
            }

            try {
                fromGroupIndex = Integer.parseInt(words[2]) - 1;

                // Check it is a valid group number
                if(fromGroupIndex < 0 || fromGroupIndex > vault.size() - 1) {
                    printErrorMsg("ERROR: " + words[2] + " is not a valid group number. Use 'list group' to show all group names and numbers.");
                    fromGroupIndex = INVALID_INDEX;
                }
            } catch(NumberFormatException e) {
                // Check if the user entered a group name
                for(int index = 0; index < vault.size(); index++) {
                    if(words[2].equals(vault.getGroupName(index, key))) {
                        fromGroupIndex = index;
                        break;
                    }
                }
            }

            try {
                toGroupIndex = Integer.parseInt(words[3]) - 1;

                // Check it is a valid group number
                if(toGroupIndex < 0 || toGroupIndex > vault.size() - 1) {
                    printErrorMsg("ERROR: " + words[3] + " is not a valid group number. Use 'list group' to show all group names and numbers.");
                    toGroupIndex = INVALID_INDEX;
                }
            } catch(NumberFormatException e) {
                // Check if the user entered a group name
                for(int index = 0; index < vault.size(); index++) {
                    if(words[2].equals(vault.getGroupName(index, key))) {
                        toGroupIndex = index;
                        break;
                    }
                }
            }

            if(entryIndex != INVALID_INDEX && fromGroupIndex != INVALID_INDEX && toGroupIndex != INVALID_INDEX) {
                vault.moveEntry(fromGroupIndex, toGroupIndex, entryIndex);
            }
        } else {
            printErrorMsg("ERROR: use 'move <entry-number> <current-group> <next-group>'");
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
