/*
 * NAME: Shell
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Shell for viewing and modifying a vault file
 */

package com.jgptech.Locals.CLI;

import com.jgptech.Locals.Vault.Vault;

import javax.crypto.SecretKey;
import java.awt.*;
import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class Shell {
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
    private  void printHelp() {
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

    // List the groups in the vault or entries in the vault
    private void list(String[] words) {
        // Check that the user supplied the second argument
        if(words.length > 1) {
            // Check if the user wants to list groups or entries
            if(isGroupSelected(words[1])) {
                vault.listGroups(key);
            } else if(isEntrySelected(words[1])) {
                vault.listEntries(groupIndex, key);
            } else {
                System.out.println("ERROR: use 'list groups' or 'list entries'\n");
                printHelp();
            }
        } else {
            System.out.println("ERROR: use 'list groups' or 'list entries'\n");
            printHelp();
        }
    }

    // Open a group in the vault
    private void open(String[] words) {
        // Check that the user supplied the second argument
        if(words.length > 1) {
            // TODO: also allow the user to pass the name of the group to select it
            int newGroupIndex = Integer.parseInt(words[1]) - 1;

            if(newGroupIndex < 0 || newGroupIndex > vault.size()) {
                System.out.println("ERROR: invalid group index: " + words[1] + ". There are " + vault.size() + " groups in this vault.");
            } else {
                groupIndex = Integer.parseInt(words[1]) - 1;
            }
        } else {
            System.out.println("ERROR: add the group number that you wish to open: 'open <group-number>'");
            printHelp();
        }
    }

    // Show an entry's data
    private void show(String[] words) {
        // Check that the user supplied the second argument
        if(words.length > 1) {
            int entryIndex = Integer.parseInt(words[1]) - 1;

            System.out.println();
            System.out.println("Name: " + vault.getEntryName(groupIndex, entryIndex, key));
            System.out.println("Username: " + vault.getEntryUsername(groupIndex, entryIndex, key));
            System.out.println("Password: " + vault.getEntryPassword(groupIndex,entryIndex, key));
            System.out.println("URL: " + vault.getEntryUrl(groupIndex, entryIndex, key));
            System.out.println("Notes: " + vault.getEntryNotes(groupIndex, entryIndex, key));
            System.out.println();
        } else {
            System.out.println("ERROR: add the entry number you wish to show: 'show <entry-number>'");
            printHelp();
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

                System.out.print("Color: ");
                Color color = Color.RED; // TODO: put switch statement to get color from user, just using red for all for now

                vault.addGroup(name, color, key);
            } else if(isEntrySelected(words[1])) {
                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Username: ");
                String username = scanner.nextLine();

                String password = new String(console.readPassword("Password: "));

                System.out.print("URL: ");
                String url = scanner.nextLine();

                System.out.print("Notes: ");
                String notes = scanner.nextLine();

                vault.addEntry(groupIndex, key, name, username, password, url, notes);
            } else {
                System.out.println("ERROR: use 'add group' or 'add entry'");
            }

            // Write this to the vault to avoid data loss in the event the shell does not close properly
            vault.write();
        } else {
            System.out.println("ERROR: use 'add group' or 'add entry'");
            printHelp();
        }
    }

    // Remove a group or entry from the vault
    private void delete(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 2) {
            // Check if a user wants to remove a group or entry
            if(isGroupSelected(words[1])) {
                vault.removeGroup(Integer.parseInt(words[2]) - 1);
            } else if(isEntrySelected(words[1])) {
                vault.removeEntry(groupIndex, Integer.parseInt(words[2]) - 1);
            } else {
                System.out.println("ERROR: use 'delete group <group-number>' or 'delete entry <entry-number>'");
            }

            // Write this to the vault to avoid data loss in the event the shell does not close properly
            vault.write();
        } else {
            System.out.println("ERROR:  use 'delete group <group-number>' or 'delete entry <entry-number>'");
            printHelp();
        }
    }

    // Move an entry from one group to another
    private void move(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 3) {
            // TODO: add method to move groups in vault
            int entryIndex = Integer.parseInt(words[1]) - 1;
            int fromGroupIndex = Integer.parseInt(words[2]) - 1;
            int toGroupIndex = Integer.parseInt(words[3]) - 1;

            vault.moveEntry(fromGroupIndex, toGroupIndex, entryIndex);
        } else {
            System.out.println("ERROR: use 'move <entry-number> <current-group> <next-group>'");
            printHelp();
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
